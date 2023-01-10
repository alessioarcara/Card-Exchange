package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.CardImpl;
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

    public CardImpl[] getCards() {
        Map<Integer, CardImpl> map = getDb().getMap(getServletContext(), CARDS_HASHMAP_NAME, Serializer.INTEGER, new SerializerCard());
        int n = map.size();
        CardImpl[] cards = new CardImpl[n];
        for (int i = 0; i < n; i++) {
            cards[i] = map.get(i);
        }
        return cards;
    }
}
