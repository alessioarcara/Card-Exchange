package com.aadm.cardexchange.client.places;

import com.google.gwt.place.shared.Place;

public class ExchangePlace extends Place {
    private final int proposalId;

    public ExchangePlace(int proposalId) {
        this.proposalId = proposalId;
    }

    public int getExchangeProposalId() {
        return proposalId;
    }
}
