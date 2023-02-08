package com.aadm.cardexchange.shared.exceptions;


public class DeckNotFoundException extends BaseException {
    private static final long serialVersionUID = -9069721007995377785L;

    public DeckNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public DeckNotFoundException() {
    }
}
