package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.services.AuthServiceImpl;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.User;
import com.aadm.cardexchange.shared.payloads.AuthPayload;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapdb.Serializer;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class AuthServiceTest {
    @Nested
    class WithMockDB {
        ServletConfig mockConfig;
        ServletContext mockCtx;
        private IMocksControl ctrl;
        private MapDB mockDB;
        private AuthServiceImpl authService;

        @BeforeEach
        public void initialize() throws ServletException {
            ctrl = createStrictControl();
            mockDB = ctrl.createMock(MapDB.class);
            mockConfig = ctrl.createMock(ServletConfig.class);
            mockCtx = ctrl.createMock(ServletContext.class);
            authService = new AuthServiceImpl(mockDB);
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
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(userMap);
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> authService.signup("test@test.it", "password"));
            ctrl.verify();
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
        public void testSigninForUserNotFound() {
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
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
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
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
        public void testCheckTokenValidityForInvalidToken() {
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> authService.checkTokenValidity("invalidToken", new HashMap<>() {{
                put("validToken1", new LoginInfo("test1@test.it", System.currentTimeMillis()));
                put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 2000));
                put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 4000));
            }}));
            ctrl.verify();
        }

        @Test
        public void testCheckTokenValidityForNullToken() {
            Assertions.assertThrows(AuthException.class, () -> authService.checkTokenValidity(null, new HashMap<>()));
        }

        @Test
        //Token scaduto da 7 gg + 10 secondi
        public void testCheckTokenValidityForExpiredToken() {
            ctrl.replay();
            try {
                authService.checkTokenValidity("validToken1", new HashMap<>() {{
                    put("validToken1", new LoginInfo("test1@test.it", System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 7 + 1000 * 10)));
                }});
            } catch (AuthException e) {
                Assertions.assertEquals("Expired token", e.getErrorMessage());
            }
            ctrl.verify();
        }

        @Test
        public void testCheckTokenValidityForValidToken() throws AuthException {
            ctrl.replay();
            Assertions.assertEquals("test1@test.it", authService.checkTokenValidity("validToken1", new HashMap<>() {{
                put("validToken1", new LoginInfo("test1@test.it", System.currentTimeMillis()));
            }}));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = "invalidToken")
        public void testMeForInvalidToken(String input) {
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<String, LoginInfo>() {{
                        put("validToken1", new LoginInfo("test1@test.it", System.currentTimeMillis() - 10000));
                        put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
                        put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
                    }});
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> authService.me(input));
            ctrl.verify();
        }

        @Test
        public void testMeForValidToken() throws AuthException {
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(new HashMap<String, LoginInfo>() {{
                        put("validToken1", new LoginInfo("test1@test.it", System.currentTimeMillis() - 10000));
                        put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
                        put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
                    }});
            ctrl.replay();
            Assertions.assertEquals("test2@test.it", authService.me("validToken2"));
            ctrl.verify();
        }
    }

    @Nested
    class WithFakeDB {
        IMocksControl ctrl;
        AuthServiceImpl authService;
        ServletConfig mockConfig;
        ServletContext mockCtx;

        @BeforeEach
        public void initialize() throws ServletException {
            ctrl = createStrictControl();
            FakeDB fakeDB = new FakeDB(new HashMap<>(), new HashMap<>());
            authService = new AuthServiceImpl(fakeDB);
            mockConfig = ctrl.createMock(ServletConfig.class);
            mockCtx = ctrl.createMock(ServletContext.class);
            authService.init(mockConfig);
        }


        @Test
        public void testSignupForCorrectEmailAndPassword() throws AuthException {
            String email = "test3@test.it";
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
            ctrl.replay();
            AuthPayload token = authService.signup(email, "password");
            ctrl.verify();
            Assertions.assertAll(() -> {
                Assertions.assertNotNull(token.getToken());
                Assertions.assertEquals(email, token.getEmail());
            });
        }

        @Test
        public void testSigninForCorrectEmailAndPassword() throws AuthException {
            String email = "test@test.it"; // check fakeDB userMap
            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            ctrl.replay();
            AuthPayload token = authService.signin(email, "password");
            ctrl.verify();
            Assertions.assertAll(() -> {
                Assertions.assertNotNull(token.getToken());
                Assertions.assertEquals(email, token.getEmail());
            });
        }


        @Test
        public void testLogoutForInvalidToken() {
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            ctrl.replay();
            Assertions.assertThrows(AuthException.class, () -> authService.logout("invalidToken"));
            ctrl.verify();
        }

        @Test
        public void testLogoutForValidToken() throws AuthException {
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            ctrl.replay();
            Assertions.assertTrue(authService.logout("validToken"));
            ctrl.verify();
        }
    }
}