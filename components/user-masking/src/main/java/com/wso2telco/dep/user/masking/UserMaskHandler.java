package com.wso2telco.dep.user.masking;

import com.wso2telco.dep.user.masking.utils.Constants;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class UserMaskHandler {

    private static final Log log = LogFactory.getLog(UserMaskHandler.class);

    public static String maskUserId(String userId, boolean encript, String secretKey) {
        String userPrefix = "";
        String returnedUserId = userId;
        try {
            if (userId.startsWith(Constants.ETEL_1)) {
                userId = userId.substring(6).trim();
                userPrefix = Constants.ETEL_1;
            } else if (userId.startsWith(Constants.TEL_1) || userId.startsWith(Constants.ETEL_2)) {
                userId = userId.substring(5);
                userPrefix = Constants.TEL_1;
            } else if (userId.startsWith(Constants.TEL_2) || userId.startsWith(Constants.ETEL_3)) {
                userId = userId.substring(4);
                userPrefix = Constants.TEL_2;
            } else if (userId.startsWith(Constants.TEL_3)) {
                userPrefix = Constants.TEL_3;
                userId = userId.substring(3);
            } else if (userId.startsWith(Constants.PLUS)) {
                userPrefix = Constants.PLUS;
                userId = userId.substring(1);
            }

            if (secretKey != null && !secretKey.isEmpty()) {
                if (encript) {
                    returnedUserId = encrypt(userId, secretKey);
                } else {
                    returnedUserId = decrypt(userId, secretKey);
                }
                //append prefix
                returnedUserId = userPrefix + returnedUserId;
            } else {
                log.error("Error while getting configuration, MSISDN_ENCRIPTION_KEY is not provided");
            }
        } catch (Exception e) {
            log.error("Error while masking/unmasking user ID " + e);
        }
        return  returnedUserId;
    }

    /**
     *
     * @param userId
     * @return get unmasked user ID
     */
    public static String getUserMask(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            String maskingSecretKey = MaskingUtils.getUserMaskingConfiguration("user.masking.feature.masking.secret.key");
            userId =  maskUserId(userId, true, maskingSecretKey);
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
            String maskingSecretKey = MaskingUtils.getUserMaskingConfiguration("user.masking.feature.masking.secret.key");
            userMask = maskUserId(userMask, false, maskingSecretKey);
        }
        return  userMask;
    }

    /**
     * Validate given user is a masked user
     * @param userId
     * @return true if a masked user ID
     */
    public static boolean isMaskedUserId(String userId) {
        String defaultUserIdPattern = MaskingUtils.getUserMaskingConfiguration("default.msisdn.regex");
        return  UserMaskHandler.isMaskedUserId(userId, defaultUserIdPattern);
    }

    /**
     * Validate given user is a masked user
     * @param userId
     * @param msisdnRegex
     * @return true if a masked user ID
     */
    public static boolean isMaskedUserId(String userId, String msisdnRegex) {
        boolean isAMaskedUserId = false;
        // If the provided user id is matched with default msidsn, then it is not a masked user ID
        if (msisdnRegex != null && !msisdnRegex.isEmpty()) {
            isAMaskedUserId = !userId.matches(msisdnRegex);
        }
        return  isAMaskedUserId;
    }

    /**
     *
     * @param userId
     * @param secretKey
     * @return Encripted User ID
     */
    private static String encrypt(String userId, String secretKey) throws  Exception {
        String maskedId = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(secretKey));
            maskedId =  new String(Base64.encodeBase64(cipher.doFinal(userId.getBytes("UTF-8"))));
        } catch (Exception e) {
            log.error("Error while encrypting: " + e);
            throw e;
        }
        return maskedId;
    }

    /**
     *
     * @param maskedUserId
     * @param secretKey
     * @return User Id decoded
     */
    private static String decrypt(String maskedUserId, String secretKey) throws Exception {
        String userId = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(secretKey));
            userId = new String(cipher.doFinal(Base64.decodeBase64(maskedUserId.getBytes())));
        } catch (Exception e) {
            log.error("Error while decrypting masked User ID : " +  maskedUserId,  e);
            throw e;
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
            log.error("Error while getting  SecretKeySpec: " ,  e);
            throw e;
        } catch (UnsupportedEncodingException e) {
            log.error("Error while getting SecretKeySpec : ",  e);
            throw e;
        }
        return secretKeySpec;
    }
}
