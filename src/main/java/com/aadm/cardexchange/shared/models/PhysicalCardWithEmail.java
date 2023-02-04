package com.aadm.cardexchange.shared.models;


public class PhysicalCardWithEmail extends PhysicalCard {
    private static final long serialVersionUID = -4735395411575040587L;
    String email;

    public PhysicalCardWithEmail(PhysicalCard physicalCard, String email) {
        super(physicalCard.getId(), physicalCard.getCardId(), physicalCard.getStatus(), physicalCard.getDescription());
        this.email = email;
    }

    public PhysicalCardWithEmail() {
    }

    public String getEmail() {
        return email;
    }
}
