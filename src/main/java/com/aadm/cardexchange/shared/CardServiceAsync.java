package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface CardServiceAsync {
    void getCards(AsyncCallback<CardImpl[]> callback);

    void getGameCards(Game game, AsyncCallback<List<CardDecorator>> callback);
}
