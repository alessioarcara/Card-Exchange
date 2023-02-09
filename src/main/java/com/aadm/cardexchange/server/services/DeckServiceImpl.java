package com.aadm.cardexchange.server.services;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.server.mapdb.MapDBImpl;
import com.aadm.cardexchange.shared.DeckService;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.DeckNotFoundException;
import com.aadm.cardexchange.shared.exceptions.ExistingProposal;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
import com.aadm.cardexchange.shared.payloads.ModifiedDeckPayload;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.lang.reflect.Type;
import java.util.*;

import static com.aadm.cardexchange.server.services.AuthServiceImpl.validateEmail;

public class DeckServiceImpl extends RemoteServiceServlet implements DeckService, MapDBConstants {
    private static final long serialVersionUID = 5868007467963819042L;
    private static final String OWNED_DECK = "Owned";
    private static final String WISHED_DECK = "Wished";
    private final MapDB db;
    private final Gson gson = new Gson();
    private final Type type = new TypeToken<Map<String, Deck>>() {
    }.getType();

    public DeckServiceImpl() {
        db = new MapDBImpl();
    }

    public DeckServiceImpl(MapDB mockDB) {
        db = mockDB;
    }

    private static boolean addDeck(String email, String deckName, boolean isDefault, Map<String, Map<String, Deck>> deckMap) {
        Map<String, Deck> userDecks = deckMap.computeIfAbsent(email, k -> new LinkedHashMap<>());
        // if deck already exists in decks container do nothing
        if (userDecks.get(deckName) != null) {
            return false;
        }
        userDecks.put(deckName, new Deck(deckName, isDefault));
        deckMap.put(email, userDecks);
        return true;
    }

    @Override
    public boolean addDeck(String token, String deckName) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        return addDeck(email, deckName, false, deckMap);
    }

    public boolean removeCustomDeck(String token, String deckName) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> userDecks = deckMap.get(email);
        Deck deck = userDecks.get(deckName);
        if (deck == null || deck.isDefault()) {
            return false;
        }
        userDecks.remove(deckName, deck);
        deckMap.put(email, userDecks);
        return true;
    }

    public static boolean createDefaultDecks(String email, Map<String, Map<String, Deck>> deckMap) {
        return addDeck(email, OWNED_DECK, true, deckMap) && addDeck(email, WISHED_DECK, true, deckMap);
    }

    private boolean checkDescriptionValidity(String description) {
        int count = 0;
        for (int i = 0; i < description.length(); i++) {
            if (description.charAt(i) != ' ') {
                count++;
            }
            if (count >= 20) {
                return true;
            }
        }
        return false;
    }

    private void checkDeckNameInvalidity(String deckName) throws InputException {
        if (deckName == null || deckName.isEmpty())
            throw new InputException("Invalid deck name");
    }

    private void checkIfCardExistsInProposal(PhysicalCard pCard) throws ExistingProposal {
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        if (proposalMap.values().stream().anyMatch(proposal -> proposal.getSenderPhysicalCards().contains(pCard) ||
                proposal.getReceiverPhysicalCards().contains(pCard)))
            throw new ExistingProposal("Physical card edit/remove is not allowed as it already exists in a proposal.");
    }

    @Override
    public boolean addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description) throws AuthException, InputException {
        /* PARAMETERS CHECK */
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        CardServiceImpl.checkGameInvalidity(game);
        checkDeckNameInvalidity(deckName);
        CardServiceImpl.checkCardIdInvalidity(cardId);
        if (status == null) {
            throw new InputException("Invalid status");
        }
        if (description == null || !checkDescriptionValidity(description)) {
            throw new InputException("Invalid description");
        }
        /* PHYSICAL CARD ADDITION TO DECK*/
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> decks = deckMap.get(userEmail);
        if (decks == null) {
            throw new RuntimeException("Not existing decks");
        }
        // selection of deck with deckName
        Deck foundDeck = decks.get(deckName);
        if (foundDeck == null) {
            return false;
        }
        // physical card addition
        if (foundDeck.addPhysicalCard(new PhysicalCard(game, cardId, status, description))) {
            deckMap.put(userEmail, decks);
            return true;
        }
        return false;
    }

    @Override
    public boolean removePhysicalCardFromDeck(String token, String deckName, PhysicalCard pCard) throws AuthException, InputException {
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        checkDeckNameInvalidity(deckName);
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> decks = new LinkedHashMap<>(deckMap.get(userEmail));
        if (decks.size() == 0) {
            throw new RuntimeException("Not existing decks");
        }
        Deck foundDeck = decks.get(deckName);
        if (foundDeck == null) {
            return false;
        }
        if (foundDeck.removePhysicalCard(pCard)) {
            deckMap.put(userEmail, decks);
            return true;
        } else return false;
    }

    @Override
    public List<ModifiedDeckPayload> editPhysicalCard(String token, String deckName, PhysicalCard pCard) throws AuthException, InputException, ExistingProposal {
        String email = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        checkDeckNameInvalidity(deckName);
        if (!deckName.equals(OWNED_DECK) && !deckName.equals(WISHED_DECK))
            throw new InputException("Sorry, you can only edit physical cards in Default decks.");
        if (pCard == null)
            throw new InputException("Invalid physical card");
        checkIfCardExistsInProposal(pCard);
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Map<String, Deck> userDecks = deckMap.get(email);
        List<ModifiedDeckPayload> modifiedDecks = new LinkedList<>();
        userDecks.values().forEach(deck -> {
            if (deck.removePhysicalCard(pCard)) {
                deck.addPhysicalCard(pCard);
                modifiedDecks.add(new ModifiedDeckPayload(deck.getName(), joinPhysicalCardsWithCatalogCards(deck.getPhysicalCards())));
            }
        });
        deckMap.put(email, userDecks);
        return modifiedDecks;
    }

    @Override
    public List<String> getUserDeckNames(String token) throws AuthException {
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> decks = deckMap.get(userEmail);
        return decks == null ? Collections.emptyList() : new ArrayList<>(decks.keySet());
    }

    private List<PhysicalCardWithName> getUserDeck(String userEmail, String deckName) {
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> userDecks = deckMap.get(userEmail);
        Deck userDeck = userDecks.get(deckName);
        return joinPhysicalCardsWithCatalogCards(userDeck.getPhysicalCards());
    }

    @Override
    public List<PhysicalCardWithName> getMyDeck(String token, String deckName) throws AuthException {
        return getUserDeck(AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson))), deckName);
    }

    @Override
    public List<PhysicalCardWithName> getUserOwnedDeck(String email) throws AuthException {
        if (email == null || !validateEmail(email)) {
            throw new AuthException("Invalid email");
        }
        return getUserDeck(email, OWNED_DECK);
    }

    private List<PhysicalCardWithEmail> getPhysicalCardByCardIdAndDeckName(int cardId, String deckName) {
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Set<String> userEmails = deckMap.keySet();
        List<PhysicalCardWithEmail> pCardsWithEmail = new ArrayList<>();
        for (String userEmail : userEmails) {
            Set<PhysicalCard> pCards = deckMap.get(userEmail).get(deckName).getPhysicalCards();
            for (PhysicalCard pCard : pCards) {
                if (pCard.getCardId() == cardId) {
                    pCardsWithEmail.add(new PhysicalCardWithEmail(pCard, userEmail));
                }
            }
        }
        return pCardsWithEmail;
    }

    @Override
    public List<PhysicalCardWithEmail> getOwnedPhysicalCardsByCardId(int cardId) throws InputException {
        CardServiceImpl.checkCardIdInvalidity(cardId);
        return getPhysicalCardByCardIdAndDeckName(cardId, OWNED_DECK);
    }

    @Override
    public List<PhysicalCardWithEmail> getWishedPhysicalCardsByCardId(int cardId) throws InputException {
        CardServiceImpl.checkCardIdInvalidity(cardId);
        return getPhysicalCardByCardIdAndDeckName(cardId, WISHED_DECK);
    }

    private List<PhysicalCardWithName> joinPhysicalCardsWithCatalogCards(Set<PhysicalCard> pCards) {
        List<PhysicalCardWithName> pCardsWithName = new LinkedList<>();
        for (PhysicalCard pCard : pCards) {
            pCardsWithName.add(new PhysicalCardWithName(
                    pCard,
                    CardServiceImpl.getNameCard(pCard.getCardId(), db.getCachedMap(
                            getServletContext(),
                            CardServiceImpl.getCardMap(pCard.getGameType()),
                            Serializer.INTEGER,
                            new GsonSerializer<>(gson)
                    ))
            ));
        }
        return pCardsWithName;
    }

    @Override
    public List<PhysicalCardWithName> addPhysicalCardsToCustomDeck(String token, String customDeckName, List<PhysicalCard> pCards) throws AuthException, InputException, DeckNotFoundException {
        String userEmail = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        checkDeckNameInvalidity(customDeckName);
        if (customDeckName.equals(OWNED_DECK) || customDeckName.equals(WISHED_DECK))
            throw new InputException("Default deck names not allowed for custom deck");
        if (pCards.isEmpty())
            throw new InputException("Empty list of physical cards");
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> userDecks = deckMap.get(userEmail);
        Deck userDeck = userDecks.get(customDeckName);
        if (userDeck == null)
            throw new DeckNotFoundException("Custom deck '" + customDeckName + "' not found.");
        pCards.forEach(userDeck::addPhysicalCard);
        deckMap.put(userEmail, userDecks);
        // Return the modified deck's card list
        return joinPhysicalCardsWithCatalogCards(userDeck.getPhysicalCards());
    }
}
