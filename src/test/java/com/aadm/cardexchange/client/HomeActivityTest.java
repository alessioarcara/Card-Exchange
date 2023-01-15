package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.presenters.HomeActivity;
import com.aadm.cardexchange.client.views.HomeView;
import com.aadm.cardexchange.server.MockCardData;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class HomeActivityTest {
    IMocksControl ctrl;
    HomeView mockView;
    CardServiceAsync mockRpcService;
    PlaceController placeController;
    HomeActivity homeActivity;

    private static Stream<Arguments> provideMockCards() {
        return Stream.of(
                Arguments.of(Game.Magic, MockCardData.createMagicDummyList()),
                Arguments.of(Game.Pokemon, MockCardData.createPokemonDummyList()),
                Arguments.of(Game.YuGiOh, MockCardData.createYuGiOhDummyList())
        );
    }

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockView = ctrl.createMock(HomeView.class);
        mockRpcService = ctrl.createMock(CardServiceAsync.class);
        placeController = ctrl.createMock(PlaceController.class);
        homeActivity = new HomeActivity(mockView, mockRpcService, placeController);
    }

    @Test
    public void testForGoTo() {
        placeController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        homeActivity.goTo(new CardPlace());
        ctrl.verify();
    }

    @Test
    public void testFetchGameCardsForNullParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeActivity.fetchGameCards(null));
    }

    @SuppressWarnings("unchecked")
    public void setupFetchGameCardsTest(Game game, List<CardDecorator> mockCards) {
        mockRpcService.getGameCards(eq(game), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<CardDecorator>> callback = (AsyncCallback<List<CardDecorator>>) args[args.length - 1];
            callback.onSuccess(mockCards);
            return null;
        });
        mockView.setData(mockCards);
        expectLastCall();
        ctrl.replay();
        homeActivity.fetchGameCards(game);
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFetchGameCardsExpectedListForGameParameter(Game game, List<CardDecorator> mockCards) {
        setupFetchGameCardsTest(game, mockCards);
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForAllParameters(Game game, List<CardDecorator> mockCards) {
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertArrayEquals(homeActivity.filterGameCards("all", "all",
                "Name", "",
                Collections.emptyList(), Collections.emptyList()).toArray(), mockCards.toArray());
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForNameParameter(Game game, List<CardDecorator> mockCards) {
        CardDecorator expectedCard = mockCards.get(mockCards.size() - 1);
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
                "Name", expectedCard.getName(),
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForDescriptionParameter(Game game, List<CardDecorator> mockCards) {
        CardDecorator expectedCard = mockCards.get(mockCards.size() - 1);
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
                "Description", expectedCard.getDescription(),
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @Test
    public void testFilterGameCardsFor_Magic_and_Artist_Parameters() {
        List<CardDecorator> mockCards = MockCardData.createMagicDummyList();
        CardDecorator expectedCard = mockCards.get(mockCards.size() - 1);
        setupFetchGameCardsTest(Game.Magic, mockCards);
        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
                "Artist", ((MagicCardDecorator) expectedCard).getArtist(),
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @Test
    public void testFilterGameCardsFor_Pokemon_and_Artist_Parameters() {
        List<CardDecorator> mockCards = MockCardData.createPokemonDummyList();
        CardDecorator expectedCard = mockCards.get(0);
        setupFetchGameCardsTest(Game.Pokemon, mockCards);
        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
                "Artist", ((PokemonCardDecorator) expectedCard).getArtist(),
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForSpecialAttributeParameter(Game game, List<CardDecorator> mockCards) {
        CardDecorator expectedCard = mockCards.get(mockCards.size() - 1);
        String specialAttribute = expectedCard instanceof MagicCardDecorator ?
                ((MagicCardDecorator) expectedCard).getRarity() : expectedCard instanceof PokemonCardDecorator ?
                ((PokemonCardDecorator) expectedCard).getRarity() : ((YuGiOhCardDecorator) expectedCard).getRace();

        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filterGameCards(specialAttribute, "all",
                "Description", "",
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForTypeParameter(Game game, List<CardDecorator> mockCards) {
        CardDecorator expectedCard = mockCards.get(mockCards.size() - 1);
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filterGameCards("all", expectedCard.getType(),
                "Description", "",
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }
}
