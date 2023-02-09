package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.*;
import com.aadm.cardexchange.shared.payloads.ModifiedDeckPayload;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface DeckServiceAsync {
    void addDeck(String token, String deckName, AsyncCallback<Boolean> callback);

    void removeCustomDeck(String token, String deckName, AsyncCallback<Boolean> callback);

    void addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description, AsyncCallback<Boolean> callback);

    void removePhysicalCardFromDeck(String token, String deckName, PhysicalCard pCard, AsyncCallback<Boolean> callback);

    void editPhysicalCard(String token, String deckName, PhysicalCard pCard, AsyncCallback<List<ModifiedDeckPayload>> callback);

    void getUserDeckNames(String token, AsyncCallback<List<String>> callback);

    void getMyDeck(String token, String deckName, AsyncCallback<List<PhysicalCardWithName>> callback);

    void getUserOwnedDeck(String email, AsyncCallback<List<PhysicalCardWithName>> callback);

    void getOwnedPhysicalCardsByCardId(int cardId, AsyncCallback<List<PhysicalCardWithEmail>> callback);

    void getWishedPhysicalCardsByCardId(int cardId, AsyncCallback<List<PhysicalCardWithEmail>> callback);

    void addPhysicalCardsToCustomDeck(String token, String customDeckName, List<PhysicalCard> pCards, AsyncCallback<List<PhysicalCardWithName>> callback);
}
