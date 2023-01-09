package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.CardImpl;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.Serializer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

/*
 * Create a mock ServletConfig, mock its getServletContext() method to return your mocked ServletContext.
 * and use the other init method: init(servletConfig).
 * Then you don't need to mock the getServletContext() method of your servlet.
 */

public class ExampleRPCTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    private CardServiceImpl rpcService;

    @BeforeEach
    protected void initialize() throws Exception {
        ctrl = createStrictControl();
        mockDB = ctrl.createMock(MapDB.class);
        rpcService = new CardServiceImpl(mockDB);
        ServletConfig mockConfig = ctrl.createMock(ServletConfig.class);
        ServletContext mockCtx = ctrl.createMock(ServletContext.class);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        rpcService.init(mockConfig);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetCards() {
        Map<Integer, Card> expectedCards = new HashMap<Integer, Card>() {
            {
                put(0, new CardImpl("name0", "desc0", "type0"));
                put(1, new CardImpl("name1", "desc1", "type1"));
                put(2, new CardImpl("name2", "desc2", "type2"));
            }
        };
        expect(mockDB.getMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedCards);

        ctrl.replay();

        Card[] cards = rpcService.getCards();

        ctrl.verify();
        Assertions.assertEquals(3, cards.length);
    }
}
