package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public class DeckException extends Exception implements Serializable {
    private static final long serialVersionUID = 5970427549028882302L;
    private String errorMessage;

    public DeckException() {
    }

    public DeckException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
