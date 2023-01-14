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
    private static final String MAGIC_FILE_PATH = "./resources/json/magic_cards.json";
    private static final String POKEMON_FILE_PATH = "./resources/json/magic_cards.json";
    private static final String YUGIOH_FILE_PATH = "./resources/json/magic_cards.json";


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
        JSONParser parser = new JSONParser(new MagicCardParseStrategy(), gson);
        MapDB DB = new MapDBImpl();

        Map<Integer, CardDecorator> magicMap = DB.getCachedMap(sce.getServletContext(), MAGIC_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> pokemonMap = DB.getCachedMap(sce.getServletContext(), POKEMON_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> yuGiOhMap = DB.getCachedMap(sce.getServletContext(), YUGIOH_MAP_NAME,
                Serializer.INTEGER, cardSerializer);

        int count = 0;
        try {
            uploadDataToDB(count, magicMap, parser.parseJSON(MAGIC_FILE_PATH));
            parser.setParseStrategy(new PokemonCardParseStrategy());
            uploadDataToDB(count, yuGiOhMap, parser.parseJSON(POKEMON_FILE_PATH));
            parser.setParseStrategy(new YuGiOhCardParseStrategy());
            uploadDataToDB(count, pokemonMap, parser.parseJSON(YUGIOH_FILE_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("*** Data Loaded. ***");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}