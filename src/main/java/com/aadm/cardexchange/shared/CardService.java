package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

// associa il service a un path relativo al modulo
@RemoteServiceRelativePath("cards")
public interface CardService extends RemoteService {
    CardImpl[] getCards();

    List<CardDecorator> getGameCards(Game game);
}
