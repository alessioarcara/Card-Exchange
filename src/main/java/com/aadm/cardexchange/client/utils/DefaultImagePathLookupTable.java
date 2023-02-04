package com.aadm.cardexchange.client.utils;

import com.aadm.cardexchange.shared.models.Game;

import java.util.HashMap;
import java.util.Map;

public class DefaultImagePathLookupTable {
    private static final Map<Game, String> lookupTable = new HashMap<Game, String>() {{
        put(Game.MAGIC, "placeholders/magic-placeholder.png");
        put(Game.POKEMON, "placeholders/pokemon-placeholder.png");
        put(Game.YUGIOH, "placeholders/yugioh-placeholder.png");
    }};

    public static String getPath(Game game) {
        return lookupTable.get(game);
    }
}
