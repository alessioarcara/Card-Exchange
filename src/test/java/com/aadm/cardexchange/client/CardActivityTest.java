package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.AuthSubject.AuthSubject;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.presenters.CardActivity;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class CardActivityTest {
    private static final int CARD_ID = 111;
    IMocksControl ctrl;
    CardPlace mockPlace;
    CardView mockView;
    CardServiceAsync mockCardService;
    DeckServiceAsync mockDeckService;
    CardActivity cardActivity;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockPlace = new CardPlace(Game.Magic, CARD_ID);
        mockView = ctrl.createMock(CardView.class);
        mockCardService = ctrl.mock(CardServiceAsync.class);
        mockDeckService = ctrl.mock(DeckServiceAsync.class);
        cardActivity = new CardActivity(mockPlace, mockView, mockCardService, mockDeckService, new AuthSubject(null));
    }

    @Test
    public void testUpdate() {
        mockView.createUserWidgets(anyBoolean());
        expectLastCall();
        ctrl.replay();
        cardActivity.update();
        ctrl.verify();
    }

    @Test
    public void testFetchCardForOnSuccess() {
        CardDecorator cardDecorator = new CardDecorator(new CardImpl("Charizard", "Un pokemon di fuoco", "Fuoco"));
        mockCardService.getGameCard(isA(Game.class), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<CardDecorator> callback = (AsyncCallback<CardDecorator>) args[args.length - 1];
            callback.onSuccess(cardDecorator);
            return null;
        });
        mockView.setData(cardDecorator);
        expectLastCall();
        ctrl.replay();
        cardActivity.fetchCard();
        ctrl.verify();
    }

    private static Stream<Arguments> provideDifferentTypeOfErrors() {
        return Stream.of(
                Arguments.of(new AuthException()),
                Arguments.of(new IllegalArgumentException("Invalid description.")),
                Arguments.of(new RuntimeException())
        );
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testAddCardToDeckForOnFailure(Exception error) {
        mockDeckService.addPhysicalCardToDeck(anyString(), isA(Game.class), anyString(), anyInt(), isA(Status.class), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(error);
            return null;
        });
        mockView.displayErrorAlert(anyString());
        ctrl.replay();
        cardActivity.addCardToDeck("Owned", "1", "This is a valid description.");
        ctrl.verify();
    }

    @Test
    public void testAddCardToDeckForOnSuccessTrue() {
        mockDeckService.addPhysicalCardToDeck(anyString(), isA(Game.class), anyString(), anyInt(), isA(Status.class), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });
        mockView.displaySuccessAlert();
        mockView.hideModal();
        ctrl.replay();
        cardActivity.addCardToDeck("Owned", "1", "This is a valid description.");
        ctrl.verify();
    }

    @Test
    public void testAddCardToDeckForOnSuccessFalse() {
        mockDeckService.addPhysicalCardToDeck(anyString(), isA(Game.class), anyString(), anyInt(), isA(Status.class), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });
        mockView.displayErrorAlert(anyString());
        ctrl.replay();
        cardActivity.addCardToDeck("Owned", "1", "This is a valid description.");
        ctrl.verify();
    }
}
