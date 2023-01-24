package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.AuthSubject.AuthSubject;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.presenters.CardActivity;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

public class CardActivityTest {
    private static final int CARD_ID = 111;
    IMocksControl ctrl;
    CardPlace mockPlace;
    CardView mockView;
    CardServiceAsync mockRpcService;
    CardActivity cardActivity;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockPlace = new CardPlace(Game.Magic, CARD_ID);
        mockView = ctrl.createMock(CardView.class);
        mockRpcService = ctrl.mock(CardServiceAsync.class);
        cardActivity = new CardActivity(mockPlace, mockView, mockRpcService, new AuthSubject(null));
    }

    @Test
    public void testFetchCardForOnSuccess() {
        CardDecorator cardDecorator = new CardDecorator(new CardImpl("Charizard", "Un pokemon di fuoco", "Fuoco"));
        mockRpcService.getGameCard(isA(Game.class), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<CardDecorator> callback = (AsyncCallback<CardDecorator>) args[args.length - 1];
            callback.onSuccess(cardDecorator);
            return null;
        });
        mockView.setData(cardDecorator);
        expectLastCall();
        ctrl.replay();
        cardActivity.fetchCard();
        ctrl.verify();
    }
}
