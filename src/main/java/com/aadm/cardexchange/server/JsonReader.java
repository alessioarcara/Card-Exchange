package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.MalformedJsonException;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonReader {
    public JsonReader() {}

    public YuGiOhCardDecorator[] importYugiCards() throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader jsonFile = null;
        try {
            jsonFile =  new FileReader("./resources/json/yugioh_cards.json");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }

        JsonObject[] jsonArray = gson.fromJson(jsonFile, JsonObject[].class);

        YuGiOhCardDecorator[] yuGiOhCards = (YuGiOhCardDecorator[]) getCardsArrayWithStrategy(jsonArray, 'y');

        return yuGiOhCards;
    }

    public YuGiOhCardDecorator[] importYugiCards(String jsonString) throws MalformedJsonException {
        try {
            return (YuGiOhCardDecorator[]) getCardsArrayWithStrategy(jsonString, 'y');
        } catch (MalformedJsonException e) {
            throw new MalformedJsonException(e);
        }
    }

    public MagicCardDecorator[] importMagicCards() throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader jsonFile = null;
        try {
            jsonFile =  new FileReader("./resources/json/magic_cards.json");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }

        JsonObject[] jsonArray = gson.fromJson(jsonFile, JsonObject[].class);

        MagicCardDecorator[] magicCards = (MagicCardDecorator[]) getCardsArrayWithStrategy(jsonArray, 'm');

        return magicCards;
    }

    public MagicCardDecorator[] importMagicCards(String jsonString) throws MalformedJsonException {
        try {
            return (MagicCardDecorator[]) getCardsArrayWithStrategy(jsonString, 'm');
        } catch (MalformedJsonException e) {
            throw new MalformedJsonException(e);
        }
    }

    public PokemonCardDecorator[] importPokemonCards() throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader jsonFile = null;
        try {
            jsonFile =  new FileReader("./resources/json/pokemon_cards.json");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }

        JsonObject[] jsonArray = gson.fromJson(jsonFile, JsonObject[].class);

        PokemonCardDecorator[] pokemonCards = (PokemonCardDecorator[]) getCardsArrayWithStrategy(jsonArray, 'p');

        return pokemonCards;
    }

    public PokemonCardDecorator[] importPokemonCards(String jsonString) throws MalformedJsonException {
        try {
            return (PokemonCardDecorator[]) getCardsArrayWithStrategy(jsonString, 'p');
        } catch (MalformedJsonException e) {
            throw new MalformedJsonException(e);
        }
    }

    @NotNull
    private String[] parseJSONString(@NotNull String jsonString) throws MalformedJsonException {
        jsonString = jsonString.substring(1, jsonString.length() - 1);
        if ( jsonString.charAt(jsonString.length() - 1) == ',' ) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
        }

        if ( jsonString.charAt(0) != '{' ||
                jsonString.charAt(jsonString.length() - 1) != '}' ||
                !jsonString.contains("},{") ) {
            throw new MalformedJsonException("malformed JSON");
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
    @NotNull
    private CardDecorator[] getCardsArrayWithStrategy(@NotNull String jsonString, char type) throws MalformedJsonException {
        Gson gson = new Gson();

        String[] inputJSON;

        try {
            inputJSON = parseJSONString(jsonString);
        } catch (MalformedJsonException e) {
            throw new MalformedJsonException(e);
        }

        CardDecorator[] cards = new CardDecorator[inputJSON.length];

        // common fields between the 3 types of deck
        switch (type) {
            case 'y' :
            {
                cards = new YuGiOhCardDecorator[inputJSON.length];
                for (int i = 0; i < inputJSON.length; i++) {
                    JsonObject json = gson.fromJson(inputJSON[i], JsonObject.class);
                    cards[i] = yuGiOhCardParserStrategy(json);
                }
                break;
            }
            case 'm' :
            {
                cards = new MagicCardDecorator[inputJSON.length];
                for (int i = 0; i < inputJSON.length; i++) {
                    JsonObject json = gson.fromJson(inputJSON[i], JsonObject.class);
                    cards[i] = magicCardParserStrategy(json);
                }
                break;
            }
            case 'p' :
            {
                cards = new PokemonCardDecorator[inputJSON.length];
                for (int i = 0; i < inputJSON.length; i++) {
                    JsonObject json = gson.fromJson(inputJSON[i], JsonObject.class);
                    cards[i] = pokemonCardParserStrategy(json);
                }
                break;
            }
            default : return cards;
        }

        return cards;
    }

    @NotNull
    private CardDecorator[] getCardsArrayWithStrategy(@NotNull JsonObject[] jsons, char type) {
        CardDecorator[] cards = new CardDecorator[jsons.length];

        switch (type) {
            case 'y' :
            {
                cards = new YuGiOhCardDecorator[jsons.length];
                for (int i = 0; i < jsons.length; i++) {
                    cards[i] = yuGiOhCardParserStrategy(jsons[i]);
                }
                break;
            }
            case 'm' :
            {
                cards = new MagicCardDecorator[jsons.length];
                for (int i = 0; i < jsons.length; i++) {
                    cards[i] = magicCardParserStrategy(jsons[i]);
                }
                break;
            }
            case 'p' :
            {
                cards = new PokemonCardDecorator[jsons.length];
                for (int i = 0; i < jsons.length; i++) {
                    cards[i] = pokemonCardParserStrategy(jsons[i]);
                }
                break;
            }
            default : return cards;
        }

        return cards;
    }

    @NotNull
    private CardDecorator yuGiOhCardParserStrategy(@NotNull JsonObject json) {
        //fields
        String name = json.has("name") ? json.get("name").getAsString() : "";
        String description = json.has("desc") ? json.get("desc").getAsString() : "";
        String types = json.has("type") ? json.get("type").getAsString() : "";
        String race = json.has("race") ? json.get("race").getAsString() : "";
        String imageUrl = json.has("image_url") ? json.get("image_url").getAsString() : "";
        String smallImageUrl = json.has("small_image_url") ? json.get("small_image_url").getAsString() : "";

        return new YuGiOhCardDecorator(new CardImpl(name, description, types), race, imageUrl, smallImageUrl);
    }

    @NotNull
    private CardDecorator magicCardParserStrategy(@NotNull JsonObject json) {
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

        return new MagicCardDecorator(new CardImpl(name, description, types),
                                          artist, rarity,
                                          hasFoil, isAlternative, isFullArt, isPromo, isReprint);
    }

    @NotNull
    private CardDecorator pokemonCardParserStrategy(@NotNull JsonObject json) {
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

        if (json.has("variants")) {
            assert variants != null;
            isFirstEdition = variants.has("firstEdition") && variants.get("firstEdition").getAsBoolean();
            isHolo = variants.has("holo") && variants.get("holo").getAsBoolean();
            isNormal = variants.has("normal") && variants.get("normal").getAsBoolean();
            isReverse = variants.has("reverse") && variants.get("reverse").getAsBoolean();
            isPromo = variants.has("wPromo") && variants.get("wPromo").getAsBoolean();
        }

        return new PokemonCardDecorator(new CardImpl(name, description, types),
                                            illustrator, image, rarity,
                                            isFirstEdition, isHolo, isNormal, isReverse, isPromo);
    }
}
