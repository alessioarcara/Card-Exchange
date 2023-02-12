package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.services.DeckServiceImpl;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.DeckNotFoundException;
import com.aadm.cardexchange.shared.exceptions.ExistingProposalException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
import com.aadm.cardexchange.shared.payloads.ModifiedDeckPayload;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapdb.Serializer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.*;

import static org.easymock.EasyMock.*;

public class DeckServiceTest {
    @Nested
    class WithMockDB {
        ServletConfig mockConfig;
        ServletContext mockCtx;
        private IMocksControl ctrl;
        private MapDB mockDB;
        private DeckServiceImpl deckService;

        @BeforeEach
        public void initialize() throws ServletException {
            ctrl = createStrictControl();
            mockDB = ctrl.createMock(MapDB.class);
            deckService = new DeckServiceImpl(mockDB);
            mockConfig = ctrl.createMock(ServletConfig.class);
            mockCtx = ctrl.createMock(ServletContext.class);
            deckService.init(mockConfig);
        }

        @Test
        public void testIfDeckAlreadyExist() {
            Map<String, Map<String, Deck>> deckMap = new HashMap<>();
            Map<String, Deck> mockDecks = new HashMap<>() {{
                put("Owned", new Deck("Owned"));
                put("Wished", new Deck("Wished"));
            }};
            deckMap.put("test@test.it", mockDecks);
            ctrl.replay();
            Assertions.assertFalse(DeckServiceImpl.createDefaultDecks("test@test.it", deckMap));
            ctrl.verify();
        }

        @Test
        public void testDefaultDeckCreation() {
            Map<String, Map<String, Deck>> deckMap = new HashMap<>();
            ctrl.replay();
            Assertions.assertTrue(DeckServiceImpl.createDefaultDecks("testbis@test.it", deckMap));
            ctrl.verify();
            // check if default decks "Owned", "Wished" are present
            Assertions.assertAll(() -> {
                Assertions.assertNotNull(deckMap.get("testbis@test.it").get("Owned"));
                Assertions.assertNotNull(deckMap.get("testbis@test.it").get("Wished"));
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testAddPhysicalCardToDeckForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () ->
                    deckService.addPhysicalCardToDeck(input, Game.MAGIC, "Owned", 111, Status.Damaged, "This is a valid description.")
            );
            ctrl.verify();
        }

        private void setupForValidToken() {
            Map<String, LoginInfo> mockLoginMap = new HashMap<>() {{
                put("validToken", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockLoginMap);
        }

        private void setupForInvalidToken() {
            Map<String, LoginInfo> mockLoginMap = new HashMap<>() {{
                put("validToken1", new LoginInfo("test@test1.it", System.currentTimeMillis() - 10000));
                put("validToken2", new LoginInfo("test@test2.it", System.currentTimeMillis() - 20000));
                put("validToken3", new LoginInfo("test@test3.it", System.currentTimeMillis() - 30000));
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockLoginMap);
        }

        @Test
        public void testAddPhysicalCardToDeckForInvalidGame() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> {
                deckService.addPhysicalCardToDeck("validToken", null, "Owned", 111, Status.Excellent, "This is a valid description.");
            });
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        public void testAddPhysicalCardToDeckForInvalidDeckName(String input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () ->
                    deckService.addPhysicalCardToDeck("validToken", Game.MAGIC, input, 111, Status.Excellent, "This is a valid description.")
            );
            ctrl.verify();
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0})
        public void testAddPhysicalCardToDeckForInvalidCardId(int input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () ->
                    deckService.addPhysicalCardToDeck("validToken", Game.MAGIC, "Owned", input, Status.Excellent, "This is a valid description.")
            );
            ctrl.verify();
        }

        @Test
        public void testAddPhysicalCardToDeckForInvalidStatus() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () ->
                    deckService.addPhysicalCardToDeck("validToken", Game.MAGIC, "Owned", 111, null, "This is a valid description.")
            );
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"not valid descr", "1234567890123456789", "1 3 5 7 9 1 3 5 7 9 "})
        public void testAddPhysicalCardToDeckForInvalidDescription(String input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () ->
                    deckService.addPhysicalCardToDeck("validToken", Game.MAGIC, "Owned", 111, Status.Excellent, input)
            );
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testAddDeckForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.addDeck(input, "testDeckName"));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetUserDeckNamesForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.getUserDeckNames(input));
            ctrl.verify();
        }

        @Test
        public void testGetUserDeckNamesForNotExistingDeckMap() throws AuthException {
            setupForValidToken();
            Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
                put("test@test.it", null);
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockDeckMap);
            ctrl.replay();
            Assertions.assertTrue(deckService.getUserDeckNames("validToken").isEmpty());
            ctrl.verify();
        }

        @Test
        public void testGetUserDeckNamesForReturnCorrectDeckNames() throws AuthException {
            List<String> mockDeckNames = Arrays.asList("Owned", "Wished", "Test");
            setupForValidToken();
            Map<String, Deck> decks = new HashMap<>() {{
                put("Owned", new Deck("Owned", true));
                put("Wished", new Deck("Wished", true));
                put("Test", new Deck("Test", false));
            }};
            Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
                put("test@test.it", decks);
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockDeckMap);
            ctrl.replay();
            List<String> deckNames = deckService.getUserDeckNames("validToken");
            ctrl.verify();
            Assertions.assertAll(() -> {
                Assertions.assertEquals(mockDeckNames.size(), deckNames.size());
                Assertions.assertIterableEquals(mockDeckNames, deckNames);
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetDeckByNameForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.getMyDeck(input, "Owned"));
            ctrl.verify();
        }

        @Test
        public void testGetDeckByNameForNotExistingDeck() {
            setupForValidToken();
            Map<String, Deck> deckMap = new HashMap<>();
            Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
                put("test@test.it", deckMap);
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockDeckMap);
            ctrl.replay();
            Assertions.assertThrows(NullPointerException.class, () -> deckService.getMyDeck("validToken", "Owned"));
            ctrl.verify();
        }

        private void setupForValidCardsAndUserDecks() {
            // init Mocks
            PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 1111, Status.Excellent, "This is a valid description.");
            PhysicalCard mockPCard2 = new PhysicalCard(Game.MAGIC, 2222, Status.Fair, "This is a valid bis description.");
            Card mockCard1 = DummyData.createPokemonDummyCard();
            Card mockCard2 = DummyData.createYuGiOhDummyCard();
            Map<Integer, Card> cardMap = new HashMap<>() {{
                put(1111, mockCard1);
                put(2222, mockCard2);
            }};
            Map<String, Deck> userDecks = new HashMap<>() {{
                put("Owned", new Deck("Owned", true));
            }};
            userDecks.get("Owned").addPhysicalCard(mockPCard1);
            userDecks.get("Owned").addPhysicalCard(mockPCard2);
            Map<String, Map<String, Deck>> deckMap = new HashMap<>() {{
                put("test@test.it", userDecks);
            }};

            // what I expect
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(deckMap);
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(cardMap);
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(cardMap);
        }

        private void setupDefaultDecks(String deckName, String mail1, String mail2) {
            PhysicalCard mockPCard1 = new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is the card that I want.");
            PhysicalCard mockPCard2 = new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is the card that I want.");
            PhysicalCard mockPCard3 = new PhysicalCard(Game.randomGame(), 2222, Status.randomStatus(), "This is the card that I want.");
            PhysicalCard mockPCard4 = new PhysicalCard(Game.randomGame(), 3333, Status.randomStatus(), "This is the card that I want.");
            Deck mockDeck1 = new Deck(deckName, true);
            Deck mockDeck2 = new Deck(deckName, true);

            mockDeck1.addPhysicalCard(mockPCard1);
            mockDeck1.addPhysicalCard(mockPCard4);
            mockDeck2.addPhysicalCard(mockPCard3);
            mockDeck2.addPhysicalCard(mockPCard2);
            Map<String, Map<String, Deck>> deckMap = new LinkedHashMap<>() {{
                put(mail1, new HashMap<>() {{
                    put(deckName, mockDeck1);
                }});
                put(mail2, new HashMap<>() {{
                    put(deckName, mockDeck2);
                }});
            }};

            // expects
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class))).andReturn(deckMap);
        }

        @Test
        public void testGetDeckByNameForValidEntry() throws AuthException {
            setupForValidToken();
            setupForValidCardsAndUserDecks();
            ctrl.replay();
            List<PhysicalCardWithName> decoratedCards = deckService.getMyDeck("validToken", "Owned");
            ctrl.verify();
            Assertions.assertAll(() -> {
                Assertions.assertEquals(2, decoratedCards.size());
                Assertions.assertEquals("Charizard", decoratedCards.get(0).getName());
                Assertions.assertEquals("Exodia the Forbidden One", decoratedCards.get(1).getName());
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"test", "test@", "test@test", "test@test."})
        public void testGetUserOwnedDeckForInvalidEmail(String input) {
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.getUserOwnedDeck(input));
            ctrl.verify();
        }

        @Test
        public void testGetUserOwnedDeckForValidEmail() throws AuthException {
            setupForValidCardsAndUserDecks();
            ctrl.replay();
            List<PhysicalCardWithName> decoratedCards = deckService.getUserOwnedDeck("test@test.it");
            ctrl.verify();
            Assertions.assertAll(() -> {
                Assertions.assertEquals(2, decoratedCards.size());
                Assertions.assertEquals("Charizard", decoratedCards.get(0).getName());
                Assertions.assertEquals("Exodia the Forbidden One", decoratedCards.get(1).getName());
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0})
        public void testGetOwnedPhysicalCardsByCardIdForInvalidId(int input) {
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> deckService.getOwnedPhysicalCardsByCardId(input));
            ctrl.verify();
        }

        @Test
        public void testGetOwnedPhysicalCardsByCardIdForValidId() throws InputException {
            // init Mocks
            String emailTest1 = "test1@test.it";
            String emailTest2 = "test2@test.it";
            setupDefaultDecks("Owned", emailTest1, emailTest2);
            ctrl.replay();
            List<PhysicalCardWithEmail> pCardsWithEmail = deckService.getOwnedPhysicalCardsByCardId(1111);
            ctrl.verify();
            Assertions.assertAll(() -> {
                Assertions.assertEquals(2, pCardsWithEmail.size());
                Assertions.assertEquals(emailTest1, pCardsWithEmail.get(0).getEmail());
                Assertions.assertEquals(emailTest2, pCardsWithEmail.get(1).getEmail());
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        public void testRemovePhysicalCardFromDeckForInvalidDeckName(String input) {
            setupForValidToken();
            PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 1111, Status.Excellent, "This is a valid description.");
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () ->
                    deckService.removePhysicalCardFromDeck("validToken", input, mockPCard1)
            );
            ctrl.verify();
        }

        @Test
        public void testRemovePhysicalCardFromDeckForNotExistingDeckMap() {
            setupForValidToken();
            PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 1111, Status.Excellent, "This is a valid description.");

            Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
                put("test@test.it", null);
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockDeckMap);
            ctrl.replay();
            Assertions.assertThrows(RuntimeException.class, () ->
                    deckService.removePhysicalCardFromDeck("validToken", "Owned", mockPCard1)
            );
            ctrl.verify();
        }

        @Test
        public void testRemovePhysicalCardFromDeckForExistingProposal() {
            // init mocks
            PhysicalCard mockPCard = new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description.");
            List<PhysicalCard> testList = DummyData.createPhysicalCardDummyList(5);
            testList.add(mockPCard);
            Proposal mockProposal1 = new Proposal("test1@test.it", "test2@test.it",
                    testList, DummyData.createPhysicalCardDummyList(5));
            Proposal mockProposal2 = new Proposal("test3@test.it", "test4@test.it",
                    DummyData.createPhysicalCardDummyList(5), DummyData.createPhysicalCardDummyList(5));

            // expects
            setupForValidToken();
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put(0, mockProposal1);
                        put(1, mockProposal2);
                    }});
            ctrl.replay();
            Assertions.assertThrows(ExistingProposalException.class, () -> deckService.removePhysicalCardFromDeck("validToken", "Owned", mockPCard));
            ctrl.verify();
        }

        @Test
        public void testRemovePhysicalCardFromDeckForNotExistingDeck() {
            setupForValidToken();
            PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 1111, Status.Excellent, "This is a valid description.");
            Map<String, Deck> deckMap = new HashMap<>() {{
                put("Wished", new Deck("Wished", true));
            }};
            Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
                put("test@test.it", deckMap);
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>());
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockDeckMap);
            ctrl.replay();
            Assertions.assertThrows(DeckNotFoundException.class, () -> deckService.removePhysicalCardFromDeck("validToken", "Owned", mockPCard1));
            ctrl.verify();
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0})
        public void testGetWishedPhysicalCardsByCardIdForInvalidId(int input) {
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> deckService.getWishedPhysicalCardsByCardId(input));
            ctrl.verify();
        }

        @Test
        public void testGetWishedPhysicalCardsByCardIdForValidId() throws InputException {
            // init mocks
            String emailTest1 = "test1@test.it";
            String emailTest2 = "test2@test.it";
            setupDefaultDecks("Wished", emailTest1, emailTest2);

            ctrl.replay();
            List<PhysicalCardWithEmail> pCardsWithEmail = deckService.getWishedPhysicalCardsByCardId(1111);
            ctrl.verify();

            Assertions.assertAll(() -> {
                Assertions.assertEquals(emailTest1, pCardsWithEmail.get(0).getEmail());
                Assertions.assertEquals(emailTest2, pCardsWithEmail.get(1).getEmail());
                Assertions.assertEquals(2, pCardsWithEmail.size());
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testRemoveCustomDeckForInvalidToken(String input) {
            Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
                put("token1", new LoginInfo("test@test.it", System.currentTimeMillis() - 100000));
                put("token2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 200000));
                put("token3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 300000));
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(loginInfoMap);
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.removeCustomDeck(input, "testDeckName"));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testAddPhysicalCardsToCustomDeckForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.addPhysicalCardsToCustomDeck(input, "test",
                    Arrays.asList(
                            new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.randomGame(), 2222, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.randomGame(), 3333, Status.randomStatus(), "This is a valid description.")
                    )));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"Owned", "Wished"})
        public void testAddPhysicalCardsToCustomDeckForInvalidDeckName(String input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> deckService.addPhysicalCardsToCustomDeck("validToken", input,
                    Arrays.asList(
                            new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.randomGame(), 2222, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.randomGame(), 3333, Status.randomStatus(), "This is a valid description.")
                    )));
            ctrl.verify();
        }

        @Test
        public void testAddPhysicalCardsToCustomDeckForEmptyList() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> deckService.addPhysicalCardsToCustomDeck("validToken",
                    "test", Collections.emptyList()));
            ctrl.verify();
        }

        @Test
        public void testAddPhysicalCardsToCustomDeckForCustomDeckNotFound() {
            setupForValidToken();
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put("test@test.it", new HashMap<>() {{
                            put("Owned", new Deck("Owned", true));
                            put("Wished", new Deck("Wished", true));
                        }});
                    }});

            ctrl.replay();
            Assertions.assertThrows(DeckNotFoundException.class, () -> deckService.addPhysicalCardsToCustomDeck("validToken", "test",
                    Arrays.asList(
                            new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.randomGame(), 2222, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.randomGame(), 3333, Status.randomStatus(), "This is a valid description.")
                    )));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testEditPhysicalCardForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.editPhysicalCard(input,
                    "Owned", new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description.")));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"custom"})
        public void testEditPhysicalCardForInvalidDeckName(String input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> deckService.editPhysicalCard("validToken",
                    input, new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description.")));
            ctrl.verify();
        }

        @Test
        public void testEditPhysicalCardForInvalidPhysicalCard() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> deckService.editPhysicalCard("validToken", "Owned", null));
            ctrl.verify();
        }

        @Test
        public void testEditPhysicalCardForExistingProposal() {
            // init mocks
            PhysicalCard mockPCard = new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description.");
            List<PhysicalCard> testList = DummyData.createPhysicalCardDummyList(5);
            testList.add(mockPCard);
            Proposal mockProposal1 = new Proposal("test2@test.it", "test2@test.it",
                    testList, DummyData.createPhysicalCardDummyList(5));
            Proposal mockProposal2 = new Proposal("test3@test.it", "test4@test.it",
                    DummyData.createPhysicalCardDummyList(5), DummyData.createPhysicalCardDummyList(5));

            // expects
            setupForValidToken();
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put(0, mockProposal1);
                        put(1, mockProposal2);
                    }});
            ctrl.replay();
            Assertions.assertThrows(ExistingProposalException.class, () -> deckService.editPhysicalCard("validToken",
                    "Owned", mockPCard));
            ctrl.verify();
        }


        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetListPhysicalCardWithEmailDealingForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> deckService.getListPhysicalCardWithEmailDealing(input, Game.randomGame(), 1));
            ctrl.verify();
        }

        @Test
        public void testGetListPhysicalCardWithEmailDealingForNullGame() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> {
                deckService.getListPhysicalCardWithEmailDealing("validToken", null, 1);
                ctrl.verify();
            });
        }

        @Test
        public void testGetListPhysicalCardWithEmailDealingForNegativeCardId() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(InputException.class, () -> {
                deckService.getListPhysicalCardWithEmailDealing("validToken", Game.randomGame(), -1);
                ctrl.verify();
            });
        }

        @Test
        public void testGetListPhysicalCardWithEmailDealingForDeckNotFound() {
            setupForValidToken();
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put("test@test.it", new HashMap<>());
                    }});

            ctrl.replay();
            Assertions.assertThrows(DeckNotFoundException.class, () -> deckService.getListPhysicalCardWithEmailDealing("validToken", Game.randomGame(), 1));
            ctrl.verify();
        }

        @Test
        public void testGetListPhysicalCardWithEmailDealingWithEmptyOwnedDeck() throws DeckNotFoundException, InputException, AuthException {
            // init mocks
            //Generate empty Owned deck and  Wished consistent deck
            PhysicalCard wishedCard1 = new PhysicalCard(Game.MAGIC, 1, Status.Excellent, "This is a valid description.");
            PhysicalCard wishedCard2 = new PhysicalCard(Game.MAGIC, 1, Status.Fair, "This is a valid description.");
            PhysicalCard wishedCard3 = new PhysicalCard(Game.MAGIC, 3, Status.Fair, "This is a valid description.");
            Deck wishedDeck = new Deck("Wished", true);
            wishedDeck.addPhysicalCard(wishedCard1);
            wishedDeck.addPhysicalCard(wishedCard2);
            wishedDeck.addPhysicalCard(wishedCard3);

            // expects
            setupForValidToken();
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put("test@test.it", new HashMap<>() {{
                            put("Owned", new Deck("Owned", true));
                        }});
                    }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put("test@test.it", new HashMap<>() {{
                            put("Wished", wishedDeck);
                        }});
                    }});

            ctrl.replay();
            List<PhysicalCardWithEmailDealing> actual = deckService.getListPhysicalCardWithEmailDealing("validToken", Game.MAGIC, 1);
            Assertions.assertAll(() -> {
                Assertions.assertEquals(2, actual.size());
                Assertions.assertNull(actual.get(0).getIdPhysicalCardPawn());
                Assertions.assertNull(actual.get(1).getIdPhysicalCardPawn());
            });
            ctrl.verify();
        }


        @Test
        public void testGetListPhysicalCardWithEmailDealingMatchingIdCard() throws DeckNotFoundException, InputException, AuthException {
            // init mocks
            //Generate empty Owned deck
            PhysicalCard ownedCard1 = new PhysicalCard(Game.MAGIC, 1, Status.VeryDamaged, "This is a valid description.");
            PhysicalCard ownedCard2 = new PhysicalCard(Game.MAGIC, 1, Status.Fair, "This is a valid description.");        //Match
            PhysicalCard ownedCard3 = new PhysicalCard(Game.MAGIC, 2, Status.Good, "This is a valid description.");
            Deck ownedDeck = new Deck("Owned", true);
            ownedDeck.addPhysicalCard(ownedCard1);
            ownedDeck.addPhysicalCard(ownedCard2);
            ownedDeck.addPhysicalCard(ownedCard3);


            //Generate Wished consistent deck
            PhysicalCard wishedCard1 = new PhysicalCard(Game.MAGIC, 1, Status.Excellent, "This is a valid description.");
            PhysicalCard wishedCard2 = new PhysicalCard(Game.MAGIC, 1, Status.Fair, "This is a valid description.");
            PhysicalCard wishedCard3 = new PhysicalCard(Game.MAGIC, 3, Status.Fair, "This is a valid description.");
            Deck wishedDeck = new Deck("Wished", true);
            wishedDeck.addPhysicalCard(wishedCard1);
            wishedDeck.addPhysicalCard(wishedCard2);
            wishedDeck.addPhysicalCard(wishedCard3);

            // expects
            setupForValidToken();
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put("test@test.it", new HashMap<>() {{
                            put("Owned", ownedDeck);
                        }});
                    }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<>() {{
                        put("test@test.it", new HashMap<>() {{
                            put("Wished", wishedDeck);
                        }});
                    }});

            ctrl.replay();
            List<PhysicalCardWithEmailDealing> actual = deckService.getListPhysicalCardWithEmailDealing("validToken", Game.MAGIC, 1);
            Assertions.assertAll(() -> {
                Assertions.assertEquals(2, actual.size());                                     //like wished card list size
                Assertions.assertNull(actual.get(0).getIdPhysicalCardPawn());                          //first wished card in list don't match
                Assertions.assertEquals(ownedCard2.getId(), actual.get(1).getIdPhysicalCardPawn());    //second wished card in list match with second Owned card
            });
            ctrl.verify();
        }

        @Test
        public void testRemovePhysicalCardsFromWishedDeckAfterExchangeForEmptyDeck() {
            Deck emptyDeck = new Deck("empty");

            ctrl.replay();
            Deck actual = DeckServiceImpl.removePhysicalCardsFromWishedDecksAfterExchange(emptyDeck, new ArrayList<>());
            Assertions.assertTrue(actual.getPhysicalCards().isEmpty());
            ctrl.verify();
        }

        @Test
        public void testRemovePhysicalCardsFromWishedDeckAfterExchangeForEmptyCards() {
            Deck deck = new Deck("deck");
            for (PhysicalCard pCard : DummyData.createPhysicalCardDummyList(5)) {
                deck.addPhysicalCard(pCard);
            }

            ctrl.replay();
            Deck actual = DeckServiceImpl.removePhysicalCardsFromWishedDecksAfterExchange(deck, new ArrayList<>());
            Assertions.assertEquals(actual.getPhysicalCards().size(), deck.getPhysicalCards().size());
            ctrl.verify();
        }

        @Test
        public void testRemovePhysicalCardsFromWishedDeckAfterExchangeSuccess() {
            // Wished Deck
            PhysicalCard pCard1 = new PhysicalCard(Game.MAGIC, 111, Status.VeryDamaged, "This is a valid description.");
            PhysicalCard pCard2 = new PhysicalCard(Game.MAGIC, 123, Status.Excellent, "This is a valid description.");
            PhysicalCard pCard3 = new PhysicalCard(Game.MAGIC, 123, Status.Good, "This is a valid description.");        //Match
            PhysicalCard pCard4 = new PhysicalCard(Game.MAGIC, 123, Status.VeryDamaged, "This is a valid description."); //Match

            Deck wishedDeck = new Deck("Wished", true);
            wishedDeck.addPhysicalCard(pCard1);
            wishedDeck.addPhysicalCard(pCard2);
            wishedDeck.addPhysicalCard(pCard3);
            wishedDeck.addPhysicalCard(pCard4);

            // setup cards list
            List<PhysicalCard> listPhysicalCards = new ArrayList<>();
            listPhysicalCards.add(new PhysicalCard(Game.MAGIC, 888, Status.randomStatus(), "This is a valid description."));
            listPhysicalCards.add(new PhysicalCard(Game.MAGIC, 444, Status.Excellent, "This is a valid description."));
            listPhysicalCards.add(pCard3);          //Match

            ctrl.replay();
            Deck actual = DeckServiceImpl.removePhysicalCardsFromWishedDecksAfterExchange(wishedDeck, listPhysicalCards);
            ctrl.verify();

            Status actualPCardStatus = null;
            for (PhysicalCard pCard : actual.getPhysicalCards()) {
                if (pCard.getCardId() == pCard3.getCardId())
                    actualPCardStatus = pCard.getStatus();
            }

            Status finalActualPCardStatus = actualPCardStatus;
            Assertions.assertAll(() -> {
                Assertions.assertEquals(2, actual.getPhysicalCards().size());
                Assertions.assertEquals(Status.Excellent, finalActualPCardStatus);
            });
        }
    }

    @Nested
    class WithFakeDB {
        ServletConfig mockConfig;
        ServletContext mockCtx;

        @BeforeEach
        public void initialize() throws ServletException {
            mockConfig = createStrictMock(ServletConfig.class);
            mockCtx = createStrictMock(ServletContext.class);
        }

        private DeckServiceImpl initializeDeckService(Map<String, Map<String, Deck>> deckMap) throws ServletException {
            FakeDB fakeDB = new FakeDB(new HashMap<>(), deckMap);
            DeckServiceImpl deckService = new DeckServiceImpl(fakeDB);
            deckService.init(mockConfig);
            return deckService;
        }

        @Test
        public void testAddDeckForNotAlreadyExistingDeck() throws AuthException, ServletException {
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new LinkedHashMap<>() {{
                    put("Owned", new Deck("Owned", true));
                    put("Wished", new Deck("Wished", true));
                }});
            }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertTrue(deckService.addDeck("validToken", "testDeckName"));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testAddDeckForAlreadyExistingDeck() throws AuthException, ServletException {
            String deckName = "testDeckName";
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new LinkedHashMap<>() {{
                    put(deckName, new Deck(deckName, false));
                }});
            }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertFalse(deckService.addDeck("validToken", deckName));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testRemoveCustomDeckForNotExistingDeck() throws AuthException, ServletException {
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new LinkedHashMap<>());
            }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertFalse(deckService.removeCustomDeck("validToken", "testDeckName"));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testRemoveCustomDeckForDefaultDeck() throws AuthException, ServletException {
            String deckName = "testDeckName";
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new LinkedHashMap<>() {{
                    put(deckName, new Deck(deckName, true));
                }});
            }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertFalse(deckService.removeCustomDeck("validToken", deckName));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testRemoveCustomDeckForRemovableCustomDeck() throws AuthException, ServletException {
            String deckName = "testDeckName";
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new LinkedHashMap<>() {{
                    put(deckName, new Deck(deckName, false));
                }});
            }});
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertTrue(deckService.removeCustomDeck("validToken", deckName));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testAddPhysicalCardToDeckForNotExistingUserDecks() throws ServletException, InputException, AuthException {
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>());
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertFalse(deckService.addPhysicalCardToDeck("validToken", Game.MAGIC, "Owned", 111, Status.Excellent, "This is a valid description."));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testAddPhysicalCardToDeckForNotExistingDeck() throws ServletException, InputException, AuthException {
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new LinkedHashMap<>());
            }});
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertFalse(deckService.addPhysicalCardToDeck("validToken", Game.MAGIC, "Owned", 111, Status.Excellent, "This is a valid description."));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testAddPhysicalCardToDeckForCardAddition() throws ServletException, InputException, AuthException {
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new LinkedHashMap<>() {{
                    put("Owned", new Deck("Owned", true));
                }});
            }});
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertTrue(deckService.addPhysicalCardToDeck("validToken", Game.MAGIC, "Owned", 111, Status.Excellent, "This is a valid description."));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testRemovePhysicalCardFromDeckForNotExistingPhysicalCard() throws ServletException, DeckNotFoundException, InputException, AuthException, ExistingProposalException {
            PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 1111, Status.Excellent, "This is a valid description.");
            PhysicalCard mockPCard2 = new PhysicalCard(Game.MAGIC, 1221, Status.Fair, "This is a valid bis description.");
            Map<String, Deck> deckMap = new LinkedHashMap<>() {{
                put("Owned", new Deck("Owned", true));
            }};
            deckMap.get("Owned").addPhysicalCard(mockPCard2);

            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", deckMap);
            }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(4);
            replay(mockConfig, mockCtx);
            Assertions.assertEquals(Collections.emptyList(), deckService.removePhysicalCardFromDeck("validToken", "Owned", mockPCard1));
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testRemovePhysicalCardFromDeckSuccess() throws ServletException, DeckNotFoundException, InputException, AuthException, ExistingProposalException {
            final int numOfCards = 4;

            // init mocks
            PhysicalCard targetPCard = new PhysicalCard(Game.MAGIC, 1111, Status.Excellent, "This is a valid description.");

            List<Deck> decks = Arrays.asList(
                    new Deck("Owned", true),
                    new Deck("Wished", true),
                    new Deck("custom1"),
                    new Deck("custom2"),
                    new Deck("custom3")
            );

            Map<String, Deck> mockUserDeckMap = new HashMap<>();
            // load physical cards on existing decks
            for (Deck deck : decks) {
                for (PhysicalCard pCard : DummyData.createPhysicalCardDummyList(numOfCards)) {
                    deck.addPhysicalCard(pCard);
                }
                if (!deck.getName().equals("Wished") && !deck.getName().equals("custom2")) {
                    deck.addPhysicalCard(targetPCard);
                }
                mockUserDeckMap.put(deck.getName(), deck);
            }

            Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
                put("test@test.it", mockUserDeckMap);
            }};
            DeckServiceImpl deckService = initializeDeckService(mockDeckMap);

            // expects
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times((3 * numOfCards) + 4);

            replay(mockConfig, mockCtx);
            Assertions.assertEquals(3, deckService.removePhysicalCardFromDeck("validToken", "Owned", targetPCard).size());
            verify(mockConfig, mockCtx);
        }

        @Test
        public void testAddPhysicalCardsToCustomDeckForValidParameters() throws ServletException, DeckNotFoundException, InputException, AuthException {
            // init mocks
            PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 0, Status.randomStatus(), "This is a valid description.");
            PhysicalCard mockPCard2 = new PhysicalCard(Game.MAGIC, 1, Status.randomStatus(), "This is a valid description.");
            Deck testDeck = new Deck("test", false);
            testDeck.addPhysicalCard(mockPCard1);
            testDeck.addPhysicalCard(mockPCard2);
            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", new HashMap<>() {{
                    put("Owned", new Deck("Owned", true));
                    put("Wished", new Deck("Wished", true));
                    put("test", testDeck);
                }});
            }});

            // expects
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(8);

            replay(mockConfig, mockCtx);
            List<PhysicalCardWithName> pCardsWithNames = deckService.addPhysicalCardsToCustomDeck("validToken", "test",
                    Arrays.asList(
                            new PhysicalCard(Game.MAGIC, 2, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.MAGIC, 3, Status.randomStatus(), "This is a valid description."),
                            new PhysicalCard(Game.MAGIC, 4, Status.randomStatus(), "This is a valid description."),
                            // duplicated cards
                            mockPCard1,
                            mockPCard2
                    ));
            verify(mockConfig, mockCtx);
            Assertions.assertAll(() -> {
                Assertions.assertEquals(5, pCardsWithNames.size());
                Assertions.assertEquals("Lightning Bolt", pCardsWithNames.get(0).getName());
            });
        }

        @Test
        public void testEditPhysicalCardForValidParameters() throws ServletException, InputException, ExistingProposalException, AuthException {
            Map<String, Deck> deckMap = new HashMap<>();

            // init mocks
            PhysicalCard mockPCard = new PhysicalCard(Game.randomGame(), 1111, Status.randomStatus(), "This is a valid description.");
            PhysicalCard editedPCard = mockPCard.copyWithModifiedStatusAndDescription(Status.randomStatus(), "This is a modified description");
            List<Deck> decks = Arrays.asList(
                    new Deck("Owned", true),
                    new Deck("Wished", true),
                    new Deck("test1", false),
                    new Deck("test2", false),
                    new Deck("test3", false)
            );

            // modify random number of decks
            Random r = new Random(42);
            int numOfCards = 4;
            int numOfDecksModified = 0;

            for (Deck deck : decks) {
                // load deck with dummy physical cards (#numOfCards)
                for (PhysicalCard pCard : DummyData.createPhysicalCardDummyList(numOfCards)) {
                    deck.addPhysicalCard(pCard);
                }
                // insert target physical card to edit in random number of decks (+1)
                if (!deck.getName().equals("Wished") && r.nextBoolean()) {
                    deck.addPhysicalCard(mockPCard);
                    numOfDecksModified++;
                }
                deckMap.put(deck.getName(), deck);
            }

            DeckServiceImpl deckService = initializeDeckService(new HashMap<>() {{
                put("test@test.it", deckMap);
            }});

            // expects
            // db call for token, check if physical card exists in a proposal, read and write
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(((numOfCards + 1) * numOfDecksModified) + 4);
            replay(mockConfig, mockCtx);
            List<ModifiedDeckPayload> modifiedDecks = deckService.editPhysicalCard("validToken", "Owned", editedPCard);
            verify(mockConfig, mockCtx);
            // number of decks modified should be equal of number of decks returned
            Assertions.assertEquals(numOfDecksModified, modifiedDecks.size());
        }
    }
}
