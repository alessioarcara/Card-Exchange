package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.AuthException;
import com.aadm.cardexchange.shared.models.DeckException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("users")
public interface DeckService extends RemoteService {

     boolean addDeck(String email, String deckName) throws DeckException;

    //String signin(String email, String password) throws AuthException;

    //Boolean logout(String token) throws AuthException;
}
