package com.wso2telco.dep.user.masking.configuration;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import java.util.Properties;

public class UserMaskingConfiguration {

    private static UserMaskingConfiguration SINGLE_INSTANCE = null;
    private static final Log log = LogFactory.getLog(UserMaskingConfiguration.class);
    private String userMaskingEnabled = null;
    private String secretKey = null;
    private String defaultMSISNDRegex = null;
    private String userIdFilterRegex = null;
    private String userMask = null;

    private UserMaskingConfiguration(){
        Properties props = PropertyFileReader.getFileReader().getProperties(MaskingUtils.USER_MASKING_PROPERTIES_FILE);

        this.userMaskingEnabled = props.getProperty(MaskingUtils.USER_MASKING_ENABLED);
        this.secretKey = props.getProperty(MaskingUtils.USER_MASKING_SECRET_KEY);
        this.defaultMSISNDRegex = props.getProperty(MaskingUtils.USER_MASKING_DEFAULT_MSISDN_REGEX);
        this.userIdFilterRegex = props.getProperty(MaskingUtils.USER_MASKING_USER_ID_FILTER_REGEX);

        log.debug("user masking property: [" + this.userMaskingEnabled + "] secret key: ["+
                this.secretKey + "] default MSISDN Regex: [" + this.defaultMSISNDRegex +
                "] user Id Filter Regex: [" + this.userIdFilterRegex + "]");
    }

    public static UserMaskingConfiguration getInstance(){// no synchronized methods because perf hit
        if(SINGLE_INSTANCE == null){
            synchronized (UserMaskingConfiguration.class){
                if(SINGLE_INSTANCE == null)// test again because eliminate recreating. few thread may wait at
                    // synchronized block and create few instances if this test not there
                SINGLE_INSTANCE = new UserMaskingConfiguration();
            }
        }

        return SINGLE_INSTANCE;
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
