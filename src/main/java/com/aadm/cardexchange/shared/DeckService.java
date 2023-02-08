package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("decks")
public interface DeckService extends RemoteService {

    boolean addDeck(String token, String deckName) throws AuthException;

    boolean removeCustomDeck(String token, String deckName) throws AuthException;

    boolean addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description) throws BaseException;

    List<Deck> removePhysicalCardFromDeck(String token, String deckName, PhysicalCard pCard) throws BaseException;

    List<String> getUserDeckNames(String token) throws AuthException;

    List<PhysicalCardWithName> getMyDeck(String token, String deckName) throws AuthException;

    List<PhysicalCardWithName> getUserOwnedDeck(String email) throws AuthException;

    List<PhysicalCardWithEmail> getOwnedPhysicalCardsByCardId(int cardId) throws InputException;

    List<PhysicalCardWithEmail> getWishedPhysicalCardsByCardId(int cardId) throws InputException;

    List<PhysicalCardWithName> addPhysicalCardsToCustomDeck(String token, String customDeckName, List<PhysicalCard> pCards) throws BaseException;
}
