package com.aadm.cardexchange.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CardServiceAsync {
    void getCards(AsyncCallback<Card[]> callback);
}
