package com.aadm.cardexchange.shared;

public class YuGiOhCardDecorator extends CardDecorator {
    private final String race;
    private final String imageUrl;
    private final String smallImageUrl;

    public YuGiOhCardDecorator(Card card, String race, String imageUrl, String smallImageUrl) {
        super(card);
        this.race = race;
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
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
