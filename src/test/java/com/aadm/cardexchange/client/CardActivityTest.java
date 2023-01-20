package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.presenters.CardActivity;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.Game;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;

import static org.easymock.EasyMock.createStrictControl;

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
        cardActivity = new CardActivity(mockPlace, mockView, mockRpcService);
    }
}
