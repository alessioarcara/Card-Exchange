package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.presenters.AuthActivity;
import com.aadm.cardexchange.client.views.AuthMode;
import com.aadm.cardexchange.client.views.AuthView;
import com.aadm.cardexchange.shared.AuthServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class AuthActivityTest {
    IMocksControl ctrl;
    AuthView mockView;
    AuthServiceAsync mockRpcService;
    AuthSubject mockSubject;
    PlaceController mockPlaceController;
    AuthActivity authActivity;

    private static Stream<Arguments> provideExceptions() {
        return Stream.of(
                Arguments.of(new AuthException("User not found")),
                Arguments.of(new Exception("Internal server error"))
        );
    }

    @BeforeEach
    public void initialize() {
        ctrl = EasyMock.createStrictControl();
        mockView = ctrl.createMock(AuthView.class);
        mockRpcService = ctrl.createMock(AuthServiceAsync.class);
        mockSubject = new AuthSubject(null);
        mockPlaceController = ctrl.createMock(PlaceController.class);
        authActivity = new AuthActivity(mockView, mockRpcService, mockSubject, mockPlaceController);
    }

    @Test
    public void testAuthenticateForIncorrectEmails() {
        mockView.displayAlert(anyString());
        expectLastCall().times(7);
        ctrl.replay();
        Assertions.assertAll(() -> {
            authActivity.authenticate(AuthMode.Login, "", "password");
            authActivity.authenticate(AuthMode.Login, "test", "password");
            authActivity.authenticate(AuthMode.Login, "test@", "password");
            authActivity.authenticate(AuthMode.Login, "test@test", "password");
            authActivity.authenticate(AuthMode.Login, "test@test.", "password");
            authActivity.authenticate(AuthMode.Login, "test@test.it ", "password");
            authActivity.authenticate(AuthMode.Login, " test@test.it", "password");
        });
        ctrl.verify();
    }

    @Test
    public void testAuthenticateForIncorrectPassword() {
        mockView.displayAlert(anyString());
        expectLastCall();
        ctrl.replay();
        authActivity.authenticate(AuthMode.Login, "test@test.it", "pass");
        ctrl.verify();
    }

    @ParameterizedTest
    @EnumSource(AuthMode.class)
    public void testAuthenticateSuccessForAuthModeParameter(AuthMode authMode) {
        String token = "this_is_a_token";
        if (authMode == AuthMode.Login) {
            mockRpcService.signin(anyString(), anyString(), isA(AsyncCallback.class));
        } else {
            mockRpcService.signup(anyString(), anyString(), isA(AsyncCallback.class));
        }
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<String> callback = (AsyncCallback<String>) args[args.length - 1];
            callback.onSuccess(token);
            return null;
        });
        mockView.setAuthToken(token);
        expectLastCall();
        mockSubject.setToken(token);
        expectLastCall();
        mockPlaceController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        authActivity.authenticate(authMode, "test@test.it", "password");
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    public void testAuthenticateFailure(Exception e) {
        mockRpcService.signup(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<String> callback = (AsyncCallback<String>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockView.displayAlert(anyString());
        expectLastCall();
        ctrl.replay();
        authActivity.authenticate(AuthMode.Signup, "test@test.it", "password");
        ctrl.verify();
    }
}
