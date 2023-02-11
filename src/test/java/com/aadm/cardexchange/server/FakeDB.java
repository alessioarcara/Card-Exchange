package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.Proposal;
import com.aadm.cardexchange.shared.models.User;
import org.mapdb.Serializer;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

class FakeDB implements MapDB, MapDBConstants {
    Map<String, User> userMap = new HashMap<>() {{
        put("test@test.it", new User("test@test.it", BCrypt.hashpw("password", BCrypt.gensalt())));
        put("test2@test.it", new User("test2@test.it", BCrypt.hashpw("password", BCrypt.gensalt())));
    }};

    Map<String, LoginInfo> loginMap = new HashMap<>() {
        {
            put("validToken", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
        }
    };

    Map<Integer, Proposal> proposalMap;
    Map<String, Map<String, Deck>> deckMap;

    public FakeDB(Map<Integer, Proposal> proposalMap, Map<String, Map<String, Deck>> deckMap) {
        this.proposalMap = proposalMap;
        this.deckMap = deckMap;
    }

    public Map<Integer, Proposal> getProposalMap() {
        return proposalMap;
    }

    public Map<String, Map<String, Deck>> getDeckMap() {
        return deckMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        switch (mapName) {
            case MAGIC_MAP_NAME:
                return (Map<K, V>) DummyData.createMagicDummyMap();
            case POKEMON_MAP_NAME:
                return (Map<K, V>) DummyData.createPokemonDummyMap();
            case YUGIOH_MAP_NAME:
                return (Map<K, V>) DummyData.createYuGiOhDummyMap();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <K, V> Map<K, V> getMapName(String mapName) {
        switch (mapName) {
            case USER_MAP_NAME:
                return (Map<K, V>) userMap;
            case LOGIN_MAP_NAME:
                return (Map<K, V>) loginMap;
            case DECK_MAP_NAME:
                return (Map<K, V>) deckMap;
            case PROPOSAL_MAP_NAME:
                return (Map<K, V>) proposalMap;
        }
        return null;
    }

    @Override
    public <K, V> Map<K, V> getPersistentMap(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        return getMapName(mapName);
    }

    @Override
    public <K, V, T> T writeOperation(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer, Function<Map<K, V>, T> operation) {
        return operation.apply(getMapName(mapName));
    }

    @Override
    public boolean writeOperation(ServletContext ctx, Runnable runnable) {
        runnable.run();
        return true;
    }
}