package com.wso2telco.dep.user.masking.exceptions;

public class UserMaskingException extends Exception{
    /**
     * Instantiates a new UserMasking exception.
     *
     * @param message the message
     */
    public UserMaskingException(String message) {
        super(message);
    }

    /**
     * Instantiates a new UserMasking exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public UserMaskingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new UserMasking exception.
     *
     * @param cause the cause
     */
    public UserMaskingException(Throwable cause) {
        super(cause);
    }
}
