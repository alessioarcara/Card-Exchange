package com.aadm.cardexchange.client.places;

import com.google.gwt.place.shared.Place;

public class ProposalPlace extends Place {
    private final int proposalId;

    public ProposalPlace(int proposalId) {
        this.proposalId = proposalId;
    }

    public int getProposalId() {
        return proposalId;
    }
}
