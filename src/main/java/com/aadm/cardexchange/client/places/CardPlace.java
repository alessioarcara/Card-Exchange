package com.aadm.cardexchange.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class CardPlace extends Place {
    private final String id;

    public CardPlace(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Prefix("Card")
    public static class Tokenizer implements PlaceTokenizer<CardPlace> {

        @Override
        public String getToken(CardPlace place) {
            return place.getId();
        }

        @Override
        public CardPlace getPlace(String token) {
            return new CardPlace(token);
        }
    }
}
