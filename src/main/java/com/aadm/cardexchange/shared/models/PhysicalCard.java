package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class PhysicalCard implements Serializable {
    private static final long serialVersionUID = -8705711710936945407L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private String id;
    private int cardId;
    private Status status;
    private String description;

    public PhysicalCard(Game game, int cardId, Status status, String description) {
        this.id = (game == Game.MAGIC ? "m" : game == Game.POKEMON ? "p" : game == Game.YUGIOH ? "y" : "")
                + uniqueId.getAndIncrement();
        this.cardId = cardId;
        this.status = status;
        this.description = description;
    }

    // create a copy
    protected PhysicalCard(String id, int cardId, Status status, String description) {
        this.id = id;
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

    public Game getGameType() {
        return id.charAt(0) == 'm' ? Game.MAGIC :
                id.charAt(0) == 'y' ? Game.YUGIOH :
                        id.charAt(0) == 'p' ? Game.POKEMON :
                                null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalCard)) return false;
        PhysicalCard that = (PhysicalCard) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
