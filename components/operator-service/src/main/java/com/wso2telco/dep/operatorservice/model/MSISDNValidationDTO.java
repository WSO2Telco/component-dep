package com.wso2telco.dep.operatorservice.model;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.wso2telco.dep.oneapivalidation.util.MsisdnDTO;

public class MSISDNValidationDTO {

    private List<String> valid = null;
    private String validationRegex = null;
    private int validationPrefixGroup = 0;
    private int validationDigitsGroup = 0;
    private transient List<MsisdnDTO> valid_temp = null;

    /**
     * Ensure .process() method is called first!
     * @return
     */
    public List<MsisdnDTO> getValidProcessed() {
        return valid_temp;
    }

    public List<String> getValid() {
        return valid;
    }

    public String getValidationRegex() {
        return validationRegex;
    }

    public void setValid(List<String> valid) {
        this.valid = valid;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    public int getValidationPrefixGroup() {
        return validationPrefixGroup;
    }

    public void setValidationPrefixGroup(int validationPrefixGroup) {
        this.validationPrefixGroup = validationPrefixGroup;
    }

    public int getValidationDigitsGroup() {
        return validationDigitsGroup;
    }

    public void setValidationDigitsGroup(int validationDigitsGroup) {
        this.validationDigitsGroup = validationDigitsGroup;
    }

    /**
     * This method should be called if you need the prefix and digits separately
     */
    public void process() {
        if (valid != null && valid_temp == null) {
            valid_temp = new ArrayList<MsisdnDTO>();
            Pattern pattern = Pattern.compile(validationRegex);
            Matcher matcher;

            for(String msisdn : valid){
                matcher = pattern.matcher(msisdn);
                if(matcher.matches()){
                    valid_temp.add(new MsisdnDTO(matcher.group(validationPrefixGroup),matcher.group(validationDigitsGroup)));
                }
            }
        }
    }
}
