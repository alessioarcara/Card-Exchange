package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.services.ExchangeServiceImpl;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
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
import java.text.SimpleDateFormat;
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
            put("validToken2", new LoginInfo("UserMail4", System.currentTimeMillis() - 10000));
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

    private  void  generateLoginInfoMap(){
        Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
            put("validToken1", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(loginInfoMap);
    }

    private void setupForProposalMap(){
        String date = new SimpleDateFormat("dd-MM-yy").format(new Date());
        Map<Integer, Proposal> proposalMap = new HashMap<>() {{
            Proposal p1 = new Proposal("UserMail1", "UserMail2", new ArrayList<>(), new ArrayList<>(), date);
            put(p1.getId(),p1);
            Proposal p2 = new Proposal("UserMail1", "UserMail3", new ArrayList<>(), new ArrayList<>(), date);
            put(p2.getId(),p2);
            Proposal p3 = new Proposal("UserMail1", "UserMail4", new ArrayList<>(), new ArrayList<>(), date);
            put(p3.getId(),p3);
            Proposal p4 = new Proposal("UserMail2", "UserMail3", new ArrayList<>(), new ArrayList<>(), date);
            put(p4.getId(),p4);
            Proposal p5 = new Proposal("UserMail2", "UserMail4", new ArrayList<>(), new ArrayList<>(), date);
            put(p5.getId(),p5);
            Proposal p6 = new Proposal("UserMail3", "UserMail4", new ArrayList<>(), new ArrayList<>(), date);
            put(p6.getId(),p6);

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
    public void testAddProposalSuccess() throws BaseException {
        setupForValidToken();
        setupForValidEmail();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(1), generateValidListPcard(2)));
        ctrl.verify();
    }
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testGetProposalListReceivedForInvalidToken(String input) {
        generateLoginInfoMap();
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.GetProposalListReceived(input));
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForEmptyProposalMap() throws BaseException {
        setupForValidToken();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.GetProposalListReceived("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForNoProposalForThisUser() throws BaseException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        Assertions.assertTrue(exchangeService.GetProposalListReceived("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForSuccess() throws BaseException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        List<Proposal>  proposalList = exchangeService.GetProposalListReceived("validToken2");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(3, proposalList.size());
            Assertions.assertEquals("UserMail4", proposalList.get(0).getReceiverUserEmail());
        });
    }
}
