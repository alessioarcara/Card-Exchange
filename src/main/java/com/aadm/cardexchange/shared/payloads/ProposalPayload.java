package com.aadm.cardexchange.shared.payloads;

import com.aadm.cardexchange.shared.models.PhysicalCardWithName;

import java.io.Serializable;
import java.util.List;

public class ProposalPayload implements Serializable {
    private static final long serialVersionUID = 1744595539331599230L;
    private String senderEmail;
    private String receiverEmail;
    private List<PhysicalCardWithName> senderCards;
    private List<PhysicalCardWithName> receiverCards;

    public ProposalPayload(String senderEmail, String receiverEmail, List<PhysicalCardWithName> senderCards, List<PhysicalCardWithName> receiverCards) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.senderCards = senderCards;
        this.receiverCards = receiverCards;
    }

    public ProposalPayload() {
    }

    public String getSenderEmail() {
        return senderEmail;
    }

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
