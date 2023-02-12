package com.aadm.cardexchange.client.places;

import com.google.gwt.place.shared.Place;

public class ExchangesPlace extends Place {
    private final Integer proposalId;

    public ExchangesPlace(Integer proposalId) {
        this.proposalId = proposalId;
    }

    public Integer getProposalId() {
        return proposalId;
    }
}
