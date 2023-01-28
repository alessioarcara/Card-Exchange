package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Deck implements Serializable {
    private static final long serialVersionUID = -3036168098606868237L;
    private String userEmail;
    private String name;
    private boolean isDefault;
    private Set<PhysicalCardImpl> physicalCardImpls;

    public Deck(String userEmail, String name) {
        this.userEmail = userEmail;
        this.name = name;
        this.isDefault = false;
        physicalCardImpls = new LinkedHashSet<>();
    }

    public Deck(String userEmail, String name, boolean isDefault) {
        this.userEmail = userEmail;
        this.name = name;
        this.isDefault = isDefault;
        physicalCardImpls = new LinkedHashSet<>();
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

    public Set<PhysicalCardImpl> getPhysicalCards() {
        return physicalCardImpls;
    }

    public boolean addPhysicalCard(PhysicalCardImpl physicalCardImpl) {
        return physicalCardImpls.add(physicalCardImpl);
    }

    public boolean removePhysicalCard(PhysicalCardImpl physicalCardImplId) {
        return physicalCardImpls.remove(physicalCardImplId);
    }

     public boolean containsPhysicalCard(PhysicalCardImpl physicalCardImplId) {
        return physicalCardImpls.contains(physicalCardImplId);
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
