package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.Deck;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}