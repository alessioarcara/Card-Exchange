package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PhysicalCardDecorator;
import com.aadm.cardexchange.shared.models.PhysicalCardImpl;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface DeckServiceAsync {
    void addDeck(String email, String deckName, AsyncCallback<Boolean> callback);

    void addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description, AsyncCallback<Boolean> callback);

    void getUserDeckNames(String token, AsyncCallback<List<String>> async);

    void getDeckByName(String token, String deckName, AsyncCallback<List<PhysicalCardDecorator>> async);

    void removePhysicalCardFromDeck(String token, String deckName, PhysicalCardImpl pCardImpl, AsyncCallback<Boolean> async);
}
