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

    //#### SPECIFIC SETUP FOR TESTS #####
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
    public void testGetProposalListReceivedForEmptyProposalMap() throws BaseException {
        setupForValidToken();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListReceived("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForNoProposalForThisUser() throws BaseException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListReceived("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListReceivedForSuccess() throws BaseException {
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
    public void testGetProposalListSendForEmptyProposalMap() throws BaseException {
        setupForValidToken();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListSend("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListSendForNoProposalForThisUser() throws BaseException {
        setupForValidToken();
        setupForProposalMap();
        ctrl.replay();
        Assertions.assertTrue(exchangeService.getProposalListSend("validToken").isEmpty());
        ctrl.verify();
    }

    @Test
    public void testGetProposalListSendForSuccess() throws BaseException {
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