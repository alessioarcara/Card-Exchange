package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.DeckService;
import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.DeckException;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.util.LinkedHashSet;
import java.util.Map;


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
    public boolean addDeck(String email, String deckName) throws DeckException {
        return addDeck(email, deckName, false);
    }

    public boolean addDeck(String email, String deckName, boolean isDefault) throws DeckException {
        Map<String, LinkedHashSet<Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        //deckMap.get(email).add(new Deck(email, deckName, isDefault));
        /*if (deckMap.containsKey(email)) {
            for (Deck item : deckMap.get(email)) {
                if (item.getName().equals(deckName)) {
                    throw new DeckException("Deck already exists");
                }
            }
        }
         */
        if (!deckMap.get(email).add(new Deck(email, deckName, isDefault))) {
            throw new DeckException("Deck already exists");
        }
        return true;
    }

    public boolean createDefaultDecks(String email) throws DeckException {
        Map<String, LinkedHashSet<Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        if (deckMap.putIfAbsent(email, new LinkedHashSet<>()) != null) {
            throw new DeckException("Deck already exists");
        }
        //return (addDeck(email, "Owned", true) && addDeck(email, "Wished", true));
        return (addDeck(email, "Owned", true));
    }

}
