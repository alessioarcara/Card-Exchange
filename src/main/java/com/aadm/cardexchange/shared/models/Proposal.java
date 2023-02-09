package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Proposal implements Serializable {
    private static final long serialVersionUID = 997487558313555532L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int id;
    private String senderUserEmail;
    private String receiverUserEmail;
    private List<PhysicalCard> senderPhysicalCards;
    private List<PhysicalCard> receiverPhysicalCards;

    public Proposal(String senderUserEmail, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards) {
        this.id = uniqueId.getAndIncrement();
        this.senderUserEmail = senderUserEmail;
        this.receiverUserEmail = receiverUserEmail;
        this.senderPhysicalCards = senderPhysicalCards;
        this.receiverPhysicalCards = receiverPhysicalCards;
    }

    public Proposal() {
    }

    public int getId() {
        return id;
    }

    public String getSenderUserEmail() {
        return senderUserEmail;
    }

    public String getReceiverUserEmail() {
        return receiverUserEmail;
    }

    public List<PhysicalCard> getSenderPhysicalCards() {
        return senderPhysicalCards;
    }

    public List<PhysicalCard> getReceiverPhysicalCards() {
        return receiverPhysicalCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposal proposal = (Proposal) o;
        return id == proposal.id &&
                ((Proposal) o).getSenderUserEmail().equals(proposal.getSenderUserEmail()) &&
                ((Proposal) o).getReceiverUserEmail().equals(proposal.getReceiverUserEmail()) &&
                ((Proposal) o).getSenderPhysicalCards().equals(proposal.getSenderPhysicalCards()) &&
                ((Proposal) o).getReceiverPhysicalCards().equals(proposal.getReceiverPhysicalCards());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderUserEmail, receiverUserEmail, senderPhysicalCards, receiverPhysicalCards);
    }
}
