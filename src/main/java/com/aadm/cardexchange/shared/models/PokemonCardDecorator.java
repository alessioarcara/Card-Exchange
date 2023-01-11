package com.aadm.cardexchange.shared.models;


public class PokemonCardDecorator extends CardDecorator {
    private static final long serialVersionUID = -2033966136995921050L;
    private String artist;
    private String imageUrl;
    private String rarity;
    private boolean isFirstEdition;
    private boolean isHolo;
    private boolean isNormal;
    private boolean isReverse;
    private boolean isPromo;

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

    public PokemonCardDecorator() {
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
