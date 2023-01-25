package com.aadm.cardexchange.shared.models;

public enum Status {
    VeryDamaged(1), Damaged(2), Fair(3), Good(4), Excellent(5);
    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
