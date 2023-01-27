package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.AuthException;
import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.Status;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.easymock.EasyMock.*;

public class DeckServiceTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    ServletConfig mockConfig;
    ServletContext mockCtx;
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
        Map<String, Set<Deck>> deckMap = new HashMap<>();
        Set<Deck> mockDecks =  new LinkedHashSet<>();
        mockDecks.add(new Deck("test@test.it", "Owned"));
        mockDecks.add(new Deck("test@test.it", "Wished"));
        deckMap.put("test@test.it", mockDecks);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(deckMap);
        ctrl.replay();
        Assertions.assertFalse(deckService.createDefaultDecks("test@test.it"));
        ctrl.verify();
    }

    @Test
    public void testDefaultDeckCreation() {
        Map<String, Set<Deck>> deckMap = new HashMap<>();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(deckMap);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(deckMap);
        ctrl.replay();
        Assertions.assertTrue(deckService.createDefaultDecks("testbis@test.it"));
        ctrl.verify();
        // check if last default deck is "Wished"
        boolean isTrue = false;
        for (Deck deck: deckMap.get("testbis@test.it")) {
            if (deck.getName().equals("Wished")) {
                isTrue = true;
                break;
            }
        }
        Assertions.assertTrue(isTrue);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testAddPhysicalCardToDeckForInvalidToken(String input) {
        Map<String, LoginInfo> mockLoginMap = new HashMap<>(){{
            put("validToken1", new LoginInfo("test@test1.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test@test2.it", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginInfo("test@test3.it", System.currentTimeMillis() - 30000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockLoginMap);
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () ->
            deckService.addPhysicalCardToDeck(input, "Owned", 111, Status.Damaged, "This is a valid description.")
        );
        ctrl.verify();
    }

    private void setupForValidToken() {
        Map<String, LoginInfo> mockLoginMap = new HashMap<>(){{
            put("validToken", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockLoginMap);
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testAddPhysicalCardToDeckForInvalidDeckName(String input) {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            deckService.addPhysicalCardToDeck("validToken", input, 111, Status.Excellent, "This is a valid description.")
        );
        ctrl.verify();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void testAddPhysicalCardToDeckForInvalidCardId(int input) {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                deckService.addPhysicalCardToDeck("validToken", "Owned", input, Status.Excellent, "This is a valid description.")
        );
        ctrl.verify();
    }

    @Test
    public void testAddPhysicalCardToDeckForInvalidStatus() {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                deckService.addPhysicalCardToDeck("validToken", "Owned", 111, null, "This is a valid description.")
        );
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"not valid descr", "1234567890123456789", "1 3 5 7 9 1 3 5 7 9 "})
    public void testAddPhysicalCardToDeckForInvalidDescription(String input) {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                deckService.addPhysicalCardToDeck("validToken", "Owned", 111, Status.Excellent, input)
        );
        ctrl.verify();
    }

    @Test
    public void testAddPhysicalCardToDeckForNotExistingDecksSet() {
        setupForValidToken();
        Map<String, Set<Deck>> mockDeckMap = new HashMap<>(){{
            put("test@test.it", null);
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertThrows(RuntimeException.class, () ->
                deckService.addPhysicalCardToDeck("validToken", "Owned", 111, Status.Excellent, "This is a valid description.")
        );
        ctrl.verify();
    }

    @Test
    public void testAddPhysicalCardToDeckForNotExistingDeck() {
//        setupForValidToken();
//        ctrl.replay();
//
//        ctrl.verify();
    }

    @Test
    public void testAddPhysicalCardToDeckForNotAlreadyAddedCard() throws AuthException {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertFalse(deckService.addPhysicalCardToDeck("validToken", "Owned", 111, Status.Excellent, "This is a valid description."));
        ctrl.verify();
    }

    @Test
    public void testAddPhysicalCardToDeckForAlreadyAddedCard() {
//        setupForValidToken();
//        ctrl.replay();
//
//        ctrl.verify();
    }
}
