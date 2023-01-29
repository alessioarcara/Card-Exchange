package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.DeckService;
import com.aadm.cardexchange.shared.models.*;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.util.*;


public class DeckServiceImpl extends RemoteServiceServlet implements DeckService, MapDBConstants {
    private static final long serialVersionUID = 5868007467963819042L;
    private final MapDB db;
    private final Gson gson = new Gson();

    public DeckServiceImpl() {
        db = new MapDBImpl();
    }

    public DeckServiceImpl(MapDB mockDB) {
        db = mockDB;
    }

    @Override
    public boolean addDeck(String token, String deckName) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        return addDeck(email, deckName, false);
    }

    private boolean addDeck(String email, String deckName, boolean isDefault) {
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Map<String, Deck> userDecks = deckMap.computeIfAbsent(email, k -> new HashMap<>());
        // if deck already exists in decks container do nothing
        if (userDecks.get(deckName) != null) {
            return false;
        }
        userDecks.put(deckName, new Deck(email, deckName, isDefault));
        return true;
    }

    public boolean createDefaultDecks(String email) {
        return addDeck(email, "Owned", true) && addDeck(email, "Wished", true);
    }

    private boolean checkDescriptionValidity(String description) {
        int count = 0;
        for (int i = 0; i < description.length(); i++) {
            if (description.charAt(i) != ' ') {
                count++;
            }
            if (count >= 20) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description) throws AuthException {
        /* PARAMETERS CHECK */
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        if (game == null) {
            throw new IllegalArgumentException("Invalid game");
        }
        if (deckName == null || deckName.isEmpty()) {
            throw new IllegalArgumentException("Invalid deck name");
        }
        if (cardId <= 0) {
            throw new IllegalArgumentException("Invalid card id");
        }
        if (status == null) {
            throw new IllegalArgumentException("Invalid status");
        }
        if (description == null || !checkDescriptionValidity(description)) {
            throw new IllegalArgumentException("Invalid description");
        }
        /* PHYSICAL CARD ADDITION TO DECK*/
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Map<String, Deck> decks = deckMap.get(userEmail);
        if (decks == null) {
            throw new RuntimeException("Not existing decks");
        }
        // selection of deck with deckName
        Deck foundDeck = decks.get(deckName);
        if (foundDeck == null) {
            return false;
        }
        // physical card addition
        return foundDeck.addPhysicalCard(new PhysicalCardImpl(game, cardId, status, description));
    }

    public List<PhysicalCardDecorator> getDecks (String token, String deckName) throws AuthException {
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Map<String, Deck> allUserDecks = deckMap.get(userEmail);
        Deck thisUserDeck = allUserDecks.get(deckName);
        List<PhysicalCardDecorator> pDecoratedCards;
        for ( PhysicalCard physicalCard : thisUserDeck.getPhysicalCards()) {
            CardDecorator.getStaticName(physicalCard.getCardId());
            CardDecorator()

        }


        title = metodoStaticoPerPrendereIlTitoloDallaCartaVera(physicalCard.getCardId())
        pDecoratedCards.add(physicalCard, title)

        return pDecoratedCards;

    }




}
