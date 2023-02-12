package com.aadm.cardexchange.server.services;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.mapdb.MapDBConstants;
import com.aadm.cardexchange.server.mapdb.MapDBImpl;
import com.aadm.cardexchange.shared.DeckService;
import com.aadm.cardexchange.shared.DefaultDecksConstants;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.DeckNotFoundException;
import com.aadm.cardexchange.shared.exceptions.ExistingProposalException;
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

public class DeckServiceImpl extends RemoteServiceServlet implements DeckService, DefaultDecksConstants, MapDBConstants {
    private static final long serialVersionUID = 5868007467963819042L;
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
        return db.writeOperation(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type),
                (Map<String, Map<String, Deck>> deckMap) -> addDeck(email, deckName, false, deckMap));
    }

    @Override
    public boolean removeCustomDeck(String token, String deckName) throws AuthException {
        String email = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        return db.writeOperation(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type), (Map<String, Map<String, Deck>> deckMap) -> {
            Map<String, Deck> userDecks = deckMap.get(email);
            if (userDecks == null)
                return false;
            Deck deck = userDecks.get(deckName);
            if (deck == null || deck.isDefault())
                return false;
            userDecks.remove(deckName, deck);
            deckMap.put(email, userDecks);
            return true;
        });
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

    private static void checkFoundDeckValidity(String deckName, Deck foundDeck) throws DeckNotFoundException {
        if (foundDeck == null) {
            throw new DeckNotFoundException("Deck '" + deckName + "' not found.");
        }
    }

    private void checkIfCardExistsInProposal(PhysicalCard pCard) throws ExistingProposalException {
        Map<Integer, Proposal> proposalMap = db.getPersistentMap(getServletContext(), PROPOSAL_MAP_NAME, Serializer.INTEGER, new GsonSerializer<>(gson));
        if (proposalMap.values().stream().anyMatch(proposal -> proposal.getSenderPhysicalCards().contains(pCard) ||
                proposal.getReceiverPhysicalCards().contains(pCard)))
            throw new ExistingProposalException("Physical card edit/remove is not allowed as it already exists in a proposal.");
    }

    private void checkDeckNameValidity(String deckName) throws InputException {
        if (deckName == null || deckName.isEmpty())
            throw new InputException("Invalid deck name");
    }

    @Override
    public boolean addPhysicalCardToDeck(String token, Game game, String deckName, int cardId, Status status, String description) throws AuthException, InputException {
        /* PARAMETERS CHECK */
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        CardServiceImpl.checkGameValidity(game);
        checkDeckNameValidity(deckName);
        CardServiceImpl.checkCardIdValidity(cardId);
        if (status == null) {
            throw new InputException("Invalid status");
        }
        if (description == null || !checkDescriptionValidity(description)) {
            throw new InputException("Invalid description");
        }
        /* PHYSICAL CARD ADDITION TO DECK*/
        return db.writeOperation(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type), (Map<String, Map<String, Deck>> deckMap) -> {
            Map<String, Deck> decks = deckMap.get(userEmail);
            if (decks == null) {
                return false;
            }
            // selection of deck with deckName
            Deck foundDeck = decks.get(deckName);
            if (foundDeck == null) {
                return false;
            }
            // physical card addition
            if (!foundDeck.addPhysicalCard(new PhysicalCard(game, cardId, status, description))) {
                return false;
            }
            deckMap.put(userEmail, decks);
            return true;
        });
    }

    @Override
    public List<ModifiedDeckPayload> removePhysicalCardFromDeck(String token, String deckName, PhysicalCard pCard) throws AuthException, InputException, DeckNotFoundException {
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        checkDeckNameValidity(deckName);
        if (pCard == null)
            throw new InputException("Invalid physical card");
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> userDecks = deckMap.get(userEmail);
        List<ModifiedDeckPayload> modifiedDecks = new LinkedList<>();
        Deck foundDeck = userDecks.get(deckName);
        checkFoundDeckValidity(deckName, foundDeck);
        if (deckName.equals("Owned")) {
            for (Deck deck : userDecks.values()) {
                if (deck.removePhysicalCard(pCard)) {
                    modifiedDecks.add(new ModifiedDeckPayload(deck.getName(), joinPhysicalCardsWithCatalogCards(deck.getPhysicalCards())));
                }
            }
        } else {
            foundDeck.removePhysicalCard(pCard);
            modifiedDecks.add(new ModifiedDeckPayload(foundDeck.getName(), joinPhysicalCardsWithCatalogCards(foundDeck.getPhysicalCards())));
        }
        db.writeOperation(getServletContext(), () -> deckMap.put(userEmail, userDecks));
        return modifiedDecks;
    }

    @Override
    public List<ModifiedDeckPayload> editPhysicalCard(String token, String deckName, PhysicalCard pCard) throws AuthException, InputException, ExistingProposalException {
        String userEmail = AuthServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        checkDeckNameValidity(deckName);
        if (!deckName.equals(OWNED_DECK) && !deckName.equals(WISHED_DECK))
            throw new InputException("Sorry, you can only edit physical cards in Default decks.");
        if (pCard == null)
            throw new InputException("Invalid physical card");
        checkIfCardExistsInProposal(pCard);
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        Map<String, Deck> userDecks = deckMap.get(userEmail);
        List<ModifiedDeckPayload> modifiedDecks = new LinkedList<>();
        userDecks.values().forEach(deck -> {
            if (deck.removePhysicalCard(pCard)) {
                deck.addPhysicalCard(pCard);
                modifiedDecks.add(new ModifiedDeckPayload(deck.getName(), joinPhysicalCardsWithCatalogCards(deck.getPhysicalCards())));
            }
        });
        db.writeOperation(getServletContext(), () -> deckMap.put(userEmail, userDecks));
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
        CardServiceImpl.checkCardIdValidity(cardId);
        return getPhysicalCardByCardIdAndDeckName(cardId, OWNED_DECK);
    }

    @Override
    public List<PhysicalCardWithEmail> getWishedPhysicalCardsByCardId(int cardId) throws InputException {
        CardServiceImpl.checkCardIdValidity(cardId);
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
        checkDeckNameValidity(customDeckName);
        if (customDeckName.equals(OWNED_DECK) || customDeckName.equals(WISHED_DECK))
            throw new InputException("Default deck names not allowed for custom deck");
        if (pCards.isEmpty())
            throw new InputException("Empty list of physical cards");
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> userDecks = deckMap.get(userEmail);
        Deck userDeck = userDecks.get(customDeckName);
        checkFoundDeckValidity(customDeckName, userDeck);
        pCards.forEach(userDeck::addPhysicalCard);
        db.writeOperation(getServletContext(), () -> deckMap.put(userEmail, userDecks));
        // Return the modified deck's card list
        return joinPhysicalCardsWithCatalogCards(userDeck.getPhysicalCards());
    }


    @Override
    public List<PhysicalCardWithEmailDealing> getListPhysicalCardWithEmailDealing(String token, Game game, int cardId) throws AuthException, InputException, DeckNotFoundException {
        //Initial checks
        String userEmail = AuthServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson)));
        CardServiceImpl.checkGameValidity(game);
        CardServiceImpl.checkCardIdValidity(cardId);
        Map<String, Map<String, Deck>> deckMap = db.getPersistentMap(getServletContext(), DECK_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Deck> userDecks = deckMap.get(userEmail);
        Deck userDeck = userDecks.get(OWNED_DECK);
        checkFoundDeckValidity(OWNED_DECK, userDeck);
        //Prepare Owned array of idPCard matching id and switched by status
        String[] ownedMatchingPCardId = new String[5];
        int position;
        for (PhysicalCard pCard : userDeck.getPhysicalCards()) {
            if (cardId == pCard.getCardId()) {
                position = pCard.getStatus().getValue() - 1;
                //If position for this Status is empty, fill with this Pcard
                if (ownedMatchingPCardId[position] == null) {
                    ownedMatchingPCardId[position] = pCard.getId();
                    //if all Status positions are filled, exit from cycle
                    if (Arrays.stream(ownedMatchingPCardId).allMatch(Objects::nonNull))
                        break;
                }
            }
        }

        List<PhysicalCardWithEmail> listWishedPCard = getPhysicalCardByCardIdAndDeckName(cardId, WISHED_DECK);
        String offeredPCardId = null;
        List<PhysicalCardWithEmailDealing> result = new ArrayList<>();

        for (PhysicalCardWithEmail pCardWished : listWishedPCard) {
            for (int i = (pCardWished.getStatus().getValue() - 1); i < 5; i++) {
                if (ownedMatchingPCardId[i] != null) {
                    offeredPCardId = ownedMatchingPCardId[i];
                    break;
                }
            }
            result.add(new PhysicalCardWithEmailDealing(pCardWished, offeredPCardId));
            offeredPCardId = null;
        }
        return result;
    }

    private static Map<Integer, List<PhysicalCard>> WishedLookUpTable(Deck wishedDeck) {
        Map<Integer, List<PhysicalCard>> wishedLookUpTable = new HashMap<>();

        for (PhysicalCard pCardWished : wishedDeck.getPhysicalCards()) {
            int cardId = pCardWished.getCardId();
            wishedLookUpTable.computeIfAbsent(cardId, k -> new LinkedList<>());
            wishedLookUpTable.get(cardId).add(pCardWished);
        }
        return wishedLookUpTable;
    }

    public static Deck removePhysicalCardsFromWishedDecksAfterExchange(Deck wishedDeck, List<PhysicalCard> cardsToExchange) {
        //Create a lookup Table to direct access to PCardsWished by Catalogue CardId
        Map<Integer, List<PhysicalCard>> wishedLookUpTable = WishedLookUpTable(wishedDeck);

        for (PhysicalCard pCardProposal : cardsToExchange) {
            int cardId = pCardProposal.getCardId();
            if (!wishedLookUpTable.containsKey(cardId)) {
                continue;
            }

            for (PhysicalCard pCardWished : wishedLookUpTable.get(cardId)) {
                if (pCardWished.getStatus().getValue() <= pCardProposal.getStatus().getValue())
                    wishedDeck.removePhysicalCard(pCardWished);
            }
        }

        return wishedDeck;
    }
}
