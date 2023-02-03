package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Property;
import com.aadm.cardexchange.shared.models.PropertyType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JSONParser {
    private CardParseStrategy strategy;
    private final Gson gson;

    public JSONParser(CardParseStrategy strategy, Gson gson) {
        this.strategy = strategy;
        this.gson = gson;
    }

    public void setParseStrategy(CardParseStrategy strategy) {
        this.strategy = strategy;
    }

    public static List<Property> getPropertiesFromJson(JsonObject json, PropertyType propertyType, String... propertyNames) {
        List<Property> properties = new LinkedList<>();
        for (String propertyName : propertyNames) {
            properties.add(new Property(propertyName, propertyType, json.has(propertyName) ? json.get(propertyName).getAsString() : "unknown"));
        }
        return properties;
    }

    public static List<String> getVariantsFromJson(JsonObject json, String... variantNames) {
        List<String> variants = new ArrayList<>();
        if (json == null) {
            return variants;
        }
        for (String variantName : variantNames) {
            if (json.has(variantName)) {
                JsonElement variant = json.get(variantName);
                if (variant.getAsBoolean() || variant.getAsInt() != 0) {
                    variants.add(variantName);
                }
            }
        }
        return variants;
    }

    public Card[] parseJSON(String filePath) throws FileNotFoundException {
        JsonArray jsonArray = gson.fromJson(new FileReader(filePath), JsonArray.class);

        Card[] cards = new Card[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            cards[i] = strategy.execute(jsonArray.get(i).getAsJsonObject());
        }
        return cards;
    }
}
