package com.aadm.cardexchange.shared.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Game {
    MAGIC,
    POKEMON,
    YUGIOH;

    private static final List<Game> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Game randomGame()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}