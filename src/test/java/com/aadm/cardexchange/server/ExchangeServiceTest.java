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

    //#### SPECIFIC SETUP FOR TESTS #####
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

    private PhysicalCard generateValidPCard(int i) {
        return new PhysicalCard(Game.randomGame(), (i+3000), Status.randomStatus(), "This is a valid description.");
    }


    private  List<PhysicalCard> generateValidListPcard(int n) {
        List<PhysicalCard> myList = new ArrayList<>();
        for (int i = 0; i<n; i++ ) {
            PhysicalCard mockPCard = generateValidPCard(i);
            myList.add(mockPCard);
        }
        return myList;
    }




    //#### UNIT TESTS #####

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
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", input, generateValidListPcard(2), generateValidListPcard(2));
        });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForNotExistingReceiverUserEmail() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "NotExisting@Mail.it", generateValidListPcard(2), generateValidListPcard(2));
        });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForEmptySenderPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
                exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(0), generateValidListPcard(2));
            });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForNullSenderPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", null, generateValidListPcard(2));
        });
        ctrl.verify();
    }

    @Test
    public void testAddProposalForEmptyReceiverPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(2), generateValidListPcard(0));
        });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForNullReceiverPCards() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(2), null);
        });
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
}