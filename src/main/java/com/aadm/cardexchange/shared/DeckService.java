package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PhysicalCardDecorator;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("decks")
public interface DeckService extends RemoteService {

    boolean addDeck(String token, String deckName) throws AuthException;

    boolean addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description) throws BaseException;

    boolean removePhysicalCardFromDeck(String token, String deckName, PhysicalCardImpl pCardImpl) throws AuthException;

    List<String> getUserDeckNames(String token) throws AuthException;

    List<PhysicalCardDecorator> getMyDeck(String token, String deckName) throws AuthException;

    List<PhysicalCardDecorator> getUserOwnedDeck(String email) throws AuthException;
}
