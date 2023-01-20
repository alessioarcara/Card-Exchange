package com.aadm.cardexchange.client.routes;

import com.aadm.cardexchange.client.places.AuthPlace;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;

public class AppPlaceHistoryMapper implements PlaceHistoryMapper {
    private static final String DELIMITER = "/";
    private final Place defaultPlace = new HomePlace();

    @Override
    public Place getPlace(String token) {
        if (token.isEmpty()) {
            return defaultPlace;
        } else if (token.equals("auth")) {
            return new AuthPlace();
        } else {
            try {
                String[] parts = token.split(DELIMITER);
                Game game = Game.valueOf(parts[1]);
                int cardId = Integer.parseInt(parts[2]);
                return new CardPlace(game, cardId);
            } catch (Exception e) {
                return defaultPlace;
            }
        }
    }

    @Override
    public String getToken(Place place) {
        if (place instanceof HomePlace) {
            return "";
        } else if (place instanceof AuthPlace) {
            return "auth";
        } else if (place instanceof CardPlace) {
            CardPlace cardPlace = (CardPlace) place;
            return "cards" + DELIMITER + cardPlace.getGame().name().toLowerCase() + DELIMITER + cardPlace.getCardId();
        } else {
            return "";
        }
    }
}