package com.aadm.cardexchange.server.services;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.server.mapdb.MapDBImpl;
import com.aadm.cardexchange.shared.ExchangeService;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.exceptions.ProposalNotFoundException;
import com.aadm.cardexchange.shared.models.*;
import com.aadm.cardexchange.shared.payloads.ProposalPayload;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ExchangeServiceImpl extends RemoteServiceServlet implements ExchangeService, MapDBConstants {
    private static final long serialVersionUID = 5868088467963819042L;
    private final MapDB db;
    private final Gson gson = new Gson();

    private final Type type = new TypeToken<Map<String, Deck>>() {
    }.getType();

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
        if (receiverUserEmail == null || receiverUserEmail.isEmpty() || !checkEmailExistence(receiverUserEmail))
            throw new InputException("Invalid receiver email");
        if (!checkPhysicalCardsConsistency(senderPhysicalCards))
            throw new InputException("Invalid sender physical cards");
        if (!checkPhysicalCardsConsistency(receiverPhysicalCards))
            throw new InputException("Invalid receiver physical cards");

        Proposal newProposal = new Proposal(email, receiverUserEmail, senderPhysicalCards, receiverPhysicalCards);
        return db.writeOperation(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson),
                (Map<Integer, Proposal> proposalMap) -> proposalMap.putIfAbsent(newProposal.getId(), newProposal) == null);
    }

    private List<PhysicalCardWithName> joinPhysicalCardsWithCatalogCards(List<PhysicalCard> pCards) {
        List<PhysicalCardWithName> pCardsWithName = new LinkedList<>();
        for (PhysicalCard pCard : pCards) {
            pCardsWithName.add(new PhysicalCardWithName(pCard, CardServiceImpl.getNameCard(
                    pCard.getCardId(),
                    db.getCachedMap(
                            getServletContext(),
                            CardServiceImpl.getCardMap(pCard.getGameType()),
                            Serializer.INTEGER,
                            new GsonSerializer<>(gson)
                    )
            )));
        }
        return pCardsWithName;
    }

    @Override
    public ProposalPayload getProposal(String token, int proposalId) throws AuthException, InputException, ProposalNotFoundException {
        String email = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        if (proposalId < 0)
            throw new InputException("Invalid proposal Id");
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        Proposal proposal = proposalMap.get(proposalId);
        if (proposal == null)
            throw new ProposalNotFoundException("Not existing proposal");
        if (!email.equals(proposal.getSenderUserEmail()) && !email.equals(proposal.getReceiverUserEmail()))
            throw new AuthException("You can only view proposals linked to your account as sender or receiver");

        return new ProposalPayload(proposal.getReceiverUserEmail(),
                joinPhysicalCardsWithCatalogCards(proposal.getSenderPhysicalCards()),
                joinPhysicalCardsWithCatalogCards(proposal.getReceiverPhysicalCards())
        );
    }

    @Override
    public boolean acceptProposal(String token, int proposalId) throws AuthException, ProposalNotFoundException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        Proposal proposal = proposalMap.get(proposalId);
        if (proposal == null)
            throw new ProposalNotFoundException("Not existing proposal");
        if (!email.equals(proposal.getReceiverUserEmail()))
            throw new AuthException("You can only accept proposals made to you.");

        return db.writeOperation(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type),
                (Map<String, Map<String, Deck>> deckMap) -> {
                    Map<String, Deck> receiverDecks = deckMap.get(proposal.getReceiverUserEmail());
                    Map<String, Deck> senderDecks = deckMap.get(proposal.getSenderUserEmail());
                    Deck senderOwnedDeck = senderDecks.get("Owned");
                    Deck receiverOwnedDeck = receiverDecks.get("Owned");
                    for (PhysicalCard pCard : proposal.getSenderPhysicalCards())
                        if (!senderOwnedDeck.removePhysicalCard(pCard) || !receiverOwnedDeck.addPhysicalCard(pCard))
                            throw new RuntimeException("DB ROLLBACK!");
                    for (PhysicalCard pCard : proposal.getReceiverPhysicalCards())
                        if (!receiverOwnedDeck.removePhysicalCard(pCard) || !senderOwnedDeck.addPhysicalCard(pCard))
                            throw new RuntimeException("DB ROLLBACK!");
                    deckMap.put(proposal.getReceiverUserEmail(), receiverDecks);
                    deckMap.put(proposal.getSenderUserEmail(), senderDecks);
                    proposalMap.remove(proposalId, proposal);
                    return true;
                }) != null;
    }

    public List<Proposal> getProposalListReceived(String token) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        List<Proposal> proposalList = new ArrayList<>();
        for (Proposal item : proposalMap.values())
            if (email.equals(item.getReceiverUserEmail()))
                proposalList.add(item);
        return proposalList;
    }

    public List<Proposal> getProposalListSent(String token) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        List<Proposal> proposalList = new ArrayList<>();
        for (Proposal item : proposalMap.values())
            if (email.equals(item.getSenderUserEmail()))
                proposalList.add(item);
        return proposalList;
    }

    @Override
    public boolean refuseOrWithdrawProposal(String token, int proposalId) throws AuthException, InputException, NullPointerException {
        String email = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        if (proposalId < 0) {
            throw new InputException("Invalid proposal Id");
        }

        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        if (proposalMap.size() == 0) {
            throw new RuntimeException("Not existing proposal");
        }

        Proposal proposal = proposalMap.get(proposalId);
        if (!email.equals(proposal.getSenderUserEmail()) && !email.equals(proposal.getReceiverUserEmail())) {
            throw new AuthException("You can only interact with proposals of which you are the sender or the receiver");
        }

        return proposalMap.remove(proposalId, proposal);
    }
}
