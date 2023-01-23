package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.AuthSubject.AuthSubject;
import com.aadm.cardexchange.client.AuthSubject.Observer;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

public class AuthSubjectTest {
    AuthSubject authSubject;

    @BeforeEach
    public void initialize() {
        authSubject = new AuthSubject("test");
    }

    @Test
    public void testGetToken() {
        Assertions.assertEquals("test", authSubject.getToken());
    }

    @Test
    public void testIsLoggedIn() {
        Assertions.assertTrue(authSubject.isLoggedIn());
    }

    @Test
    public void testUpdate() {
        Observer observer = EasyMock.createStrictMock(Observer.class);
        authSubject.attach(observer);
        observer.update();
        expectLastCall();

        replay(observer);
        authSubject.setToken(null);
        verify(observer);
    }

    @Test
    public void testDetach() {
        Observer observer = EasyMock.createStrictMock(Observer.class);
        authSubject.attach(observer);
        authSubject.detach(observer);
        replay(observer);
        authSubject.setToken(null);
        verify(observer);
    }
}