package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Card implements Serializable {
    private static final long serialVersionUID = -6914752354287411438L;
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int id;
    private String name;
    private String description;
    private String type;
    private Set<String> variants;

    public Card(String name, String description, String type, String... variants) {
        this.id = uniqueId.incrementAndGet();
        this.name = name;
        this.description = description;
        this.type = type;
        this.variants = new HashSet<>();
        Collections.addAll(this.variants, variants);
    }

    protected Card() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public Set<String> getVariants() {
        return variants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return getName().equals(card.getName()) && getDescription().equals(card.getDescription()) && getType().equals(card.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getType());
    }
}
