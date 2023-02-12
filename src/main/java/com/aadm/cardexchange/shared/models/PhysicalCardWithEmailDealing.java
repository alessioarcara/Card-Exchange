package com.aadm.cardexchange.shared.models;


public class PhysicalCardWithEmailDealing extends PhysicalCardWithEmail {
    private static final long serialVersionUID = -5663782446249211479L;
    String idPhysicalCardPawn;

    public PhysicalCardWithEmailDealing(PhysicalCardWithEmail physicalCardWithEmail, String idPhysicalCardPawn) {
        super(physicalCardWithEmail, physicalCardWithEmail.getEmail());
        this.idPhysicalCardPawn = idPhysicalCardPawn;
    }

    public PhysicalCardWithEmailDealing() {
    }

    public String getIdPhysicalCardPawn() {
        return idPhysicalCardPawn;
    }
}
