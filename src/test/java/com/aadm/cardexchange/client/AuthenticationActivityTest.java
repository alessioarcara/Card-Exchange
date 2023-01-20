package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.presenters.AuthenticationActivity;
import com.aadm.cardexchange.client.views.AuthMode;
import com.aadm.cardexchange.client.views.AuthenticationView;
import com.google.gwt.place.shared.PlaceController;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationActivityTest {
    IMocksControl ctrl;
    AuthenticationView mockView;
    PlaceController mockPlaceController;
    AuthenticationActivity authenticationActivity;
    @BeforeEach
    public void initialize() {
        ctrl = EasyMock.createStrictControl();
        mockView = ctrl.createMock(AuthenticationView.class);
        mockPlaceController = ctrl.createMock(PlaceController.class);
        authenticationActivity = new AuthenticationActivity(mockView, mockPlaceController);
    }

    @Test
    public void testAuthenticateForIncorrectEmails() {
        Assertions.assertAll(() -> {
            Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationActivity.authenticate(AuthMode.Login, "", "88888888"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationActivity.authenticate(AuthMode.Login, "test", "88888888"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationActivity.authenticate(AuthMode.Login, "test@", "88888888"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationActivity.authenticate(AuthMode.Login, "test@test", "88888888"));
            Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationActivity.authenticate(AuthMode.Login, "test@test.", "88888888"));
        });
    }

    @Test
    public void testAuthenticateForIncorrectPassword() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationActivity.authenticate(AuthMode.Login, "test@test.it", "4444"));
    }

    @Test
    public void testAuthenticateForAuthModeLoginParameter() {
        Assertions.assertDoesNotThrow(() ->authenticationActivity.authenticate(AuthMode.Login, "test@test.it", "88888888"));
    }

    @Test
    public void testAuthenticateForAuthModeSignupParameter() {
        Assertions.assertDoesNotThrow(() ->authenticationActivity.authenticate(AuthMode.Signup, "test@test.it", "88888888"));
    }
}