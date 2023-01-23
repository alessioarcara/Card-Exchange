package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginInfoTest {
    private LoginInfo loginInfo;
    private long mockLoginTime;

    @BeforeEach
    public void initialize() {
        mockLoginTime = System.currentTimeMillis();
        loginInfo = new LoginInfo(
                new User("test@test.it", "password").getId(),
                mockLoginTime);
    }

    @Test
    public void testGetUserId() {
        Assertions.assertEquals(5, loginInfo.getUserId());
    }

    @Test
    public void testGetLoginTime() {
        Assertions.assertEquals(mockLoginTime, loginInfo.getLoginTime());
    }
}
