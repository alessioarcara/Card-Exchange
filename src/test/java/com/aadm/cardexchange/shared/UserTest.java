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
        user = new User(email, BCrypt.hashpw(password, BCrypt.gensalt()));
    }

    @Test
    public void testGetEmail() {
        Assertions.assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetPassword() {
        Assertions.assertTrue(BCrypt.checkpw(password, user.getPassword()));
    }
}
