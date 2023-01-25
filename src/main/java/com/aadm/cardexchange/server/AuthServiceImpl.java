package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.AuthService;
import com.aadm.cardexchange.shared.models.AuthException;
import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.User;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;
import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AuthServiceImpl extends RemoteServiceServlet implements AuthService, AuthDBConstants {
    private static final long serialVersionUID = 5646081795222815765L;
    private static final Pattern emailPattern = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final MapDB db;
    private final Gson gson = new Gson();
    private final DeckServiceImpl deckService;

    public AuthServiceImpl() {
        db = new MapDBImpl();
        deckService = new DeckServiceImpl();
    }

    public AuthServiceImpl(MapDB mockDB, DeckServiceImpl mockService) {
        db = mockDB;
        deckService = mockService;
    }

    private boolean validateEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    private boolean validateCredentials(String email, String password) {
        return (email == null || email.isBlank() || !validateEmail(email)) ||
                (password == null || password.isBlank() || password.length() < 8);
    }

    //Generazione token tramite hashing
    private static String generateHash(String userEmail) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest((userEmail + SECRET).getBytes());
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String generateAndStoreLoginToken(User user) {
        Map<String, LoginInfo> loginMap = db.getPersistentMap(
                getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        String token = generateHash(user.getEmail());
        loginMap.put(token, new LoginInfo(user.getEmail(), System.currentTimeMillis()));
        return token;
    }

    private boolean checkTokenExpiration(long loginTime) {
        final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
        return loginTime > System.currentTimeMillis() - EXPIRATION_TIME;
    }

    @Override
    public String signup(String email, String password) throws AuthException {
        if (validateCredentials(email, password)) {
            throw new AuthException("Invalid credentials");
        }
        Map<String, User> userMap = db.getPersistentMap(
                getServletContext(), USER_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        User user = new User(email, BCrypt.hashpw(password, BCrypt.gensalt()));
        if (userMap.putIfAbsent(email, user) != null) {
            throw new AuthException("User already exists");
        }
        deckService.createDefaultDecks(email);
        return generateAndStoreLoginToken(user);
    }

    @Override
    public String signin(String email, String password) throws AuthException {
        if (validateCredentials(email, password)) {
            throw new AuthException("Invalid credentials");
        }
        Map<String, User> userMap = db.getPersistentMap(
                getServletContext(), USER_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        User user = userMap.get(email);
        if (user == null) {
            throw new AuthException("User not found");
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new AuthException("Password don't match");
        }
        return generateAndStoreLoginToken(user);
    }

    @Override
    public Boolean logout(String token) throws AuthException {
        if (token == null) {
            throw new AuthException("Invalid token");
        }
        Map<String, LoginInfo> loginMap = db.getPersistentMap(
                getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        if (loginMap.remove(token) == null) {
            throw new AuthException("User not found");
        }
        return true;
    }

    public String checkTokenValidity(String token, Map<String, LoginInfo> loginMap) throws AuthException {
        if (token == null) {
            throw new AuthException("Invalid token");
        }
        LoginInfo loginInfo = loginMap.get(token);
        if (loginInfo == null) {
            throw new AuthException("Invalid token");
        } else if (!checkTokenExpiration(loginInfo.getLoginTime())) {
            throw new AuthException("Expired token");
        }
        return loginInfo.getUserEmail();
    }
}
