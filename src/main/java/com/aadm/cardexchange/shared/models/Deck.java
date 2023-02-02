package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Deck implements Serializable {
    private static final long serialVersionUID = -3036168098606868237L;
    private String name;
    private boolean isDefault;
    private Set<PhysicalCardImpl> physicalCards;

    public Deck(String name) {
        this.name = name;
        this.isDefault = false;
        physicalCards = new LinkedHashSet<>();
    }

    public Deck(String name, boolean isDefault) {
        this.name = name;
        this.isDefault = isDefault;
        physicalCards = new LinkedHashSet<>();
    }

    public Deck() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!isDefault) {
            this.name = name;
        }
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Set<PhysicalCardImpl> getPhysicalCards() {
        return physicalCards;
    }

    public boolean addPhysicalCard(PhysicalCardImpl physicalCard) {
        return physicalCards.add(physicalCard);
    }

    public boolean removePhysicalCard(PhysicalCardImpl physicalCard) {
        return physicalCards.remove(physicalCard);
    }

    public boolean containsPhysicalCard(PhysicalCardImpl physicalCard) {
        return physicalCards.contains(physicalCard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return name.equals(deck.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
