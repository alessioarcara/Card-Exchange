package com.aadm.cardexchange.server.services;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.server.mapdb.MapDBImpl;
import com.aadm.cardexchange.shared.ExchangeService;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.aadm.cardexchange.shared.models.Proposal;
import com.aadm.cardexchange.shared.models.User;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public boolean addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards) throws AuthException, InputException {
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

    @Override
    public Map<String, List<PhysicalCardWithName>> getProposalCards(String token, int proposalId) throws AuthException, InputException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));

        if (proposalId < 0) {
            throw new InputException("Invalid proposal Id");
        }

        Map<Integer, Proposal> proposalMap = new HashMap<>(db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson)));
        if (proposalMap.size() == 0) {
            throw new RuntimeException("Not existing proposal");
        }

        Proposal proposal = proposalMap.get(proposalId);

        List<PhysicalCardWithName> senderPCardsWithName = new ArrayList<>();
        for (PhysicalCard pCard : proposal.getSenderPhysicalCards()) {
            String cardName = CardServiceImpl.getNameCard(
                    pCard.getCardId(),
                    db.getCachedMap(
                            getServletContext(),
                            CardServiceImpl.getCardMap(pCard.getGameType()),
                            Serializer.INTEGER,
                            new GsonSerializer<>(gson)
                    )
            );
            senderPCardsWithName.add(new PhysicalCardWithName(pCard, cardName));
        }

        List<PhysicalCardWithName> receiverPCardsWithName = new ArrayList<>();
        for (PhysicalCard pCard : proposal.getReceiverPhysicalCards()) {
            String cardName = CardServiceImpl.getNameCard(
                    pCard.getCardId(),
                    db.getCachedMap(
                            getServletContext(),
                            CardServiceImpl.getCardMap(pCard.getGameType()),
                            Serializer.INTEGER,
                            new GsonSerializer<>(gson)
                    )
            );
            receiverPCardsWithName.add(new PhysicalCardWithName(pCard, cardName));
        }

        return new HashMap<>() {{
            put("Sender", senderPCardsWithName);
            put("Receiver", receiverPCardsWithName);
        }};
    }
}

