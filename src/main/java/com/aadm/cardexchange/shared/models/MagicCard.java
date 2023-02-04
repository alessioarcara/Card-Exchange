package com.aadm.cardexchange.shared.models;


import java.util.Objects;

public class MagicCard extends Card {
    private static final long serialVersionUID = 2368581922806822154L;
    private String artist;
    private String rarity;

    public MagicCard(String name, String description, String type, String artist, String rarity, String... variants) {
        super(name, description, type, variants);
        this.artist = artist;
        this.rarity = rarity;
    }

    public MagicCard() {
    }

    public String getArtist() {
        return artist;
    }

    public String getRarity() {
        return rarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MagicCard)) return false;
        if (!super.equals(o)) return false;
        MagicCard magicCard = (MagicCard) o;
        return Objects.equals(getArtist(), magicCard.getArtist()) && Objects.equals(getRarity(), magicCard.getRarity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getArtist(), getRarity());
    }
}
