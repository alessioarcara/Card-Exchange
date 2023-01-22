package com.aadm.cardexchange.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthServiceAsync {
    void signup(String email, String password, AsyncCallback<String> callback);

    void signin(String email, String password, AsyncCallback<String> callback);

    void logout(String token, AsyncCallback<Boolean> async);
}
