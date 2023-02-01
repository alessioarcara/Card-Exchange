package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.presenters.NewExchangeActivity;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

public class NewExchangeActivityTest {
    private final String RECEIVER_USER_EMAIL = "test@test.it";
    private final String SELECTED_PCARD_ID = "y132154654";
    IMocksControl ctrl;
    NewExchangePlace mockPlace;
    NewExchangeView mockView;
    PlaceController placeController;
    NewExchangeActivity newExchangeActivity;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockPlace = new NewExchangePlace(SELECTED_PCARD_ID, RECEIVER_USER_EMAIL);
        mockView = ctrl.createMock(CardView.class);
        placeController = ctrl.createMock(PlaceController.class);
        newExchangeActivity = new NewExchangeActivity(mockView, mockPlace, placeController);
    }

    @Test
    public void testForGoTo() {
        placeController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        newExchangeActivity.goTo(new NewExchangePlace("y123123", "test@test.it"));
        ctrl.verify();
    }

}
