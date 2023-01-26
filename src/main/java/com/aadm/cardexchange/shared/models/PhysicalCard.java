package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class PhysicalCard implements Serializable {
    private static final long serialVersionUID = -8705711710936945407L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int id;
    private int cardId;
    private String status;
    private String description;

    public PhysicalCard(int cardId, String status, String description) {
        this.id = uniqueId.getAndIncrement();
        this.cardId = cardId;
        this.status = status;
        this.description = description;
    }

    public PhysicalCard() {
    }

    public int getId() {
        return id;
    }

    public int getCardId() {
        return cardId;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
