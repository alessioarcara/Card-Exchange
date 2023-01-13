package com.aadm.cardexchange.shared.models;

import java.util.Objects;

public class CardDecorator implements Card {
    private static final long serialVersionUID = -6914752354287411438L;
    private CardImpl wrappee;

    public CardDecorator(CardImpl card) {
        wrappee = card;
    }

    protected CardDecorator() {
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

    public String getSpecialAttribute() {
        return "";
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
                && card.getType().equals(this.getType())
                && card.getSpecialAttribute().equals(this.getSpecialAttribute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getType(), getSpecialAttribute());
    }
}
