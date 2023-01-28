package com.aadm.cardexchange.shared.models;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class PhysicalCardDecorator implements PhysicalCard {
    private static final long serialVersionUID = -7398199082789935757L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int innerID;
    private PhysicalCardImpl wrapped;

    private String name;

    public PhysicalCardDecorator(PhysicalCardImpl wrapped, String name) {
        this.innerID = uniqueId.getAndIncrement();
        this.wrapped = wrapped;
        this.name = name;
    }

    public PhysicalCardDecorator() {
    }

    public int getInnerID() { return innerID; }

    @Override
    public String getId() {
        return wrapped.getId();
    }

    @Override
    public int getCardId() {
        return wrapped.getCardId();
    }

    @Override
    public Status getStatus() {
        return wrapped.getStatus();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public Game getGameType() {
        return wrapped.getGameType();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PhysicalCardDecorator)) {
            return false;
        }
        PhysicalCardDecorator card = (PhysicalCardDecorator) o;
        return (card.getInnerID() == this.getInnerID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInnerID(), getId(), getCardId(), getStatus(), getDescription(), getGameType(), getName());
    }
}
