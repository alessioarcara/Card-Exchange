package com.aadm.cardexchange.shared.exceptions;


public class AuthException extends BaseException {

    private static final long serialVersionUID = -1053746785960506740L;

    public AuthException(String errorMessage) {
        super(errorMessage);
    }

    public AuthException() {
    }
}
