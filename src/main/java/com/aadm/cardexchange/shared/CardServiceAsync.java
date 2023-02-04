package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface CardServiceAsync {
    void getGameCards(Game game, AsyncCallback<List<Card>> callback);

    void getGameCard(Game game, int cardId, AsyncCallback<Card> callback);
}
