package com.aadm.cardexchange.server.services;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.server.mapdb.MapDBImpl;
import com.aadm.cardexchange.shared.ExchangeService;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.lang.reflect.Type;
import java.util.*;

public class ExchangeServiceImpl extends RemoteServiceServlet implements ExchangeService, MapDBConstants {
    private static final long serialVersionUID = 5868088467963819042L;
    private final MapDB db;
    private final Gson gson = new Gson();

    public ExchangeServiceImpl() {
        db = new MapDBImpl();
    }

    public ExchangeServiceImpl(MapDB mockDB) {
        db = mockDB;
    }
    private boolean checkEmailExistence(String email) {
        Map<String, User> userMap = db.getPersistentMap(
                getServletContext(), USER_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        return userMap.get(email) != null;
    }

    private boolean checkPhysicalCardsConsistency(List<PhysicalCard> physicalCards) {
       return physicalCards != null && !physicalCards.isEmpty();
    }


    @Override
    public boolean CheckExistingPcardByIdCard(String token, Game game, int cardId) throws BaseException {
        String userEmail = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        if (game == null) {
            throw new InputException("Invalid game");
        }
        if (cardId <= 0) {
            throw new InputException("Invalid card");
        }
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> allUserDecks = deckMap.get(userEmail);
        Deck userDeck = allUserDecks.get("Owned");
        if (userDeck == null) {
            throw new BaseException("Deck not found");
        }
        for (PhysicalCard pCard : userDeck.getPhysicalCards()) {
            System.out.println(pCard.getCardId());
            if (cardId== pCard.getCardId()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards) throws InputException, AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        if (receiverUserEmail == null || receiverUserEmail.isEmpty() || !checkEmailExistence(receiverUserEmail)) {
            throw new InputException("Invalid receiver email");
        }
        if (!checkPhysicalCardsConsistency(senderPhysicalCards)) {
            throw new InputException("Invalid sender physical cards");
        }
        if (!checkPhysicalCardsConsistency(receiverPhysicalCards)) {
            throw new InputException("Invalid receiver physical cards");
        }
        Proposal newProposal = new Proposal(email, receiverUserEmail, senderPhysicalCards, receiverPhysicalCards);
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        return proposalMap.putIfAbsent(newProposal.getId(), newProposal) == null;
    }
}

