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

    void getUserDeckNames(String token, AsyncCallback<List<String>> callback);

    void getMyDeck(String token, String deckName, AsyncCallback<List<PhysicalCardDecorator>> callback);

    void getUserOwnedDeck(String email, AsyncCallback<List<PhysicalCardDecorator>> callback);

    void getOwnedPhysicalCardsByCardId(int cardId, AsyncCallback<List<PhysicalCardImpl>> async);
}
