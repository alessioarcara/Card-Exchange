package com.aadm.cardexchange.client.routes;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.AuthPlace;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.places.DecksPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;

public class AppPlaceHistoryMapper implements PlaceHistoryMapper, RouteConstants {
    private static final String DELIMITER = "/";
    private final Place defaultPlace = new HomePlace();
    private final AuthSubject authSubject;

    public AppPlaceHistoryMapper(AuthSubject authSubject) {
        this.authSubject = authSubject;
    }

    @Override
    public Place getPlace(String token) {
        if (token.isEmpty()) {
            return defaultPlace;
        } else if (token.equals(authLink) && !authSubject.isLoggedIn()) {
            return new AuthPlace();
        } else if (token.equals(decksLink) && authSubject.isLoggedIn()) {
            return new DecksPlace();
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
            return homeLink;
        } else if (place instanceof AuthPlace) {
            return authLink;
        } else if (place instanceof DecksPlace) {
            return decksLink;
        } else if (place instanceof CardPlace) {
            CardPlace cardPlace = (CardPlace) place;
            return "cards" + DELIMITER + cardPlace.getGame().name().toLowerCase() + DELIMITER + cardPlace.getCardId();
        } else {
            return "";
        }
    }
}