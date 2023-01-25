package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.DeckException;
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

import static org.easymock.EasyMock.*;

public class DeckServiceTest {
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
    public void testDeckException() {
        try {
            throw new DeckException("test");
        } catch (DeckException e) {
            Assertions.assertEquals("test", e.getErrorMessage());
        }
    }

    @Test
    public void testIfDeckAlreadyExist() throws DeckException {
        Map<String, LinkedHashSet<Deck>> deckMap = new HashMap<>();
        LinkedHashSet mockDecks = new LinkedHashSet<Deck>();
        mockDecks.add(new Deck("test@test.it", "Owned"));
        mockDecks.add(new Deck("test@test.it", "Wished"));
        deckMap.put("test@test.it", mockDecks);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(deckMap);
        ctrl.replay();
        Assertions.assertThrows(DeckException.class, () -> deckService.createDefaultDecks("test@test.it"));
        ctrl.verify();
    }

    @Test
    public void testDefaultDeckCreation() throws DeckException {
        Map<String, LinkedHashSet<Deck>> deckMap = new HashMap<>();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(deckMap);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(deckMap);
        ctrl.replay();
        Assertions.assertTrue(deckService.createDefaultDecks("testbis@test.it"));
        //deckService.createDefaultDecks("test@test.it");
        ctrl.verify();
    }
}