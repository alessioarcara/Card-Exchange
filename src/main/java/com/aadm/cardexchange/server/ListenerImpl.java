package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.CardImpl;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

// import com.google.gson.Gson;

public class ListenerImpl implements ServletContextListener, MapDBConstants {
    private static final CardImpl[] DUMMY_DATA = new CardImpl[]{
            new CardImpl("Charizard", "Un pokemon nervoso", "Fuoco"),
            new CardImpl("Blastoise", "Un pokemon arrogante", "Acqua"),
            new CardImpl("Venusaur", "Un pokemon solitario", "Erba")
    };

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context initialized.");
        System.out.println("Loading data from file");
        if (!new File(DB_FILENAME).exists()) {
            try {
                // List<String> stocks = new ArrayList<>();
                // ObjectMapper objectMapper = new ObjectMapper();
                // stocks = objectMapper.readValue(new File("stock.json"), new
                // TypeReference<List<String>>() {
                // });
                Serializer<CardImpl> customSerializer = new SerializerCard();
                DB db = DBMaker.fileDB(new File(DB_FILENAME)).make();
                Map<Integer, CardImpl> map = db
                        .hashMap(CARDS_HASHMAP_NAME, Serializer.INTEGER, customSerializer)
                        .createOrOpen();
                for (int i = 0; i < DUMMY_DATA.length; i++) {
                    map.put(i, DUMMY_DATA[i]);
                }
                db.close();
            } catch (Exception e) {
                Logger.getGlobal().warning(e.toString());
            }
        }
        // Gson gson = new Gson();
        // Person p = gson.fromJson("{\"age\": 24, \"name\":\"Mario\"}", Person.class);
        // System.out.println("Data: " + p);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}