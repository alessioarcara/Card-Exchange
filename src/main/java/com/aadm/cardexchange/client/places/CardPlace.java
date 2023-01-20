package com.aadm.cardexchange.client.places;

import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.place.shared.Place;

public class CardPlace extends Place {
    private final Game game;
    private final int cardId;

    public CardPlace(Game game, int cardId) {
        this.game = game;
        this.cardId = cardId;
    }

    public Game getGame() {
        return game;
    }

    public int getCardId() {
        return cardId;
    }
}
