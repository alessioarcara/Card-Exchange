package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class UserTest implements UserTestConstants {
    private User user;

    @BeforeEach
    public void initialize() {
        user = new User(username, password);
    }

    @Test
    public void testGetId() {
        User user2 = new User("gattopardo2@test.it", "gattopardo");
        Assertions.assertEquals(2, user2.getId());
    }

    @Test
    public void testGetUsername() {
        Assertions.assertEquals(username, user.getUsername());
    }

    @Test
    public void testGetPassword() {
        Assertions.assertTrue(BCrypt.checkpw(password, user.getPassword()));
    }
}
