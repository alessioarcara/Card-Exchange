package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 2004822123627539475L;
    private static final AtomicInteger uniqueId = new AtomicInteger();

    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
