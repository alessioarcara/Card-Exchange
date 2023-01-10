package com.aadm.cardexchange.shared.models;


public class YuGiOhCardDecorator extends CardDecorator {
    private static final long serialVersionUID = -5460276673235842624L;
    private String race;
    private String imageUrl;
    private String smallImageUrl;

    public YuGiOhCardDecorator(Card card, String race, String imageUrl, String smallImageUrl) {
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
}
