package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class PhysicalCard implements Serializable {
    private static final long serialVersionUID = -8705711710936945407L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int id;
    private int cardId;
    private Status status;
    private String description;

    public PhysicalCard(Game game, int cardId, Status status, String description) {
        this.id = (game == Game.Magic ? "m" : game == Game.Pokemon ? "p" : game == Game.YuGiOh ? "y" : "")
                + uniqueId.getAndIncrement();
        this.cardId = cardId;
        this.status = status;
        this.description = description;
    }

    public PhysicalCard() {
    }

    public String getId() {
        return id;
    }

    public int getCardId() {
        return cardId;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalCard)) return false;
        PhysicalCard that = (PhysicalCard) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
