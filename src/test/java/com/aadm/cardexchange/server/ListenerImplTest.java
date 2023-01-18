package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.CardDecorator;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class ListenerImplTest {
    MapDB mockDB;
    ServletContext mockCtx;

    @BeforeEach
    public void initialize() {
        mockDB = EasyMock.createStrictMock(MapDB.class);
        mockCtx = createStrictMock(ServletContext.class);
    }

    @Test
    public void testContextInitializedForIncorrectFile() {
        ServletContextListener listener = new ListenerImpl(mockDB, "");

        Map<Integer, CardDecorator> expectedMap = new HashMap<>();
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap).times(3);
        replay(mockDB);

        listener.contextInitialized(new ServletContextEvent(mockCtx));

        verify(mockDB);

        Assertions.assertEquals(0, expectedMap.size());
    }

    @Test
    public void testContextInitializedForJSONFiles() {
        ServletContextListener listener = new ListenerImpl(mockDB, "src/main/resources/json/");

        Map<Integer, CardDecorator> yugiohMap = new HashMap<>();
        Map<Integer, CardDecorator> magicMap = new HashMap<>();
        Map<Integer, CardDecorator> pokemonMap = new HashMap<>();

        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(yugiohMap);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(magicMap);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(pokemonMap);
        replay(mockDB);

        listener.contextInitialized(new ServletContextEvent(mockCtx));

        verify(mockDB);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(200, yugiohMap.size());
            Assertions.assertEquals(201, magicMap.size());
            Assertions.assertEquals(200, pokemonMap.size());
        });
    }

    @Test
    public void testcontextDestroyed() {
        ServletContextListener listener = new ListenerImpl();
        Assertions.assertDoesNotThrow(() -> {
            listener.contextDestroyed(new ServletContextEvent(mockCtx));
        });
    }
}
