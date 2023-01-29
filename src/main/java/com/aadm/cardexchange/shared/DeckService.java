package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.AuthException;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("decks")
public interface DeckService extends RemoteService {

    boolean addDeck(String token, String deckName) throws AuthException;

    boolean addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description) throws AuthException;

    List<String> getUserDecks(String token) throws AuthException;
}
