package com.aadm.cardexchange.client.routes;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.*;
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
        } else if (token.equals(exchangesLink) && authSubject.isLoggedIn()) {
            return new ExchangesPlace();
        } else {
            try {
                String[] parts = token.split(DELIMITER);
                if (parts[0].equals("cards")) {
                    Game game = Game.valueOf(parts[1].toUpperCase());
                    int cardId = Integer.parseInt(parts[2]);
                    return new CardPlace(game, cardId);
                } else if (parts[0].equals("new-exchange") && authSubject.isLoggedIn()) {
                    String receiverUserEmail = parts[1];
                    String selectedCardId = parts[2];
                    return new NewExchangePlace(selectedCardId, receiverUserEmail);
                }
            } catch (Exception e) {
                return defaultPlace;
            }
        }
        return defaultPlace;
    }

    @Override
    public String getToken(Place place) {
        if (place instanceof HomePlace) {
            return homeLink;
        } else if (place instanceof AuthPlace) {
            return authLink;
        } else if (place instanceof DecksPlace) {
            return decksLink;
        } else if (place instanceof ExchangesPlace) {
            return exchangesLink;
        } else if (place instanceof NewExchangePlace) {
            NewExchangePlace newExchangePlace = (NewExchangePlace) place;
            return "new-exchange" + DELIMITER + newExchangePlace.getReceiverUserEmail() + DELIMITER + newExchangePlace.getSelectedCardId();
        } else if (place instanceof CardPlace) {
            CardPlace cardPlace = (CardPlace) place;
            return "cards" + DELIMITER + cardPlace.getGame().name().toLowerCase() + DELIMITER + cardPlace.getCardId();
        } else {
            return "";
        }
    }
}