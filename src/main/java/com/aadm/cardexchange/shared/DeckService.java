package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("decks")
public interface DeckService extends RemoteService {

    boolean addDeck(String token, String deckName) throws AuthException;

    boolean addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description) throws AuthException;

    boolean removePhysicalCardFromDeck(String token, String deckName, PhysicalCardImpl pCardImpl) throws AuthException;

    List<String> getUserDeckNames(String token) throws AuthException;

    List<PhysicalCardDecorator> getDeckByName(String token, String deckName) throws AuthException;
}
