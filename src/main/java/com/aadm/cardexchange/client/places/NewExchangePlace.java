package com.aadm.cardexchange.client.places;

import com.google.gwt.place.shared.Place;

public class NewExchangePlace extends Place {
    private final String selectedCardId;
    private final String receiverUserEmail;

    public NewExchangePlace(String selectedCardId, String receiverUserEmail) {
        this.selectedCardId = selectedCardId;
        this.receiverUserEmail = receiverUserEmail;
    }

    public String getSelectedCardId() {
        return selectedCardId;
    }

    public String getReceiverUserEmail() {
        return receiverUserEmail;
    }
}
