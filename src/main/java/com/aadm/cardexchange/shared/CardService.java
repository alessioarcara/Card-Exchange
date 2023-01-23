package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("cards")
public interface CardService extends RemoteService {
    List<CardDecorator> getGameCards(Game game);

    CardDecorator getGameCard(Game game, int cardId);
}
