package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.presenters.HomeActivity;
import com.aadm.cardexchange.client.views.HomeView;
import com.aadm.cardexchange.server.MockCardData;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class HomeActivityTest {
    IMocksControl ctrl;
    HomeView mockView;
    CardServiceAsync mockRpcService;
    HomeActivity homeActivity;

    private static Stream<Arguments> provideMockData() {
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
        PlaceController placeController = ctrl.createMock(PlaceController.class);
        homeActivity = new HomeActivity(mockView, mockRpcService, placeController);
    }

    @Test
    public void testFetchGameCardsForNullParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeActivity.fetchGameCards(null));
    }

    @ParameterizedTest
    @MethodSource("provideMockData")
    @SuppressWarnings("unchecked")
    public void testFetchGameCardsExpectedListForGameParameter(Game game, List<CardDecorator> expectedList) {

        mockRpcService.getGameCards(eq(game), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<CardDecorator>> callback = (AsyncCallback<List<CardDecorator>>) args[args.length - 1];
            callback.onSuccess(expectedList);
            return null;
        });

        mockView.setData(expectedList);
        expectLastCall();

        ctrl.replay();

        homeActivity.fetchGameCards(game);

        ctrl.verify();

    }
}
