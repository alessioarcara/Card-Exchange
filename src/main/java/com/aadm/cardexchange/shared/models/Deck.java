package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Deck implements Serializable {
    private static final long serialVersionUID = -3036168098606868237L;
    private String userEmail;
    private String name;
    private boolean isDefault;
    private Set<PhysicalCard> physicalCards;

    public Deck(String userEmail, String name) {
        this.userEmail = userEmail;
        this.name = name;
        this.isDefault = false;
        physicalCards = new LinkedHashSet<>();
    }

    public Deck(String userEmail, String name, boolean isDefault) {
        this.userEmail = userEmail;
        this.name = name;
        this.isDefault = isDefault;
        physicalCards = new LinkedHashSet<>();
    }

    public Deck() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!isDefault){
            this.name = name;
        }
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Set<PhysicalCard> getPhysicalCards() {
        return physicalCards;
    }

    public boolean addPhysicalCard(PhysicalCard physicalCard) {
        return physicalCards.add(physicalCard);
    }

    public boolean removePhysicalCard(PhysicalCard physicalCardId) {
        return physicalCards.remove(physicalCardId);
    }

     public boolean containsPhysicalCard(PhysicalCard physicalCardId) {
        return physicalCards.contains(physicalCardId);
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
