package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface DeckServiceAsync {
    void addDeck(String email, String deckName, AsyncCallback<Boolean> callback);

    void addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description, AsyncCallback<Boolean> callback);

    void getUserDecks(String token, AsyncCallback<List<String>> async);
}
