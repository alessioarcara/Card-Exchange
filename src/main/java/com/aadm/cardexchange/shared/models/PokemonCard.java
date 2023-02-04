package com.aadm.cardexchange.shared.models;


import java.util.Objects;

public class PokemonCard extends Card {
    private static final long serialVersionUID = -2033966136995921050L;
    private String artist;
    private String imageUrl;
    private String rarity;

    public PokemonCard(String name, String description, String type, String artist, String imageUrl, String rarity, String... variants) {
        super(name, description, type, variants);
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.rarity = rarity;
    }

    public PokemonCard() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokemonCard)) return false;
        if (!super.equals(o)) return false;
        PokemonCard that = (PokemonCard) o;
        return Objects.equals(getArtist(), that.getArtist()) && Objects.equals(getImageUrl(), that.getImageUrl()) && Objects.equals(getRarity(), that.getRarity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getArtist(), getImageUrl(), getRarity());
    }
}