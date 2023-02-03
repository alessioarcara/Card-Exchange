package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Card implements Serializable {
    private static final long serialVersionUID = -6914752354287411438L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int id;
    Game gameType;
    private Set<Property> properties;
    private Set<String> variants;

    public Card(Game game, Property[] properties, String... variants) {
        this.id = uniqueId.incrementAndGet();
        this.gameType = game;
        this.properties = new HashSet<>();
        this.variants = new HashSet<>();
        Collections.addAll(this.properties, properties);
        Collections.addAll(this.variants, variants);
    }

    protected Card() {
    }

    public int getId() {
        return id;
    }

    public Game getGameType() {
        return gameType;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public String getProperty(String propertyName) {
        for (Property prop : properties) {
            if (prop.getName().equals(propertyName)) {
                return prop.value;
            }
        }
        return null;
    }

    public Set<String> getVariants() {
        return variants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return getId() == card.getId() && getGameType() == card.getGameType() && Objects.equals(getProperties(), card.getProperties()) && Objects.equals(getVariants(), card.getVariants());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGameType(), getProperties(), getVariants());
    }
}
