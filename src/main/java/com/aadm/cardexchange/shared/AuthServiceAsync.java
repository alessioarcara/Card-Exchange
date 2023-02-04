package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.AuthPayload;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthServiceAsync {
    void me(String token, AsyncCallback<String> callback);

    void signup(String email, String password, AsyncCallback<AuthPayload> callback);

    void signin(String email, String password, AsyncCallback<AuthPayload> callback);

    void logout(String token, AsyncCallback<Boolean> callback);
}
