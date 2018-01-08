package com.wso2telco.dep.operatorservice.exception;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ThrowableError;

public class BlacklistException extends BusinessException {

    public BlacklistException(ThrowableError error) {
        super(error);
    }

    /**
     *
     */

    public enum BlacklistErrorType implements ThrowableError  {

        USER_ALREADY_BLACKLISTED("BE0001", "User already blacklisted"),
        INTERNAL_SERVER_ERROR("BE0001", "User already blacklisted");

        //all the msisdns already white listed for the given subscription
        BlacklistErrorType(final String code, final String msg) {

            this.code = code;
            this.msg = msg;
        }

        final String code;
        final String msg;

        public String getMessage() {

            return this.msg;
        }

        public String getCode(){

            return this.code;
        }
    }

    @Override
    public String toString() {
        return "BlacklistException " + getErrorType() + " , " + getMessage()+ "]";
    }


}
