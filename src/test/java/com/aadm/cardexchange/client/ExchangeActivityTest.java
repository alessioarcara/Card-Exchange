package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.ExchangesPlace;
import com.aadm.cardexchange.client.presenters.ExchangeActivity;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.exceptions.ProposalNotFoundException;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class ExchangeActivityTest {
    IMocksControl ctrl;
    NewExchangeView mockView;
    PlaceController placeController;
    ExchangeActivity exchangeActivity;
    ExchangeServiceAsync mockExchangeService;

    private static Stream<Arguments> provideDifferentTypeOfErrorsForRefuse() {
        return Stream.of(
                Arguments.of(new AuthException("Invalid token")),
                Arguments.of(new InputException("Invalid proposal Id")),
                Arguments.of(new ProposalNotFoundException("Not existing proposal")),
                Arguments.of(new RuntimeException("Internal server error"))
        );
    }

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        ExchangesPlace mockPlace = new ExchangesPlace(0);
        mockView = ctrl.createMock(NewExchangeView.class);
        mockExchangeService = ctrl.createMock(ExchangeServiceAsync.class);
        AuthSubject mockAuthSubject = new AuthSubject();
        mockAuthSubject.setCredentials("token", "test@test.it");
        placeController = ctrl.createMock(PlaceController.class);
        exchangeActivity = new ExchangeActivity(mockPlace, mockView, mockExchangeService, mockAuthSubject, placeController);
    }

    @Test
    public void testForGoTo() {
        placeController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        exchangeActivity.goTo(new ExchangesPlace(null));
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrorsForRefuse")
    public void testRefuseOrWithdrawProposalForFailure(Exception error) {
        // expects
        mockExchangeService.refuseOrWithdrawProposal(anyString(), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(error);
            return null;
        });
        mockView.showAlert(anyString());

        ctrl.replay();
        exchangeActivity.refuseOrWithdrawProposal();
        ctrl.verify();
    }

    @Test
    public void testRefuseOrWithdrawProposalForFalseResult() {
        // expects
        mockExchangeService.refuseOrWithdrawProposal(anyString(), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });
        mockView.showAlert(anyString());

        ctrl.replay();
        exchangeActivity.refuseOrWithdrawProposal();
        ctrl.verify();
    }

    @Test
    public void testRefuseOrWithdrawProposalForTrueResult() {
        // expects
        mockExchangeService.refuseOrWithdrawProposal(anyString(), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });
        mockView.showAlert(anyString());
        placeController.goTo(isA(Place.class));

        ctrl.replay();
        exchangeActivity.refuseOrWithdrawProposal();
        ctrl.verify();
    }

    private static Stream<Arguments> provideDifferentTypeOfErrorsForAccept() {
        return Stream.of(
                Arguments.of(new AuthException("Invalid token")),
                Arguments.of(new ProposalNotFoundException("Not existing proposal")),
                Arguments.of(new RuntimeException("Internal server error"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrorsForAccept")
    public void testAcceptProposalForFailure(Exception error) {
        // expects
        mockExchangeService.acceptProposal(anyString(), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(error);
            return null;
        });
        mockView.showAlert(anyString());

        ctrl.replay();
        exchangeActivity.acceptExchangeProposal();
        ctrl.verify();
    }

    @Test
    public void testAcceptProposalForFalseResult() {
        // expects
        mockExchangeService.acceptProposal(anyString(), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });
        mockView.showAlert(anyString());

        ctrl.replay();
        exchangeActivity.acceptExchangeProposal();
        ctrl.verify();
    }

    @Test
    public void testAcceptProposalForTrueResult() {
        // expects
        mockExchangeService.acceptProposal(anyString(), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });
        mockView.showAlert(anyString());

        ctrl.replay();
        exchangeActivity.acceptExchangeProposal();
        ctrl.verify();
    }
}
