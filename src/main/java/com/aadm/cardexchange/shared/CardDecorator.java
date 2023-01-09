package com.aadm.cardexchange.shared;

public abstract class CardDecorator implements Card {
    private final Card wrappee;

    protected CardDecorator(Card card) {
        wrappee = card;
    }

    @Override
    public String getName() {
        return wrappee.getName();
    }

    @Override
    public String getDescription() {
        return wrappee.getDescription();
    }

    @Override
    public String getType() {
        return wrappee.getType();
    }
}
