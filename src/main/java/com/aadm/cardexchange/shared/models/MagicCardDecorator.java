package com.aadm.cardexchange.shared.models;


public class MagicCardDecorator extends CardDecorator {
    private final String artist;
    private final String rarity;
    private final boolean hasFoil;
    private final boolean isAlternative;
    private final boolean isFullArt;
    private final boolean isPromo;
    private final boolean isReprint;


    public MagicCardDecorator(Card card, String artist, String rarity, boolean hasFoil, boolean isAlternative, boolean isFullArt, boolean isPromo, boolean isReprint) {
        super(card);
        this.artist = artist;
        this.rarity = rarity;
        this.hasFoil = hasFoil;
        this.isAlternative = isAlternative;
        this.isFullArt = isFullArt;
        this.isPromo = isPromo;
        this.isReprint = isReprint;
    }

    public String getArtist() {
        return artist;
    }

    public String getRarity() {
        return rarity;
    }

    public boolean getHasFoil() {
        return hasFoil;
    }

    public boolean getIsAlternative() {
        return isAlternative;
    }

    public boolean getIsFullArt() {
        return isFullArt;
    }

    public boolean getIsPromo() {
        return isPromo;
    }

    public boolean getIsReprint() {
        return isReprint;
    }
}
