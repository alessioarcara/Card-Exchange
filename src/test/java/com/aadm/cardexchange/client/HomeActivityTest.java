package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.presenters.HomeActivity;
import com.aadm.cardexchange.client.views.HomeView;
import com.aadm.cardexchange.server.MockCardData;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class HomeActivityTest {
    IMocksControl ctrl;
    HomeView mockView;
    CardServiceAsync mockRpcService;
    PlaceController placeController;
    HomeActivity homeActivity;
    private final static List<String> MAGIC_BOOLEAN_FIELDS = Arrays.asList("hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint");
    private final static List<String> POKEMON_BOOLEAN_FIELDS = Arrays.asList("firstEdition", "holo", "normal", "reverse", "wPromo");

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
        homeActivity.goTo(new CardPlace(null, 1));
        ctrl.verify();
    }

    @Test
    public void testFetchGameCardsForNullParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeActivity.fetchGameCards(null));
    }

    @SuppressWarnings("unchecked")
    public void setupFetchGameCardsTest(Game game, List<Card> mockCards) {
        mockRpcService.getGameCards(eq(game), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<Card>> callback = (AsyncCallback<List<Card>>) args[args.length - 1];
            callback.onSuccess(mockCards);
            return null;
        });
        mockView.setData(mockCards);
        expectLastCall();
        ctrl.replay();
        homeActivity.fetchGameCards(game);
        ctrl.verify();
    }

    private static Stream<Arguments> provideMockCards() {
        return Stream.of(
                Arguments.of(Game.MAGIC, MockCardData.createMagicDummyList()),
                Arguments.of(Game.POKEMON, MockCardData.createPokemonDummyList()),
                Arguments.of(Game.YUGIOH, MockCardData.createYuGiOhDummyList())
        );
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFetchGameCardsExpectedListForGameParameter(Game game, List<Card> mockCards) {
        setupFetchGameCardsTest(game, mockCards);
    }

//    @ParameterizedTest
//    @MethodSource("provideMockCards")
//    public void testFilterGameCardsForAllParameters(Game game, List<Card> mockCards) {
//        setupFetchGameCardsTest(game, mockCards);
//        Assertions.assertArrayEquals(homeActivity.filterGameCards("all", "all",
//                "Name", "",
//                Collections.emptyList(), Collections.emptyList()).toArray(), mockCards.toArray());
//    }

//    @ParameterizedTest
//    @MethodSource("provideMockCards")
//    public void testFilterGameCardsForNameParameter(Game game, List<Card> mockCards) {
//        Card expectedCard = mockCards.get(mockCards.size() - 1);
//        setupFetchGameCardsTest(game, mockCards);
//        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
//                "Name", expectedCard.getName(),
//                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideMockCards")
//    public void testFilterGameCardsForDescriptionParameter(Game game, List<Card> mockCards) {
//        Card expectedCard = mockCards.get(mockCards.size() - 1);
//        setupFetchGameCardsTest(game, mockCards);
//        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
//                "Description", expectedCard.getDescription(),
//                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
//    }
//
//    private static Stream<Arguments> provideMockCardsAndBooleanFields() {
//        return Stream.of(
//                Arguments.of(
//                        Game.MAGIC, MockCardData.createMagicDummyList(), MockCardData.createMagicDummyMap().get(0),
//                        MAGIC_BOOLEAN_FIELDS, Arrays.asList(true, false, false, false, false)),
//                Arguments.of(
//                        Game.MAGIC, MockCardData.createMagicDummyList(), MockCardData.createMagicDummyMap().get(1),
//                        MAGIC_BOOLEAN_FIELDS, Arrays.asList(false, true, false, false, false)),
//                Arguments.of(
//                        Game.MAGIC, MockCardData.createMagicDummyList(), MockCardData.createMagicDummyMap().get(2),
//                        MAGIC_BOOLEAN_FIELDS, Arrays.asList(false, false, true, false, false)),
//                Arguments.of(
//                        Game.MAGIC, MockCardData.createMagicDummyList(), MockCardData.createMagicDummyMap().get(3),
//                        MAGIC_BOOLEAN_FIELDS, Arrays.asList(false, false, false, true, false)),
//                Arguments.of(
//                        Game.MAGIC, MockCardData.createMagicDummyList(), MockCardData.createMagicDummyMap().get(4),
//                        MAGIC_BOOLEAN_FIELDS, Arrays.asList(false, false, false, false, true)),
//                Arguments.of(
//                        Game.POKEMON, MockCardData.createPokemonDummyList(), MockCardData.createPokemonDummyList().get(0),
//                        POKEMON_BOOLEAN_FIELDS, Arrays.asList(true, false, false, false, false)),
//                Arguments.of(
//                        Game.POKEMON, MockCardData.createPokemonDummyList(), MockCardData.createPokemonDummyList().get(1),
//                        POKEMON_BOOLEAN_FIELDS, Arrays.asList(false, true, false, false, false)),
//                Arguments.of(
//                        Game.POKEMON, MockCardData.createPokemonDummyList(), MockCardData.createPokemonDummyList().get(2),
//                        POKEMON_BOOLEAN_FIELDS, Arrays.asList(false, false, true, false, false)),
//                Arguments.of(
//                        Game.POKEMON, MockCardData.createPokemonDummyList(), MockCardData.createPokemonDummyList().get(3),
//                        POKEMON_BOOLEAN_FIELDS, Arrays.asList(false, false, false, true, false)),
//                Arguments.of(
//                        Game.POKEMON, MockCardData.createPokemonDummyList(), MockCardData.createPokemonDummyList().get(4),
//                        POKEMON_BOOLEAN_FIELDS, Arrays.asList(false, false, false, false, true)),
//                Arguments.of(
//                        Game.YUGIOH, MockCardData.createYuGiOhDummyList(), MockCardData.createYuGiOhDummyList().get(0),
//                        Collections.emptyList(), Collections.emptyList())
//        );
//    }
//
//    @Test
//    public void testFilterGameCardsFor_Magic_and_Artist_Parameters() {
//        List<Card> mockCards = MockCardData.createMagicDummyList();
//        Card expectedCard = mockCards.get(mockCards.size() - 1);
//        setupFetchGameCardsTest(Game.MAGIC, mockCards);
//        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
//                "Artist", ((MagicCard) expectedCard).getArtist(),
//                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideMockCards")
//    public void testFilterGameCardsForSpecialAttributeParameter(Game game, List<Card> mockCards) {
//        Card expectedCard = mockCards.get(mockCards.size() - 1);
//        String specialAttribute = expectedCard instanceof MagicCard ?
//                ((MagicCard) expectedCard).getRarity() : expectedCard instanceof PokemonCard ?
//                ((PokemonCard) expectedCard).getRarity() : ((YuGiOhCard) expectedCard).getRace();
//
//        setupFetchGameCardsTest(game, mockCards);
//        Assertions.assertEquals(homeActivity.filterGameCards(specialAttribute, "all",
//                "Description", "",
//                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideMockCards")
//    public void testFilterGameCardsForTypeParameter(Game game, List<Card> mockCards) {
//        Card expectedCard = mockCards.get(mockCards.size() - 1);
//        setupFetchGameCardsTest(game, mockCards);
//        Assertions.assertEquals(homeActivity.filterGameCards("all", expectedCard.getType(),
//                "Description", "",
//                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
//    }
//
//    @Test
//    public void testFilterGameCardsFor_Pokemon_and_Artist_Parameters() {
//        List<Card> mockCards = MockCardData.createPokemonDummyList();
//        Card expectedCard = mockCards.get(0);
//        setupFetchGameCardsTest(Game.POKEMON, mockCards);
//        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
//                "Artist", ((PokemonCard) expectedCard).getArtist(),
//                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideMockCardsAndBooleanFields")
//    public void testFilterGameCardsForBooleanParameters(Game game, List<Card> mockCards, Card expectedCard,
//                                                        List<String> booleanNames, List<Boolean> booleanValues) {
//        setupFetchGameCardsTest(game, mockCards);
//        Assertions.assertEquals(homeActivity.filterGameCards("all", "all",
//                "Name", "",
//                booleanNames, booleanValues).get(0), expectedCard);
//    }
}
