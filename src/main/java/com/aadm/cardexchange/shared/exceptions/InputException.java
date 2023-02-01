package com.aadm.cardexchange.shared.exceptions;


public class InputException extends BaseException {

    private static final long serialVersionUID = -5839756177404814542L;

    public InputException(String errorMessage) {
        super(errorMessage);
    }

    public InputException() {
    }
}
