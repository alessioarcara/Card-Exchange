package com.aadm.cardexchange.shared.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private static final List<Status> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static Status randomStatus()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
