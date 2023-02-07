package com.aadm.cardexchange.shared.models;


public class PhysicalCardWithEmailDealing extends PhysicalCardWithEmail {
    private static final long serialVersionUID = -5663782446249211479L;
    String idPhysicalCarPawn;

    public PhysicalCardWithEmailDealing(PhysicalCardWithEmail physicalCardWithEmail, String idPhysicalCarPawn) {
        super(physicalCardWithEmail, physicalCardWithEmail.getEmail());
        this.idPhysicalCarPawn = idPhysicalCarPawn;
    }

    public PhysicalCardWithEmailDealing() {
    }

    public String getIdPhysicalCarPawn() {
        return idPhysicalCarPawn;
    }
}
