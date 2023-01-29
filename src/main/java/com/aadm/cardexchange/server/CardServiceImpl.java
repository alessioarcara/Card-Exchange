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
    private static final long serialVersionUID = -45618221088536472L;
    private final MapDB db;
    private final GsonSerializer<CardDecorator> serializer = new GsonSerializer<>(new Gson());

    public CardServiceImpl() {
        db = new MapDBImpl();
    }

    public CardServiceImpl(MapDB db) {
        this.db = db;
    }

    private String getMapName(Game game) {
        return game == Game.Magic ? MAGIC_MAP_NAME :
                game == Game.Pokemon ? POKEMON_MAP_NAME :
                        YUGIOH_MAP_NAME;
    }

    @Override
    public List<CardDecorator> getGameCards(Game game) {
        if (game == null)
            throw new IllegalArgumentException("game cannot be null");
        Map<Integer, CardDecorator> map = db.getCachedMap(getServletContext(), getMapName(game),
                Serializer.INTEGER, serializer);
        return new ArrayList<>(map.values());
    }

    public String getNameCard(Game game, int idCard) {
        if (game == null)
            throw new IllegalArgumentException("game cannot be null");
        if (idCard <= 0)
            throw new IllegalArgumentException("Invalid cardId provided. cardId must be a positive integer greater than 0");
        Map<Integer, CardDecorator> map = db.getCachedMap(getServletContext(), getMapName(game),
                Serializer.INTEGER, serializer);
        try {
            return map.get(idCard).getName();
        } catch (Exception e) {
            return "No Name";
        }
    }

    @Override
    public CardDecorator getGameCard(Game game, int cardId) {
        if (game == null)
            throw new IllegalArgumentException("game cannot be null");
        if (cardId <= 0)
            throw new IllegalArgumentException("Invalid cardId provided. cardId must be a positive integer greater than 0");
        Map<Integer, CardDecorator> map = db.getCachedMap(getServletContext(), getMapName(game),
                Serializer.INTEGER, serializer);
        return map.get(cardId);
    }
}
