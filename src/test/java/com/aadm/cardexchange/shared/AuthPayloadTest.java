package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.payloads.AuthPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthPayloadTest {
    AuthPayload authPayload;

    @BeforeEach
    public void initialize() {
        authPayload = new AuthPayload("validToken", "test@test.it");
    }

    @Test
    public void testGetToken() {
        Assertions.assertEquals("validToken", authPayload.getToken());
    }

    @Test
    public void testGetEmail() {
        Assertions.assertEquals("test@test.it", authPayload.getEmail());
    }
}
