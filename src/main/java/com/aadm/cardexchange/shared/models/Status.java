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

    public static Status getStatus(int value) {
        return value == 1 ? VeryDamaged :
                value == 2 ? Damaged :
                        value == 3 ? Fair :
                                value == 4 ? Good :
                                        value == 5 ? Excellent :
                                                null;
    }
}
