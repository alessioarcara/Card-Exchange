package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("cards")
public interface CardService extends RemoteService {
    List<Card> getGameCards(Game game);

    Card getGameCard(Game game, int cardId);
}
