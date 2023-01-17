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
    private final static String PATH_TO_JSON = "CardExchange-1.0-SNAPSHOT/WEB-INF/classes/json/";

    private void uploadDataToDB(int count, Map<Integer, CardDecorator> map, CardDecorator[] cards) {
        for (CardDecorator card : cards) {
            map.put(count++, card);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) throws RuntimeException {
        System.out.println("Context initialized.");
        System.out.println("*** Loading data from file. ***");

        Gson gson = new Gson();
        GsonSerializer<CardDecorator> cardSerializer = new GsonSerializer<>(gson);
        MapDB DB = new MapDBImpl();
        int count = 0;

        Map<Integer, CardDecorator> yuGiOhMap = DB.getCachedMap(sce.getServletContext(), YUGIOH_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> magicMap = DB.getCachedMap(sce.getServletContext(), MAGIC_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> pokemonMap = DB.getCachedMap(sce.getServletContext(), POKEMON_MAP_NAME,
                Serializer.INTEGER, cardSerializer);

        JSONParser parser = new JSONParser(new YuGiOhCardParseStrategy(), gson);
        try {
            uploadDataToDB(count, yuGiOhMap, parser.parseJSON(PATH_TO_JSON + "yugioh_cards.json"));
            parser.setParseStrategy(new MagicCardParseStrategy());
            uploadDataToDB(count, magicMap, parser.parseJSON(PATH_TO_JSON + "magic_cards.json"));
            parser.setParseStrategy(new PokemonCardParseStrategy());
            uploadDataToDB(count, pokemonMap, parser.parseJSON(PATH_TO_JSON + "pokemon_cards.json"));
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
        System.out.println("*** Data Loaded. ***");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}