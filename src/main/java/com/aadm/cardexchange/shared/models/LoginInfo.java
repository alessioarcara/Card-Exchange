package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private static final long serialVersionUID = -6617923440217703559L;
    private String userEmail;
    private long loginTime;

    public LoginInfo(String userEmail, long loginTime) {
        this.userEmail = userEmail;
        this.loginTime = loginTime;
    }

    public LoginInfo() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public long getLoginTime() {
        return loginTime;
    }
}
