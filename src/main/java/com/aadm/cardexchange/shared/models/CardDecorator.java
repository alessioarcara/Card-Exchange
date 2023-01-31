package com.aadm.cardexchange.shared.models;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class CardDecorator implements Card {
    private static final long serialVersionUID = -6914752354287411438L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int id;
    private CardImpl wrapped;

    public CardDecorator(CardImpl card) {
        this.id = uniqueId.incrementAndGet();
        wrapped = card;
    }

    protected CardDecorator() {
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

   // public static String getStaticName(int id) {return  .getName();}

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public String getType() {
        return wrapped.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CardDecorator)) {
            return false;
        }
        CardDecorator card = (CardDecorator) o;
        return card.getName().equals(this.getName())
                && card.getDescription().equals(this.getDescription())
                && card.getType().equals(this.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getType());
    }
}
