package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public class AuthException extends Exception implements Serializable {
    private static final long serialVersionUID = 5970425099128882302L;
    private String errorMessage;

    public AuthException() {
    }

    public AuthException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
