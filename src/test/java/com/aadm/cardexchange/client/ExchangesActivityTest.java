package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.ExchangesPlace;
import com.aadm.cardexchange.client.presenters.ExchangesActivity;
import com.aadm.cardexchange.client.views.ExchangesView;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

public class ExchangesActivityTest {
    IMocksControl ctrl;
    ExchangesView mockView;
    PlaceController mockPlaceController;
    ExchangesActivity exchangesActivity;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockView = ctrl.createMock(ExchangesView.class);
        ExchangeServiceAsync mockRpcService = ctrl.createMock(ExchangeServiceAsync.class);
        mockPlaceController = ctrl.createMock(PlaceController.class);
        exchangesActivity = new ExchangesActivity(mockView, mockRpcService, new AuthSubject(), mockPlaceController);
    }

    @Test
    public void testForGoTo() {
        mockPlaceController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        exchangesActivity.goTo(new ExchangesPlace(1));
        ctrl.verify();
    }
}
