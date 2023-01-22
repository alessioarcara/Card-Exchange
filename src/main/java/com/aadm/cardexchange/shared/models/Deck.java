package com.aadm.cardexchange.shared.models;

import java.util.ArrayList;

public class Deck {
    private String userEmail;
    private String name;
    private boolean isDefault;
    private ArrayList<Integer> physicalCards;

    public Deck(String userEmail, String name) {
        this.userEmail = userEmail;
        this.name = name;
        this.isDefault = false;
        physicalCards = new ArrayList<>();
    }

    public Deck(String userEmail, String name, boolean isDefault) {
        this.userEmail = userEmail;
        this.name = name;
        this.isDefault = isDefault;
        physicalCards = new ArrayList<>();
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
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public ArrayList<Integer> getPhysicalCards() {
        return physicalCards;
    }

    public boolean addPhysicalCard(Integer physicalCardId) {
        if (!physicalCards.contains(physicalCardId)) {
            physicalCards.add(physicalCardId);
            return true;
        }
        return false;
    }

    public boolean removePhysicalCard(Integer physicalCardId) {
        if (physicalCards.contains(physicalCardId)) {
            physicalCards.remove(physicalCardId);
            return true;
        }
        return false;
    }

     public boolean containsPhysicalCard(Integer physicalCardId) {
        return physicalCards.contains(physicalCardId);
     }

}

