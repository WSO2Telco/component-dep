/*******************************************************************************
 * Copyright  (c) 2019, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.user.masking;

import com.wso2telco.dep.user.masking.configuration.UserMaskingConfiguration;
import com.wso2telco.dep.user.masking.exceptions.UserMaskingException;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class UserMaskHandler {

    private static final Log log = LogFactory.getLog(UserMaskHandler.class);

    /**
     *
     * @param userId
     * @param encrypt
     * @param secretKey
     * @return get masked/unmasked user ID
     */
    public static String transcryptUserId(String userId, boolean encrypt, String secretKey) throws UserMaskingException {
        String returnedUserId = userId;
        UserId userIdObj = new UserId(userId);

        if (secretKey != null && !secretKey.isEmpty()) {
            if (encrypt) {
                returnedUserId = encrypt(userIdObj.getReturnUserId(), secretKey);
            } else {
                returnedUserId = decrypt(userIdObj.getReturnUserId(), secretKey);
            }
        } else {
            log.error("Error while getting configuration, MSISDN_ENCRYPTION_KEY is not provided");
            userIdObj.setUserPrefix("");
        }

        return userIdObj.getUserPrefix() + returnedUserId;
    }

    /**
     *
     * @param userId
     * @return get unmasked user ID
     */
    public static String getUserMask(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            String maskingSecretKey = UserMaskingConfiguration.getInstance().getSecretKey();

            try {
                return transcryptUserId(userId, true, maskingSecretKey);
            } catch (UserMaskingException ume) {
                log.warn("msisdn is already masked or incorrect user mask configuration exists.", ume);
                return userId;
            }
        }
        return userId;
    }

    /**
     *
     * @param userId
     * @return get unmasked user ID
     */
    public static String getUserMaskIfAllowed(String userId) {
        try {
            if (StringUtils.isNotEmpty(userId) && MaskingUtils.isUserAnonymizationEnabled() &&
                    MaskingUtils.isUnmaskedUserId(userId)) {
                String maskingSecretKey = UserMaskingConfiguration.getInstance().getSecretKey();

                return transcryptUserId(userId, true, maskingSecretKey);
            }
        } catch (UserMaskingException ume) {
            log.warn("msisdn is already masked or incorrect user mask configuration exists. ", ume);
            return userId;
        }
        return userId;
    }

    /**
     * Get user Id for user mask
     * @param userMask
     * @return
     */
    public static String getUserId(String userMask) {
        if (StringUtils.isNotEmpty(userMask)) {
            String maskingSecretKey = UserMaskingConfiguration.getInstance().getSecretKey();
            try {
                return transcryptUserId(userMask, false, maskingSecretKey);
            } catch (UserMaskingException ume) {
                log.warn("msisdn is already unmasked or incorrect user mask configuration exists.", ume);
                return userMask;
            }
        }
        return userMask;
    }

    /**
     * Get user Id for user mask
     * @param userMask
     * @return
     */
    public static String getProperlyMaskedUserId(String userMask) {
        if (StringUtils.isNotEmpty(userMask)) {
            String maskingSecretKey = UserMaskingConfiguration.getInstance().getSecretKey();
            try {
                return transcryptUserId(userMask, false, maskingSecretKey);
            } catch (UserMaskingException ume) {
                if (isCorrectlyFormattedMSISDN(userMask)){
                    return userMask;
                }
                log.warn("msisdn is already unmasked, incorrect user mask configuration exists or previous user " +
                        "masking configuration changed", ume);
            }
        }
        return userMask;
    }

    /**
     * Validate given user is a masked user
     * @param userId
     * @return true if a masked user ID
     */
    public static boolean isMaskedUserId(String userId) {
        String defaultRegex = UserMaskingConfiguration.getInstance().getDefaultMSISNDRegex();
        return  !userId.matches(defaultRegex);
    }

    /**
     * Validate given user is a masked user
     * @param userId
     * @return true if a masked user ID
     */
    public static boolean isCorrectlyFormattedMSISDN(String userId) {
        String defaultRegex = UserMaskingConfiguration.getInstance().getDefaultMSISNDRegex();
        return  userId.matches(defaultRegex);
    }

    /**
     *
     * @param userId
     * @param secretKey
     * @return Encrypted User ID
     */
    private static String encrypt(String userId, String secretKey) throws UserMaskingException {
        String maskedId = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(secretKey));
            maskedId =  new String(Base64.encodeBase64(cipher.doFinal(userId.getBytes("UTF-8"))));
        } catch (BadPaddingException bpe) {
            log.error("Error occurred while unmasking. Possible reason would be incorrect masking configuration." , bpe);
            throw new UserMaskingException("Error occurred while unmasking. Possible reason would be incorrect masking configuration.");
        } catch (Exception e) {
            log.error("Error while encrypting." + e);
            throw new UserMaskingException("Error while encrypting.");
        }
        return maskedId;
    }

    /**
     *
     * @param maskedUserId
     * @param secretKey
     * @return User Id decoded
     */
    private static String decrypt(String maskedUserId, String secretKey) throws UserMaskingException {
        String userId = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(secretKey));
            userId = new String(cipher.doFinal(Base64.decodeBase64(maskedUserId.getBytes())));
        } catch (BadPaddingException bpe) {
            log.error("Error occurred while unmasking. Possible reason would be incorrect masking configuration." , bpe);
            throw new UserMaskingException("Error while encrypting.");
        } catch (Exception e) {
            log.error("Error while decrypting User ID : " +  maskedUserId + "Possible reasons may be incorrect " +
                    "user mask configuration, configuration changed without proper migration or already un-masked user id", e);

            throw new UserMaskingException("Error while decrypting User ID : " +  maskedUserId + "Possible reasons may be incorrect " +
                    "user mask configuration, configuration changed without proper migration or already un-masked user id");
        }
        return userId;
    }

    private static SecretKeySpec getSecretKeySpec(String secretKey) throws Exception {
        MessageDigest sha = null;
        SecretKeySpec secretKeySpec = null;
        try {
            byte[] key = secretKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            log.error("Error while getting  SecretKeySpec.");
            throw e;
        } catch (UnsupportedEncodingException e) {
            log.error("Error while getting SecretKeySpec.");
            throw e;
        }
        return secretKeySpec;
    }

    private static class UserId{
        private String userPrefix = "";
        private String returnUserId = "";

        public UserId(String userId){
            separateUserId(userId);
        }

        /**
         *
         * @param userId
         */
        private void separateUserId(String userId){
            if (userId.startsWith("tel:+")) {
                returnUserId = userId.substring(5);
                userPrefix = "tel:+";
            } else if (userId.startsWith("tel:")) {
                returnUserId = userId.substring(4);
                userPrefix = "tel:";
            } else if (userId.startsWith("tel")) {
                returnUserId = userId.substring(3);
                userPrefix = "tel";
            } else if (userId.startsWith("+")) {
                returnUserId = userId.substring(1);
                userPrefix = "+";
            } else {
                returnUserId = userId;
            }
        }

        public void setUserPrefix(String userPrefix) {
            this.userPrefix = userPrefix;
        }

        public String getUserPrefix() {
            return userPrefix;
        }

        public String getReturnUserId() {
            return returnUserId;
        }
    }
}
