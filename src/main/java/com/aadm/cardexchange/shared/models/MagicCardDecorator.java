package com.aadm.cardexchange.shared.models;


public class MagicCardDecorator extends CardDecorator {
    private static final long serialVersionUID = 2368581922806822154L;
    private String artist;
    private String rarity;
    private boolean hasFoil;
    private boolean isAlternative;
    private boolean isFullArt;
    private boolean isPromo;
    private boolean isReprint;


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

    public MagicCardDecorator() {
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
