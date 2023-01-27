package com.aadm.cardexchange.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public interface DeckServiceAsync {
    void addDeck(String email, String deckName, AsyncCallback<Boolean> async);
}
