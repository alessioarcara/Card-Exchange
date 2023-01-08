package com.aadm.cardexchange.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class HomePlace extends Place {

    public HomePlace() {
    }

    @Prefix("Home")
    public static class Tokenizer implements PlaceTokenizer<HomePlace> {

        @Override
        public String getToken(HomePlace place) {
            return null;
        }

        @Override
        public HomePlace getPlace(String token) {
            return new HomePlace();
        }
    }
}
