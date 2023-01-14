package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.jsonparser.JSONParser;
import com.aadm.cardexchange.server.jsonparser.MagicCardParseStrategy;
import com.aadm.cardexchange.server.jsonparser.PokemonCardParseStrategy;
import com.aadm.cardexchange.server.jsonparser.YuGiOhCardParseStrategy;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.google.gson.Gson;
import org.mapdb.Serializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileNotFoundException;
import java.util.Map;


public class ListenerImpl implements ServletContextListener, MapDBConstants {
    /*
    private static final MagicCardDecorator[] MAGIC_DUMMY_DATA = new MagicCardDecorator[]{
            new MagicCardDecorator(new
                    CardImpl("magicName", "magicDesc", "magicType"),
                    "magicArtist", "rare",
                    true, true, true, true, true
            ),
            new MagicCardDecorator(new
                    CardImpl("magicName", "magicDesc", "magicType"),
                    "magicArtist", "epic",
                    true, true, true, true, true
            ),
            new MagicCardDecorator(new
                    CardImpl("magicName", "magicDesc", "magicType"),
                    "magicArtist", "legendary",
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
    */

    private void uploadDataToDB(int counter, Map<Integer, CardDecorator> map, CardDecorator[] data) {
        for (CardDecorator card : data) {
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
        int counter = 0;

        Map<Integer, CardDecorator> yuGiOhMap = DB.getCachedMap(sce.getServletContext(), YUGIOH_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> magicMap = DB.getCachedMap(sce.getServletContext(), MAGIC_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> pokemonMap = DB.getCachedMap(sce.getServletContext(), POKEMON_MAP_NAME,
                Serializer.INTEGER, cardSerializer);

        JSONParser parser = new JSONParser(new YuGiOhCardParseStrategy(), gson);
        try {
            uploadDataToDB(counter, yuGiOhMap, parser.parseJSON("./resources/json/yugioh_cards.json"));

            parser.setParseStrategy(new MagicCardParseStrategy());
            uploadDataToDB(counter, magicMap, parser.parseJSON("./resources/json/magic_cards.json"));

            parser.setParseStrategy(new PokemonCardParseStrategy());
            uploadDataToDB(counter, pokemonMap, parser.parseJSON("./resources/json/pokemon_cards.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("*** Data Loaded. ***");

        /*
        loadDummyData(counter, mMap, MAGIC_DUMMY_DATA);
        loadDummyData(counter, pMap, POKEMON_DUMMY_DATA);
        loadDummyData(counter, yMap, YUGIOH_DUMMY_DATA);

//        if (!new File(DB_FILENAME).exists()) {
//            try {
//                // List<String> stocks = new ArrayList<>();
//                // ObjectMapper objectMapper = new ObjectMapper();
//                // stocks = objectMapper.readValue(new File("stock.json"), new
//                // TypeReference<List<String>>() {
//                // });
//            } catch (Exception e) {
//                Logger.getGlobal().warning(e.toString());
//            }
//        }
        // Gson gson = new Gson();
        // Person p = gson.fromJson("{\"age\": 24, \"name\":\"Mario\"}", Person.class);
        // System.out.println("Data: " + p);

         */
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}