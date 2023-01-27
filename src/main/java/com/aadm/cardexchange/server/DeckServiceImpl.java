package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.DeckService;
import com.aadm.cardexchange.shared.models.AuthException;
import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


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
    public boolean addDeck(String email, String deckName) {
        return addDeck(email,deckName, false);
    }

    public boolean addDeck(String email, String deckName, boolean isDefault) {
        Map<String, Set<Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Set<Deck> userDecks = deckMap.computeIfAbsent(email, k -> new LinkedHashSet<>());
        return userDecks.add(new Deck(email, deckName, isDefault));
    }

    public boolean createDefaultDecks(String email) {
        return addDeck(email, "Owned", true) && addDeck(email, "Wished", true);
    }

    private boolean checkDescriptionValidity(String description) {
        int count = 0;
        for(int i=0; i<description.length(); i++) {
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
    public boolean addPhysicalCardToDeck(String token, String deckName, int cardId, Status status, String description) throws AuthException {
        /* PARAMETERS CHECK */
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
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
        Map<String, Set<Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Set<Deck> decks = deckMap.get(userEmail);
        if (decks == null) {
            throw new RuntimeException("Not existing decks");
        }
//        // TO DO
//        if (decks instanceof HashSet<Deck>) {
//
//        }
//        Deck currentDeck = decks.get(deckName);
//        // TO REFACTOR
//        return currentDeck.addPhysicalCard(new PhysicalCard(cardId, status, description));
        return false;
    }
}
