package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.presenters.NewExchangeActivity;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class NewExchangeActivityTest {
    private final String RECEIVER_USER_EMAIL = "test@test.it";
    private final String SELECTED_PCARD_ID = "y132154654";
    IMocksControl ctrl;
    NewExchangePlace mockPlace;
    NewExchangeView mockView;
    PlaceController placeController;
    NewExchangeActivity newExchangeActivity;
    ExchangeServiceAsync mockExchangeService;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        NewExchangePlace mockPlace = new NewExchangePlace("y132154654", "receiver@test.it");
        mockView = ctrl.createMock(NewExchangeView.class);
        DeckServiceAsync mockDeckService = ctrl.createMock(DeckServiceAsync.class);
        mockExchangeService = ctrl.createMock(ExchangeServiceAsync.class);
        AuthSubject mockAuthSubject = new AuthSubject();
        mockAuthSubject.setCredentials("token", "sender@test.it");
        placeController = ctrl.createMock(PlaceController.class);
        newExchangeActivity = new NewExchangeActivity(mockPlace, mockView, mockDeckService, mockExchangeService, mockAuthSubject, placeController);
    }

    @Test
    public void testForGoTo() {
        placeController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        newExchangeActivity.goTo(new NewExchangePlace("y123123", "test@test.it"));
        ctrl.verify();
    }

    @Test
    public void testCreateProposalForInvalidLists() {
        List<PhysicalCard> senderList = new ArrayList<>(){{ add(new PhysicalCard(Game.MAGIC, 111, Status.Fair, "this is a valid description.")); }};
        List<PhysicalCard> receiverList = new ArrayList<>(){{ add(new PhysicalCard(Game.YUGIOH, 222, Status.Good, "this is a valid description.")); }};

        mockView.showAlert(anyString());
        expectLastCall().times(3);
        ctrl.replay();
        Assertions.assertAll(() -> {
            newExchangeActivity.createProposal(Collections.emptyList(), Collections.emptyList());
            newExchangeActivity.createProposal(senderList, Collections.emptyList());
            newExchangeActivity.createProposal(Collections.emptyList(), receiverList);
        });
        ctrl.verify();
    }

    private static Stream<Arguments> provideDifferentTypeOfErrors() {
        return Stream.of(
                Arguments.of(new AuthException("Invalid token")),
                Arguments.of(new InputException("Invalid receiver email")),
                Arguments.of(new RuntimeException("Internal server error"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testCreateProposalForFailure(Exception error) {
        // init
        List<PhysicalCard> senderList = new ArrayList<>(){{ add(new PhysicalCard(Game.MAGIC, 111, Status.Fair, "this is a valid description.")); }};
        List<PhysicalCard> receiverList = new ArrayList<>(){{ add(new PhysicalCard(Game.YUGIOH, 222, Status.Good, "this is a valid description.")); }};

        // expects
        mockExchangeService.addProposal(anyString(), anyString(), isA(List.class), isA(List.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(error);
            return null;
        });
        mockView.showAlert(anyString());

        ctrl.replay();
        newExchangeActivity.createProposal(senderList, receiverList);
        ctrl.verify();
    }

    @Test
    public void testCreateProposalForFalseResult() {
        // init
        List<PhysicalCard> senderList = new ArrayList<>(){{ add(new PhysicalCard(Game.MAGIC, 111, Status.Fair, "this is a valid description.")); }};
        List<PhysicalCard> receiverList = new ArrayList<>(){{ add(new PhysicalCard(Game.YUGIOH, 222, Status.Good, "this is a valid description.")); }};

        // expects
        mockExchangeService.addProposal(anyString(), anyString(), isA(List.class), isA(List.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });
        mockView.showAlert(anyString());

        ctrl.replay();
        newExchangeActivity.createProposal(senderList, receiverList);
        ctrl.verify();
    }

    @Test
    public void testCreateProposalForTrueResult() {
        // init
        List<PhysicalCard> senderList = new ArrayList<>(){{ add(new PhysicalCard(Game.MAGIC, 111, Status.Fair, "this is a valid description.")); }};
        List<PhysicalCard> receiverList = new ArrayList<>(){{ add(new PhysicalCard(Game.YUGIOH, 222, Status.Good, "this is a valid description.")); }};

        // expects
        mockExchangeService.addProposal(anyString(), anyString(), isA(List.class), isA(List.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });
        placeController.goTo(isA(Place.class));

        ctrl.replay();
        newExchangeActivity.createProposal(senderList, receiverList);
        ctrl.verify();
    }
}
