package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.jsonparser.*;
import com.aadm.cardexchange.shared.models.*;
import com.google.gson.Gson;
import org.mapdb.Serializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileNotFoundException;
import java.util.Map;

public class ListenerImpl implements ServletContextListener, MapDBConstants {

    private void uploadDataToDB(int counter, Map<Integer, CardDecorator> map, CardDecorator[] cards) {
        for (CardDecorator card : cards) {
            map.put(counter++, card);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) throws RuntimeException {
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
        CardDecorator[] tmpCards;
        try {
            tmpCards = parser.parseJSON("./CardExchange-1.0-SNAPSHOT/WEB-INF/classes/json/yugioh_cards.json");
            uploadDataToDB(counter, yuGiOhMap, tmpCards);

            parser.setParseStrategy(new MagicCardParseStrategy());
            tmpCards = parser.parseJSON("./CardExchange-1.0-SNAPSHOT/WEB-INF/classes/json/magic_cards.json");
            uploadDataToDB(counter, magicMap, tmpCards);

            parser.setParseStrategy(new PokemonCardParseStrategy());
            tmpCards = parser.parseJSON("./CardExchange-1.0-SNAPSHOT/WEB-INF/classes/json/pokemon_cards.json");
            uploadDataToDB(counter, pokemonMap, tmpCards);

        } catch (FileNotFoundException e) {
            System.out.println("error");
            System.out.println(e.getMessage());
        }

        System.out.println("*** Data Loaded. ***");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}