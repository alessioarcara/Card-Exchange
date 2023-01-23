package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private static final long serialVersionUID = -6617923440217703559L;
    private int userId;
    private long loginTime;

    public LoginInfo(int userId, long loginTime) {
        this.userId = userId;
        this.loginTime = loginTime;
    }

    public LoginInfo() {
    }

    public int getUserId() {
        return userId;
    }

    public long getLoginTime() {
        return loginTime;
    }
}
