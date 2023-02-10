package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.services.ExchangeServiceImpl;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.Proposal;
import com.aadm.cardexchange.shared.models.User;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

public class ExchangeServiceTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    ServletConfig mockConfig;
    ServletContext mockCtx;
    private ExchangeServiceImpl exchangeService;

    @BeforeEach
    public void initialize() throws ServletException {
        ctrl = createStrictControl();
        mockDB = ctrl.createMock(MapDB.class);
        exchangeService = new ExchangeServiceImpl(mockDB);
        mockConfig = ctrl.createMock(ServletConfig.class);
        mockCtx = ctrl.createMock(ServletContext.class);
        exchangeService.init(mockConfig);
    }

    private void setupForValidToken() {
        Map<String, LoginInfo> mockLoginMap = new HashMap<>() {{
            put("validToken", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("UserMail3", System.currentTimeMillis() - 10000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockLoginMap);
    }

    private void setupForValidEmail() {
        Map<String, User> userMap = new HashMap<>();
        userMap.put("valid@receiverUserEmail.it", new User("valid@receiverUserEmail.it", "password"));
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(userMap);
    }

    private void generateLoginInfoMap() {
        Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
            put("validToken1", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(loginInfoMap);
    }

    private void setupForProposalMap() {
        Map<Integer, Proposal> proposalMap = new HashMap<>() {{
            Proposal p1 = new Proposal("UserMail1", "UserMail2", new ArrayList<>(), new ArrayList<>());
            put(p1.getId(), p1);
            Proposal p2 = new Proposal("UserMail1", "UserMail3", new ArrayList<>(), new ArrayList<>());
            put(p2.getId(), p2);
            Proposal p3 = new Proposal("UserMail1", "UserMail4", new ArrayList<>(), new ArrayList<>());
            put(p3.getId(), p3);
            Proposal p4 = new Proposal("UserMail2", "UserMail3", new ArrayList<>(), new ArrayList<>());
            put(p4.getId(), p4);
            Proposal p5 = new Proposal("UserMail2", "UserMail4", new ArrayList<>(), new ArrayList<>());
            put(p5.getId(), p5);
            Proposal p6 = new Proposal("UserMail3", "UserMail4", new ArrayList<>(), new ArrayList<>());
            put(p6.getId(), p6);

        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(proposalMap);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testAddProposalForInvalidToken(String input) {
        generateLoginInfoMap();
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.addProposal(input, "valid@receiverUserEmail.it", DummyData.createPhysicalCardDummyList(2), DummyData.createPhysicalCardDummyList(2)));
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testAddProposalForInvalidReceiverUserEmail(String input) {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", input, DummyData.createPhysicalCardDummyList(2), DummyData.createPhysicalCardDummyList(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForNotExistingReceiverUserEmail() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "NotExisting@Mail.it", DummyData.createPhysicalCardDummyList(2), DummyData.createPhysicalCardDummyList(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForEmptySenderPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", DummyData.createPhysicalCardDummyList(0), DummyData.createPhysicalCardDummyList(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForNullSenderPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", null, DummyData.createPhysicalCardDummyList(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForEmptyReceiverPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", DummyData.createPhysicalCardDummyList(2), DummyData.createPhysicalCardDummyList(0)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForNullReceiverPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", DummyData.createPhysicalCardDummyList(2), null));
        ctrl.verify();
    }

    @Test
    public void testAddProposalSuccess() throws BaseException {
        setupForValidToken();
        setupForValidEmail();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", DummyData.createPhysicalCardDummyList(1), DummyData.createPhysicalCardDummyList(2)));
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testGetProposalListReceivedForInvalidToken(String input) {
        generateLoginInfoMap();
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.getProposalListSend(input));
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForEmptyProposalMap() throws AuthException {
        setupForValidToken();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListReceived("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForNoProposalForThisUser() throws AuthException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListReceived("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForSuccess() throws AuthException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        List<Proposal> proposalList = exchangeService.getProposalListReceived("validToken2");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(2, proposalList.size());
            Assertions.assertEquals("UserMail3", proposalList.get(0).getReceiverUserEmail());
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testGetProposalListSendForInvalidToken(String input) {
        generateLoginInfoMap();
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.getProposalListSend(input));
        ctrl.verify();
    }

    @Test
    public void testGetProposalListSendForEmptyProposalMap() throws AuthException {
        setupForValidToken();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListSend("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListSendForNoProposalForThisUser() throws AuthException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListSend("validToken").isEmpty());
        ctrl.verify();
    }


    // Test for getProposalCards();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testGetProposalCardsForInvalidUserToken(String input) throws AuthException {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.getProposalCards(input, 0));
        ctrl.verify();
    }

    @Test
    public void testGetProposalCardsForNotAllowedUser() throws AuthException {
        Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
            put("validToken1", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(loginInfoMap);

        // Setup physical card for proposal map and return object
        PhysicalCard p1S = new PhysicalCard(Game.POKEMON, 111, Status.Good, "this is a valid description");
        PhysicalCard p2S = new PhysicalCard(Game.YUGIOH, 222, Status.Good, "this is a valid description");

        PhysicalCard p1R = new PhysicalCard(Game.POKEMON, 111, Status.Excellent, "this is a valid description");
        PhysicalCard p3R = new PhysicalCard(Game.POKEMON, 333, Status.Damaged, "this is a valid description");

        Map<Integer, Proposal> proposalMap = new HashMap<>() {{
            put(0, new Proposal("sender@test.it", "receiver@test.it",
                    new ArrayList<PhysicalCard>() {{ add(p1S); add(p2S); }},
                    new ArrayList<PhysicalCard>(){{ add(p1R); add(p3R); }}
            ));
        }};

        // setup expected calls
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(proposalMap);

        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.getProposalCards("validToken1", 0));
        ctrl.verify();
    }

    @Test
    public void testGetProposalCardsForInvalidProposalId() throws InputException, AuthException {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.getProposalCards("validToken", -1));
        ctrl.verify();
    }

    @Test
    public void testGetProposalCardsForEmptyProposalMap() throws InputException, AuthException {
        setupForValidToken();
        Map<Integer, Proposal> proposalMap = new HashMap<>();

        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(proposalMap);

        ctrl.replay();
        Assertions.assertThrows(RuntimeException.class, () -> exchangeService.getProposalCards("validToken", 0));
        ctrl.verify();
    }

    @Test
    public void testGetProposalCardsForSuccess() throws AuthException, InputException {
        setupForValidToken();

        Card mockCard1 = MockCardData.createPokemonDummyCard();
        Card mockCard2 = MockCardData.createYuGiOhDummyCard();
        Card mockCard3 = MockCardData.createPokemonDummyCard();

        // Setup physical card for proposal map and return object
        PhysicalCard p1S = new PhysicalCard(Game.POKEMON, mockCard1.getId(), Status.Good, "this is a valid description");
        PhysicalCard p2S = new PhysicalCard(Game.YUGIOH, mockCard2.getId(), Status.Good, "this is a valid description");

        PhysicalCard p1R = new PhysicalCard(Game.POKEMON, mockCard1.getId(), Status.Excellent, "this is a valid description");
        PhysicalCard p3R = new PhysicalCard(Game.POKEMON, mockCard3.getId(), Status.Damaged, "this is a valid description");

        Map<Integer, Proposal> proposalMap = new HashMap<>() {{
            put(0, new Proposal("test@test.it", "receiver@test.it",
                    new ArrayList<PhysicalCard>() {{ add(p1S); add(p2S); }},
                    new ArrayList<PhysicalCard>(){{ add(p1R); add(p3R); }}
            ));
        }};

        ProposalPayload expectedPayload = new ProposalPayload("receiver@test.it",
                new LinkedList<PhysicalCardWithName>(){{
                    add(new PhysicalCardWithName(p1S, mockCard1.getName()));
                    add(new PhysicalCardWithName(p2S, mockCard2.getName()));
                }},
                new LinkedList<PhysicalCardWithName>(){{
                    add(new PhysicalCardWithName(p1R, mockCard1.getName()));
                    add(new PhysicalCardWithName(p3R, mockCard3.getName()));
                }}
        );

        // create the mock for the card Map
        Map<Integer, Card> cardMap = new HashMap<>() {{
            put(mockCard1.getId(), mockCard1);
            put(mockCard2.getId(), mockCard2);
            put(mockCard3.getId(), mockCard3);
        }};

        // setup expected calls
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(proposalMap);

        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(cardMap);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(cardMap);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(cardMap);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(cardMap);

        ctrl.replay();
        ProposalPayload proposalPayload = exchangeService.getProposalCards("validToken", 0);
        ctrl.verify();

        Assertions.assertAll( () -> {
            Assertions.assertNotNull(proposalPayload);
            Assertions.assertEquals(expectedPayload.getReceiverEmail(), proposalPayload.getReceiverEmail());
            Assertions.assertEquals(expectedPayload.getSenderCards(), proposalPayload.getSenderCards());
            Assertions.assertEquals(expectedPayload.getReceiverCards(), proposalPayload.getReceiverCards());
        });
    }

    @Test
    public void testGetProposalListSendForSuccess() throws AuthException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        List<Proposal> proposalList = exchangeService.getProposalListSend("validToken2");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1, proposalList.size());
            Assertions.assertEquals("UserMail3", proposalList.get(0).getSenderUserEmail());
        });
    }
}
