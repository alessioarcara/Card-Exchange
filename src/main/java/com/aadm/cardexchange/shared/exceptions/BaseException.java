package com.aadm.cardexchange.shared.exceptions;

import java.io.Serializable;

public class BaseException extends Exception implements Serializable {
    private static final long serialVersionUID = -2246440636124113708L;
    private String errorMessage;

    public BaseException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BaseException() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
