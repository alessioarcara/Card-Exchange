package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.*;
import com.google.gson.Gson;
import org.mapdb.Serializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;


public class ListenerImpl implements ServletContextListener, MapDBConstants {
    private static final MagicCardDecorator[] MAGIC_DUMMY_DATA = new MagicCardDecorator[]{
            new MagicCardDecorator(new
                    CardImpl("magicName", "magicDesc", "magicType"),
                    "magicArtist", "rare",
                    true, true, true, true, true
            ),
            new MagicCardDecorator(new
                    CardImpl("magicName", "magicDesc", "magicType"),
                    "magicArtist", "rare",
                    true, true, true, true, true
            ),
            new MagicCardDecorator(new
                    CardImpl("magicName", "magicDesc", "magicType"),
                    "magicArtist", "rare",
                    true, true, true, true, true
            ),
    };
    private static final PokemonCardDecorator[] POKEMON_DUMMY_DATA = new PokemonCardDecorator[]{
            new PokemonCardDecorator(new
                    CardImpl("pokemonName", "pokemonDesc", "pokemonType"),
                    "pokemonArtist", "http://www.image.jpg", "rare",
                    true, true, true, true, true
            ),
            new PokemonCardDecorator(new
                    CardImpl("pokemonName", "pokemonDesc", "pokemonType"),
                    "pokemonArtist", "http://www.image.jpg", "rare",
                    true, true, true, true, true
            ),
            new PokemonCardDecorator(new
                    CardImpl("pokemonName", "pokemonDesc", "pokemonType"),
                    "pokemonArtist", "http://www.image.jpg", "rare",
                    true, true, true, true, true
            ),
    };
    private static final YuGiOhCardDecorator[] YUGIOH_DUMMY_DATA = new YuGiOhCardDecorator[]{
            new YuGiOhCardDecorator(new
                    CardImpl("yugiohName", "yugiohDesc", "yugiohType"),
                    "race", "http://www.image.jpg", "http://www.smallimage.jpg"
            ),
            new YuGiOhCardDecorator(new
                    CardImpl("yugiohName", "yugiohDesc", "yugiohType"),
                    "race", "http://www.image.jpg", "http://www.smallimage.jpg"
            ),
            new YuGiOhCardDecorator(new
                    CardImpl("yugiohName", "yugiohDesc", "yugiohType"),
                    "race", "http://www.image.jpg", "http://www.smallimage.jpg"
            ),
    };

    public void loadDummyData(int counter, Map<Integer, CardDecorator> map, CardDecorator[] DUMMY_DATA) {
        for (CardDecorator card : DUMMY_DATA) {
            map.put(counter++, card);
        }
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context initialized.");
        System.out.println("*** Loading data from file. ***");

        Gson gson = new Gson();
        GsonSerializer<CardDecorator> cardSerializer = new GsonSerializer<>(gson);
        MapDB DB = new MapDBImpl();
        int count = 0;

        Map<Integer, CardDecorator> yMap = DB.getCachedMap(sce.getServletContext(), YUGIOH_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> mMap = DB.getCachedMap(sce.getServletContext(), MAGIC_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> pMap = DB.getCachedMap(sce.getServletContext(), POKEMON_MAP_NAME,
                Serializer.INTEGER, cardSerializer);

        loadDummyData(count, mMap, MAGIC_DUMMY_DATA);
        loadDummyData(count, pMap, POKEMON_DUMMY_DATA);
        loadDummyData(count, yMap, YUGIOH_DUMMY_DATA);

        System.out.println("*** Data Loaded. ***");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}