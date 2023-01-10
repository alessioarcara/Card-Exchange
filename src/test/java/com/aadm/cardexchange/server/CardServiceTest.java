package com.aadm.cardexchange.server;

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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;


public class CardServiceTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    private CardServiceImpl cardService;

    private static Stream<Arguments> provideMockData() {
        return Stream.of(
                Arguments.of(Game.Magic, MockCardData.createMagicDummyData()),
                Arguments.of(Game.Pokemon, MockCardData.createPokemonDummyData()),
                Arguments.of(Game.YuGiOh, MockCardData.createYuGiOhDummyData())
        );
    }

    private static Stream<Arguments> provideClassAndMockData() {
        return Stream.of(
                Arguments.of(Game.Magic, MagicCardDecorator.class, MockCardData.createMagicDummyData()),
                Arguments.of(Game.Pokemon, PokemonCardDecorator.class, MockCardData.createPokemonDummyData()),
                Arguments.of(Game.YuGiOh, YuGiOhCardDecorator.class, MockCardData.createYuGiOhDummyData())
        );
    }

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

    @Test
    public void testGetGameCardsForNullParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cardService.getGameCards(null);
        });
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
}
