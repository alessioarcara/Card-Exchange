package com.aadm.cardexchange.shared.models;


public class PhysicalCardWithName extends PhysicalCard {
    private static final long serialVersionUID = 3788889870604134028L;
    String name;

    public PhysicalCardWithName(PhysicalCard physicalCard, String name) {
        super(physicalCard.getId(), physicalCard.getCardId(), physicalCard.getStatus(), physicalCard.getDescription());
        this.name = name;
    }

    public PhysicalCardWithName() {
    }

    public String getName() {
        return name;
    }
}
