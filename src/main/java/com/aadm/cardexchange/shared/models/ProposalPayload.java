package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.List;

public class ProposalPayload implements Serializable {
    private static final long serialVersionUID = 1744595539331599230L;
    private String receiverEmail;
    private List<PhysicalCardWithName> senderCards;
    private List<PhysicalCardWithName> receiverCards;

    public ProposalPayload(String receiverEmail, List<PhysicalCardWithName> senderCards, List<PhysicalCardWithName> receiverCards) {
        this.receiverEmail = receiverEmail;
        this.senderCards = senderCards;
        this.receiverCards = receiverCards;
    }

    public ProposalPayload() {}

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public List<PhysicalCardWithName> getSenderCards() {
        return senderCards;
    }

    public List<PhysicalCardWithName> getReceiverCards() {
        return receiverCards;
    }
}
