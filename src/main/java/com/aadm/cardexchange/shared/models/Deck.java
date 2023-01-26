package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class Deck implements Serializable {
    private static final long serialVersionUID = -3036168098606868237L;
    private String userEmail;
    private String name;
    private boolean isDefault;
    private LinkedHashSet<Integer> physicalCards;

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

    public LinkedHashSet<Integer> getPhysicalCards() {
        return physicalCards;
    }

    public boolean addPhysicalCard(Integer physicalCardId) {
        return physicalCards.add(physicalCardId);
    }

    public boolean removePhysicalCard(Integer physicalCardId) {
        return physicalCards.remove(physicalCardId);
    }

     public boolean containsPhysicalCard(Integer physicalCardId) {
        return physicalCards.contains(physicalCardId);
     }
}

