package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

public class ListenerImpl implements ServletContextListener, MapDBConstants {
    private static final CardImpl[] DUMMY_DATA = new CardImpl[]{
            new CardImpl("Charizard", "Un pokemon nervoso", "Fuoco"),
            new CardImpl("Blastoise", "Un pokemon arrogante", "Acqua"),
            new CardImpl("Venusaur", "Un pokemon solitario", "Erba")
    };

    public String[]parseJSONString(String jsonString) {
        jsonString = jsonString.substring(1, jsonString.length() - 1);
        if ( jsonString.charAt(jsonString.length() - 1) == ',' ) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
        }

        String[] parsedJSON = jsonString.split("\\},\\{");
        for (int i = 0; i < parsedJSON.length; i++) {
            if (parsedJSON[i].charAt(0) != '{') {
                parsedJSON[i] = "{" + parsedJSON[i];
            }
            if (parsedJSON[i].charAt(parsedJSON[i].length() - 1) != '}') {
                parsedJSON[i] += "}";
            }
        }
        return parsedJSON;
    }
    /*
     * this method serves the scope of importing information about the cards of the three games from JSON files
     * the inputs taken are :
     *  - jsonString : the string that represents the array of json objects to be imported
     *  - type       : the type of deck we want to import
     *                 - y : yugi-oh
     *                 - m : magic
     *                 - p : pokemon
     */
     public CardDecorator[] importJSON(String jsonString, char type) {
        Gson gson = new Gson();

        String[] inputJSON = parseJSONString(jsonString);

        CardDecorator[] cards = new CardDecorator[inputJSON.length];

         // common fields between the 3 types of deck
        switch (type) {
            case 'y' :
            {
                cards = new YuGiOhCardDecorator[inputJSON.length];
                for (int i = 0; i < inputJSON.length; i++) {
                    JsonObject json = gson.fromJson(inputJSON[i], JsonObject.class);

                    // fields
                    String name = json.has("name") ? json.get("name").getAsString() : "";
                    String description = json.has("desc") ? json.get("desc").getAsString() : "";
                    String types = json.has("type") ? json.get("type").getAsString() : "";
                    String race = json.has("race") ? json.get("race").getAsString() : "";
                    String imageUrl = json.has("image_url") ? json.get("image_url").getAsString() : "";
                    String smallImageUrl = json.has("small_image_url") ? json.get("small_image_url").getAsString() : "";

                    cards[i] = new YuGiOhCardDecorator(new CardImpl(name, description, types), race, imageUrl, smallImageUrl);
                }
                break;
            }
            case 'm' :
            {
                cards = new MagicCardDecorator[inputJSON.length];
                for (int i = 0; i < inputJSON.length; i++) {
                    JsonObject json = gson.fromJson(inputJSON[i], JsonObject.class);

                    // fields
                    String name = json.has("name") ? json.get("name").getAsString() : "";
                    String description = json.has("text") ? json.get("text").getAsString() : "";
                    String types = json.has("types") ? json.get("types").getAsString() : "";
                    String artist = json.has("artist") ? json.get("artist").getAsString() : "";
                    String rarity = json.has("rarity") ? json.get("rarity").getAsString() : "";
                    boolean hasFoil = (json.has("hasFoil")) && (json.get("hasFoil").getAsInt() != 0);
                    boolean isAlternative = (json.has("isAlternative")) && (json.get("isAlternative").getAsInt() != 0);
                    boolean isFullArt = (json.has("isFullArt")) && (json.get("isFullArt").getAsInt() != 0);
                    boolean isPromo = (json.has("isPromo")) && (json.get("isPromo").getAsInt() != 0);
                    boolean isReprint = (json.has("isReprint")) && (json.get("isReprint").getAsInt() != 0);

                    cards[i] = new MagicCardDecorator(new CardImpl(name, description, types),
                            artist, rarity,
                            hasFoil, isAlternative, isFullArt, isPromo, isReprint);
                }
                break;
            }
            case 'p' :
            {
                cards = new PokemonCardDecorator[inputJSON.length];
                for (int i = 0; i < inputJSON.length; i++) {
                    JsonObject json = gson.fromJson(inputJSON[i], JsonObject.class);

                    // fields
                    String name = json.has("name") ? json.get("name").getAsString() : "";
                    String description = json.has("description") ? json.get("description").getAsString() : "";
                    String types = json.has("types") ? json.get("types").getAsString() : "";
                    String illustrator = json.has("illustrator") ? json.get("illustrator").getAsString() : "";
                    String image = json.has("image") ? json.get("image").getAsString() : "";
                    String rarity = json.has("rarity") ? json.get("rarity").getAsString() : "";

                    JsonObject variants = json.has("variants") ? json.getAsJsonObject("variants") : null;
                    boolean isFirstEdition = false;
                    boolean isHolo = false;
                    boolean isNormal = false;
                    boolean isReverse = false;
                    boolean isPromo = false;

                    if (json.has("variants")){
                        isFirstEdition = variants.has("firstEdition") && variants.get("firstEdition").getAsBoolean();
                        isHolo = variants.has("holo") && variants.get("holo").getAsBoolean();
                        isNormal = variants.has("normal") && variants.get("normal").getAsBoolean();
                        isReverse = variants.has("reverse") && variants.get("reverse").getAsBoolean();
                        isPromo = variants.has("wPromo") && variants.get("wPromo").getAsBoolean();
                    }


                    cards[i] = new PokemonCardDecorator(new CardImpl(name, description, types),
                            illustrator, image, rarity,
                            isFirstEdition, isHolo, isNormal, isReverse, isPromo);
                }
                break;
            }
            default : return cards;
        }

         return cards;
    }

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