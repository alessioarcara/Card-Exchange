package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.services.CardServiceImpl;
import com.aadm.cardexchange.shared.models.*;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapdb.Serializer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;


public class CardServiceTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    private CardServiceImpl cardService;

    @BeforeEach
    public void initialize() throws ServletException {
        ctrl = createStrictControl();
        mockDB = ctrl.createMock(MapDB.class);
        cardService = new CardServiceImpl(mockDB);
        ServletConfig mockConfig = ctrl.createMock(ServletConfig.class);
        ServletContext mockCtx = ctrl.createMock(ServletContext.class);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        cardService.init(mockConfig);
    }

    private static Stream<Arguments> provideClassAndMockData() {
        return Stream.of(
                Arguments.of(Game.Magic, MagicCardDecorator.class, MockCardData.createMagicDummyMap()),
                Arguments.of(Game.Pokemon, PokemonCardDecorator.class, MockCardData.createPokemonDummyMap()),
                Arguments.of(Game.YuGiOh, YuGiOhCardDecorator.class, MockCardData.createYuGiOhDummyMap())
        );
    }

    private static Stream<Arguments> provideMockData() {
        return Stream.of(
                Arguments.of(Game.Magic, MockCardData.createMagicDummyMap()),
                Arguments.of(Game.Pokemon, MockCardData.createPokemonDummyMap()),
                Arguments.of(Game.YuGiOh, MockCardData.createYuGiOhDummyMap())
        );
    }

    @ParameterizedTest
    @MethodSource("provideClassAndMockData")
    public void testGetGameCardsListContainsCorrectSubClassesForGameParameter(Game game, Class<?> clazz, Map<Integer, CardDecorator> expectedMap) {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        List<CardDecorator> cards = cardService.getGameCards(game);
        ctrl.verify();
        Assertions.assertTrue(cards.stream().allMatch(card ->
                card.getClass() == clazz
        ));
    }

    @Test
    public void testGetGameCardsForNullParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> cardService.getGameCards(null));
    }

    @ParameterizedTest
    @MethodSource("provideMockData")
    public void testGetGameCardsExpectedListForGameParameter(Game game, Map<Integer, CardDecorator> expectedMap) {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        List<CardDecorator> cards = cardService.getGameCards(game);
        ctrl.verify();
        Assertions.assertEquals(new ArrayList<>(expectedMap.values()), cards);
    }

    @Test
    public void testGetGameCardForNullGameParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> cardService.getGameCard(null, 2));
    }

    @Test
    public void testGetGameCardForLessThan0CardIdParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> cardService.getGameCard(Game.Magic, -5));
    }

    @ParameterizedTest
    @MethodSource("provideMockData")
    public void testGetGameCardForIdParameter(Game game, Map<Integer, CardDecorator> expectedMap) {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        CardDecorator actualCard = cardService.getGameCard(game, 2);
        ctrl.verify();
        Assertions.assertEquals(expectedMap.get(2), actualCard);
    }

    @Test
    public void testGetNameCard(){
        Assertions.assertEquals("No Name Found", CardServiceImpl.getNameCard(Game.Magic, 1111, new HashMap<>()));
    }


}
