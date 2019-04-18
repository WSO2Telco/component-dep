package com.wso2telco.dep.user.masking.configuration;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Properties;

@Singleton
public class UserMaskingConfiguration {

    private static final Log log = LogFactory.getLog(UserMaskingConfiguration.class);
    private String userMaskingEnabled = null;
    private String secretKey = null;
    private String defaultMSISNDRegex = null;
    private String userIdFilterRegex = null;
    private String userMask = null;

    public UserMaskingConfiguration(){
        log.debug("########################## working ####################");
        log.error("########################## working error ####################");
        log.info("########################## working info ####################");
    }
    /**
     * Load user-masking.properties
     * pupulate user masking properties
     */
    @PostConstruct
    public void populateUserMaskingProperties(){
        log.debug("########################## pcworking ####################");
        log.error("##########################pc working error ####################");
        log.info("##########################pc working info ####################");
        Properties props = PropertyFileReader.getFileReader().getProperties(MaskingUtils.USER_MASKING_PROPERTIES_FILE);

        this.userMaskingEnabled = props.getProperty(MaskingUtils.USER_MASKING_ENABLED);
        this.secretKey = props.getProperty(MaskingUtils.USER_MASKING_SECRET_KEY);
        this.defaultMSISNDRegex = props.getProperty(MaskingUtils.USER_MASKING_DEFAULT_MSISDN_REGEX);
        this.userIdFilterRegex = props.getProperty(MaskingUtils.USER_MASKING_USER_ID_FILTER_REGEX);

        System.out.println("##############" + userMaskingEnabled + secretKey+ defaultMSISNDRegex+userIdFilterRegex);
    }

    public String getUserMaskingEnabled() {
        return userMaskingEnabled;
    }

    public void setUserMaskingEnabled(String userMaskingEnabled) {
        this.userMaskingEnabled = userMaskingEnabled;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getDefaultMSISNDRegex() {
        return defaultMSISNDRegex;
    }

    public void setDefaultMSISNDRegex(String defaultMSISNDRegex) {
        this.defaultMSISNDRegex = defaultMSISNDRegex;
    }

    public String getUserIdFilterRegex() {
        return userIdFilterRegex;
    }

    public void setUserIdFilterRegex(String userIdFilterRegex) {
        this.userIdFilterRegex = userIdFilterRegex;
    }

    public String getUserMask() {
        return userMask;
    }

    public void setUserMask(String userMask) {
        this.userMask = userMask;
    }

}
