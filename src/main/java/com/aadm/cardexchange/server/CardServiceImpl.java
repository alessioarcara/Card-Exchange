package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.Card;
import com.aadm.cardexchange.shared.CardService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.util.Map;

public class CardServiceImpl extends RemoteServiceServlet implements CardService, MapDBConstants {
    private MapDB db;

    public CardServiceImpl() {
    }

    public CardServiceImpl(MapDB db) {
        this.db = db;
    }

    public MapDB getDb() {
        if (db == null) {
            db = new MapDBImpl();
        }
        return db;
    }

    public Card[] getCards() {
        Map<Integer, Card> map = getDb().getMap(getServletContext(), CARDS_HASHMAP_NAME, Serializer.INTEGER,
                new SerializerCard());
        int n = map.size();
        Card[] cards = new Card[n];
        for (int i = 0; i < n; i++) {
            cards[i] = map.get(i);
        }
        return cards;
    }
}
