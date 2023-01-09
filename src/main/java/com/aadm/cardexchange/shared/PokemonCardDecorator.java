package com.aadm.cardexchange.shared;

public class PokemonCardDecorator extends CardDecorator {
    private final String artist;
    private final String imageUrl;
    private final String rarity;
    private final boolean isFirstEdition;
    private final boolean isHolo;
    private final boolean isNormal;
    private final boolean isReverse;
    private final boolean isPromo;

    public PokemonCardDecorator(Card card, String artist, String imageUrl, String rarity, boolean isFirstEdition, boolean isHolo, boolean isNormal, boolean isReverse, boolean isPromo) {
        super(card);
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.rarity = rarity;
        this.isFirstEdition = isFirstEdition;
        this.isHolo = isHolo;
        this.isNormal = isNormal;
        this.isReverse = isReverse;
        this.isPromo = isPromo;
    }

    public String getArtist() {
        return artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRarity() {
        return rarity;
    }

    public boolean getIsFirstEdition() {
        return isFirstEdition;
    }

    public boolean getIsHolo() {
        return isHolo;
    }

    public boolean getIsNormal() {
        return isNormal;
    }

    public boolean getIsReverse() {
        return isReverse;
    }

    public boolean getIsPromo() {
        return isPromo;
    }
}
