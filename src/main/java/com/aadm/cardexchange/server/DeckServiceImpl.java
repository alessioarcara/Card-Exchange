package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.DeckService;
import com.aadm.cardexchange.shared.models.Deck;
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
        return addDeck(email, deckName, false);
    }

    public boolean addDeck(String email, String deckName, boolean isDefault) {
        Map<String, Set<Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Set<Deck> userDecks = deckMap.computeIfAbsent(email, k -> new LinkedHashSet<>());
        return userDecks.add(new Deck(email, deckName, isDefault));
    }

    public boolean createDefaultDecks(String email) {
        return addDeck(email, "Owned", true) && addDeck(email, "Wished", true);
    }
}
