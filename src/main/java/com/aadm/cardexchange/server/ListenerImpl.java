package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.jsonparser.JSONParser;
import com.aadm.cardexchange.server.jsonparser.MagicCardParseStrategy;
import com.aadm.cardexchange.server.jsonparser.PokemonCardParseStrategy;
import com.aadm.cardexchange.server.jsonparser.YuGiOhCardParseStrategy;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.server.mapdb.MapDBImpl;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.google.gson.Gson;
import org.mapdb.Serializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileNotFoundException;
import java.util.Map;

public class ListenerImpl implements ServletContextListener, MapDBConstants {
    private final MapDB db;
    private final String path;

    public ListenerImpl() {
        db = new MapDBImpl();
        path = "CardExchange-1.0-SNAPSHOT/WEB-INF/classes/json/";
    }

    public ListenerImpl(MapDB db, String path) {
        this.db = db;
        this.path = path;
    }

    private void uploadDataToDB(Map<Integer, CardDecorator> map, CardDecorator[] cards) {
        for (CardDecorator card : cards) {
            map.put(card.getId(), card);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) throws RuntimeException {
        System.out.println("Context initialized.");
        System.out.println("*** Loading data from file. ***");

        Gson gson = new Gson();
        GsonSerializer<CardDecorator> cardSerializer = new GsonSerializer<>(gson);

        Map<Integer, CardDecorator> yuGiOhMap = db.getCachedMap(sce.getServletContext(), YUGIOH_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> magicMap = db.getCachedMap(sce.getServletContext(), MAGIC_MAP_NAME,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, CardDecorator> pokemonMap = db.getCachedMap(sce.getServletContext(), POKEMON_MAP_NAME,
                Serializer.INTEGER, cardSerializer);

        JSONParser parser = new JSONParser(new YuGiOhCardParseStrategy(), gson);
        try {
            uploadDataToDB(yuGiOhMap, parser.parseJSON(path + "yugioh_cards.json"));
            parser.setParseStrategy(new MagicCardParseStrategy());
            uploadDataToDB(magicMap, parser.parseJSON(path + "magic_cards.json"));
            parser.setParseStrategy(new PokemonCardParseStrategy());
            uploadDataToDB(pokemonMap, parser.parseJSON(path + "pokemon_cards.json"));
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