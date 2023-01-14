package com.aadm.cardexchange.shared.models;


import java.util.Objects;

public class YuGiOhCardDecorator extends CardDecorator {
    private static final long serialVersionUID = -5460276673235842624L;
    private String race;
    private String imageUrl;
    private String smallImageUrl;

    public YuGiOhCardDecorator(CardImpl card, String race, String imageUrl, String smallImageUrl) {
        super(card);
        this.race = race;
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
    }

    public YuGiOhCardDecorator() {
    }

    public String getRace() {
        return race;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YuGiOhCardDecorator)) return false;
        if (!super.equals(o)) return false;
        YuGiOhCardDecorator that = (YuGiOhCardDecorator) o;
        return getRace().equals(that.getRace()) && getImageUrl().equals(that.getImageUrl()) && getSmallImageUrl().equals(that.getSmallImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRace(), getImageUrl(), getSmallImageUrl());
    }
}
