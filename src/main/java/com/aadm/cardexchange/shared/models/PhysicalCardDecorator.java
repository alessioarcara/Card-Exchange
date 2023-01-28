package com.aadm.cardexchange.shared.models;

public class PhysicalCardDecorator implements PhysicalCard {
    private static final long serialVersionUID = -7398199082789935757L;
    private PhysicalCardImpl wrapped;

    private String name;

    public PhysicalCardDecorator(PhysicalCardImpl wrapped, String name) {
        this.wrapped = wrapped;
        this.name = name;
    }

    public PhysicalCardDecorator() {
    }

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
}
