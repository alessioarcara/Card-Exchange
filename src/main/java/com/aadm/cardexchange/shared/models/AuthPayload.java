package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public class AuthPayload implements Serializable {
    private static final long serialVersionUID = -4250272545231717477L;
    private String token;
    private String email;

    public AuthPayload(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public AuthPayload() {
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }
}
