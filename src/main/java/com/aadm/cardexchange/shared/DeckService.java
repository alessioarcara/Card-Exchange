package com.aadm.cardexchange.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("decks")
public interface DeckService extends RemoteService {

     boolean addDeck(String email, String deckName);

}
