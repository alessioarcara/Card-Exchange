package com.aadm.cardexchange.server;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createStrictControl;

public class UserServiceTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    private UserServiceImpl userService;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockDB = ctrl.createMock(MapDB.class);
        userService = new UserServiceImpl(mockDB);
    }

    @Test
    public void testSigninForNullEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.signin(null, "password"));
    }
}
