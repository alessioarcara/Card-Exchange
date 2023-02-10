package com.aadm.cardexchange.server.services;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.server.mapdb.MapDBImpl;
import com.aadm.cardexchange.shared.ExchangeService;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.Proposal;
import com.aadm.cardexchange.shared.models.User;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.util.ArrayList;
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

//    private  List<Proposal> GetProposalList(String email, boolean send, boolean received) throws BaseException {
//        if (send ^ received) {
//            Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
//            List<Proposal> proposalList = new ArrayList<>();
//            if (send) {
//                for (Proposal item : proposalMap.values()) {
//                    if (email.equals(item.getSenderUserEmail())) {
//                        proposalList.add(item);
//                    }
//                }
//            }
//            if (received) {
//                for (Proposal item : proposalMap.values()) {
//                    if (email.equals(item.getReceiverUserEmail())) {
//                        proposalList.add(item);
//                    }
//                }
//            }
//            return proposalList;
//        } else throw new InputException("Invalid request");
//    }

    public List<Proposal> getProposalListReceived(String token) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        List<Proposal> proposalList = new ArrayList<>();
        for (Proposal item : proposalMap.values()) {
            if (email.equals(item.getReceiverUserEmail())) {
                proposalList.add(item);
            }
        }
        return proposalList;
    }

    public List<Proposal> getProposalListSend(String token) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        List<Proposal> proposalList = new ArrayList<>();
        for (Proposal item : proposalMap.values()) {
            if (email.equals(item.getSenderUserEmail())) {
                proposalList.add(item);
            }
        }
        return proposalList;
    }
}

