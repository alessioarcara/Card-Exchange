package com.aadm.cardexchange.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class CardsPlace extends Place {

    public CardsPlace() {
    }

    @Prefix("Cards")
    public static class Tokenizer implements PlaceTokenizer<CardsPlace> {

        @Override
        public String getToken(CardsPlace place) {
            return null;
        }

        @Override
        public CardsPlace getPlace(String token) {
            return new CardsPlace();
        }
    }
}
