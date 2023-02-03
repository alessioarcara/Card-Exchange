package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.models.Card;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.FileNotFoundException;
import java.io.FileReader;

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

    public Card[] parseJSON(String filePath) throws FileNotFoundException {
        JsonArray jsonArray = gson.fromJson(new FileReader(filePath), JsonArray.class);

        Card[] cards = new Card[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            cards[i] = strategy.execute(jsonArray.get(i).getAsJsonObject());
        }
        return cards;
    }
}
