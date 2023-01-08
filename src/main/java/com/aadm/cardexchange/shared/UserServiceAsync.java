package com.aadm.cardexchange.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
    void me(AsyncCallback<Boolean> callback);
}
