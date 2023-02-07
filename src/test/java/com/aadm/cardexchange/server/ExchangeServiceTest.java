package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.services.ExchangeServiceImpl;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<PhysicalCard> generateValidListPcard(int n) {
        List<PhysicalCard> myList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            PhysicalCard mockPCard = new PhysicalCard(Game.randomGame(), (i + 3000), Status.randomGame(), "This is a valid description.");
            myList.add(mockPCard);
        }
        return myList;
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testAddProposalForInvalidToken(String input) {
        Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
            put("validToken1", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(loginInfoMap);
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.addProposal(input, "valid@receiverUserEmail.it", generateValidListPcard(2), generateValidListPcard(2)));
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testAddProposalForInvalidReceiverUserEmail(String input) {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", input, generateValidListPcard(2), generateValidListPcard(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForNotExistingReceiverUserEmail() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "NotExisting@Mail.it", generateValidListPcard(2), generateValidListPcard(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForEmptySenderPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(0), generateValidListPcard(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForNullSenderPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", null, generateValidListPcard(2)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForEmptyReceiverPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(2), generateValidListPcard(0)));
        ctrl.verify();
    }

    @Test
    public void testAddProposalForNullReceiverPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(2), null));
        ctrl.verify();
    }

    @Test
    public void testAddProposalSuccess() throws AuthException, InputException {
        setupForValidToken();
        setupForValidEmail();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(1), generateValidListPcard(2)));
        ctrl.verify();
    }


    // Test for getProposalCards();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testGetProposalCardsForInvalidUser(String input) throws AuthException {
        Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
            put("validToken1", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
            .andReturn(loginInfoMap);
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.getProposalCards(input, 0));
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
    public void testGetProposalCardsForSuccess() throws AuthException, InputException {
        setupForValidToken();

        PhysicalCard p1S = new PhysicalCard(Game.MAGIC, 111, Status.Good, "this is a valid description");
        PhysicalCard p2S = new PhysicalCard(Game.POKEMON, 222, Status.Good, "this is a valid description");

        PhysicalCard p1R = new PhysicalCard(Game.MAGIC, 111, Status.Excellent, "this is a valid description");
        PhysicalCard p3R = new PhysicalCard(Game.POKEMON, 333, Status.Damaged, "this is a valid description");

        Map<Integer, Proposal> proposalMap = new HashMap<>() {{
            put(0, new Proposal("sender@test.it", "receiver@test.it",
                    new ArrayList<>() {{ add(p1S); add(p2S); }},
                    new ArrayList<>(){{ add(p1R); add(p3R); }}
            ));
        }};

        Map<String, List<PhysicalCardWithName>> cards = new HashMap<>() {{
            put("Sender", new ArrayList<>(){{
                add(new PhysicalCardWithName(p1S, "name1"));
                add(new PhysicalCardWithName(p2S, "name2"));
            }});
            put("Receiver", new ArrayList<>(){{
                add(new PhysicalCardWithName(p1R, "name1"));
                add(new PhysicalCardWithName(p3R, "name3"));
            }});
        }};

        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(proposalMap);

        ctrl.replay();
        Assertions.assertDoesNotThrow(() -> exchangeService.getProposalCards("validToken", 0));
        ctrl.verify();
    }
}
