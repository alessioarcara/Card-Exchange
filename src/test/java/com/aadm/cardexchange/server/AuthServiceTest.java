package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.AuthException;
import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.User;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.Serializer;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class AuthServiceTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    ServletConfig mockConfig;
    ServletContext mockCtx;
    private AuthServiceImpl authService;

    @BeforeEach
    public void initialize() throws ServletException {
        ctrl = createStrictControl();
        mockDB = ctrl.createMock(MapDB.class);
        authService = new AuthServiceImpl(mockDB);
        mockConfig = ctrl.createMock(ServletConfig.class);
        mockCtx = ctrl.createMock(ServletContext.class);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        authService.init(mockConfig);
    }

    @Test
    public void testAuthException() {
        try {
            throw new AuthException("test");
        } catch (AuthException e) {
            Assertions.assertEquals("test", e.getErrorMessage());
        }
    }

    @Test
    public void testSignupForNullEmail() {
        Assertions.assertThrows(AuthException.class, () -> authService.signup(null, "password"));
    }

    @Test
    public void testSignupForEmptyStringEmail() {
        Assertions.assertThrows(AuthException.class, () -> authService.signup("", "password"));
    }

    @Test
    public void testSignupForIncorrectEmails() {
        Assertions.assertAll(() -> {
            Assertions.assertThrows(AuthException.class, () -> authService.signup("test", "password"));
            Assertions.assertThrows(AuthException.class, () -> authService.signup("test@", "password"));
            Assertions.assertThrows(AuthException.class, () -> authService.signup("test@test", "password"));
            Assertions.assertThrows(AuthException.class, () -> authService.signup("test@test.", "password"));
        });
    }

    @Test
    public void testSignupForNullPassword() {
        Assertions.assertThrows(AuthException.class, () -> authService.signup("test@test.it", ""));
    }

    @Test
    public void testSignupForLessThan8CharactersPassword() {
        Assertions.assertThrows(AuthException.class, () -> authService.signup("test@test.it", "passwor"));
    }

    @Test
    public void testSignupForAlreadyExistingUser() {
        Map<String, User> userMap = new HashMap<>();
        userMap.put("test@test.it", new User("test@test.it", "password"));
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(userMap);
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> authService.signup("test@test.it", "password"));
        ctrl.verify();
    }

    @Test
    public void testSignupForCorrectEmailAndPassword() throws AuthException {
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        String token = authService.signup("test@test.it", "password");
        ctrl.verify();
        Assertions.assertNotNull(token);
    }

    @Test
    public void testSigninForNullEmail() {
        Assertions.assertThrows(AuthException.class, () -> authService.signin(null, "password"));
    }

    @Test
    public void testSigninForEmptyStringEmail() {
        Assertions.assertThrows(AuthException.class, () -> authService.signin("", "password"));
    }

    @Test
    public void testSigninForIncorrectEmails() {
        Assertions.assertAll(() -> {
            Assertions.assertThrows(AuthException.class, () -> authService.signin("test", "password"));
            Assertions.assertThrows(AuthException.class, () -> authService.signin("test@", "password"));
            Assertions.assertThrows(AuthException.class, () -> authService.signin("test@test", "password"));
            Assertions.assertThrows(AuthException.class, () -> authService.signin("test@test.", "password"));
        });
    }

    @Test
    public void testSigninForNullPassword() {
        Assertions.assertThrows(AuthException.class, () -> authService.signin("test@test.it", ""));
    }

    @Test
    public void testSigninForLessThan8CharactersPassword() {
        Assertions.assertThrows(AuthException.class, () -> authService.signin("test@test.it", "passwor"));
    }

    @Test
    public void testSigninForCorrectEmailAndPassword() throws AuthException {
        Map<String, User> userMap = new HashMap<>();
        userMap.put("test@test.it", new User("test@test.it", BCrypt.hashpw("password", BCrypt.gensalt())));
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(userMap);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        String token = authService.signin("test@test.it", "password");
        ctrl.verify();
        Assertions.assertNotNull(token);
    }

    @Test
    public void testSigninForUserNotFound() {
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () ->
                authService.signin("test@test.it", "password"));
        ctrl.verify();
    }

    @Test
    public void testSigninForNotPasswordMatch() {
        Map<String, User> userMap = new HashMap<>();
        userMap.put("test@test.it", new User("test@test.it", BCrypt.hashpw("wrong_password", BCrypt.gensalt())));
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(userMap);
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () ->
                authService.signin("test@test.it", "password"));
        ctrl.verify();
    }

    @Test
    public void testLogoutForNullToken() {
        Assertions.assertThrows(AuthException.class, () -> authService.logout(null));
    }

    @Test
    public void testLogoutForInvalidToken() {
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> authService.logout("invalidToken"));
        ctrl.verify();
    }

    @Test
    public void testLogoutForValidToken() throws AuthException {
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<String, LoginInfo>() {{
                    put("validToken", new LoginInfo(1, System.currentTimeMillis()));
                }});
        ctrl.replay();
        Assertions.assertTrue(authService.logout("validToken"));
        ctrl.verify();
    }
}