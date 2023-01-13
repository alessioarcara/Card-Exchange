package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.CardService;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<CardDecorator> getGameCards(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }

        MapDB DB = getDb();
        final String mapName = game == Game.Magic ? MAGIC_MAP_NAME :
                game == Game.Pokemon ? POKEMON_MAP_NAME :
                        YUGIOH_MAP_NAME;

        Gson gson = new Gson();
        Map<Integer, CardDecorator> map = DB.getCachedMap(getServletContext(), mapName,
                Serializer.INTEGER, new GsonSerializer<>(gson, CardDecorator.class));

        return new ArrayList<>(map.values());
    }
}
