package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.AuthService;
import com.aadm.cardexchange.shared.models.LoginInfo;
import com.aadm.cardexchange.shared.models.User;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.mapdb.Serializer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


public class AuthServiceImpl extends RemoteServiceServlet implements AuthService, AuthDBConstants {
    private static final long serialVersionUID = 5646081795222815765L;
    private final MapDB db;
    private final Gson gson = new Gson();

    public AuthServiceImpl() {
        db = new MapDBImpl();
    }

    public AuthServiceImpl(MapDB mockDB) {
        db = mockDB;
    }

    private static String generateHash(int userId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest((userId + SECRET).getBytes());
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

    @Override
    public String signup(String email, String password) {
        if ((email == null || email.isBlank()) || (password == null || password.isBlank())) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        Map<String, User> userMap = db.getPersistentMap(
                getServletContext(), USER_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        User user = new User(email, password);
        if (userMap.putIfAbsent(email, user) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        Map<String, LoginInfo> loginMap = db.getPersistentMap(
                getServletContext(), LOGIN_MAP_NAME, Serializer.STRING, new GsonSerializer<>(gson));
        String token = generateHash(user.getId());
        loginMap.put(token, new LoginInfo(user.getId(), System.currentTimeMillis()));
        db.flush(getServletContext());
        return token;
    }
}
