package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.presenters.NewExchangeActivity;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.client.widgets.ImperativeHandleDeck;
import com.aadm.cardexchange.shared.DeckServiceAsync;
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
    DeckServiceAsync mockDeckService;
    ImperativeHandleDeck mockImperativeHandleDeck;
    AuthSubject mockAuthSubject;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockPlace = new NewExchangePlace(SELECTED_PCARD_ID, RECEIVER_USER_EMAIL);
        mockView = ctrl.createMock(CardView.class);
        mockDeckService = ctrl.createMock(DeckServiceAsync.class);
        mockAuthSubject = ctrl.createMock(AuthSubject.class);
        placeController = ctrl.createMock(PlaceController.class);
        newExchangeActivity = new NewExchangeActivity(mockPlace, mockView, mockDeckService, mockAuthSubject, placeController);
    }

    @Test
    public void testForGoTo() {
        placeController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        newExchangeActivity.goTo(new NewExchangePlace("y123123", "test@test.it"));
        ctrl.verify();
    }

//    @Test
//    public void testCreateProposal() {
//        DeckWidget senderDeck = new DeckWidget(mockImperativeHandleDeck, "sender");
//        DeckWidget receiverDeck = new DeckWidget(mockImperativeHandleDeck, "receiver");
//        PhysicalCardWithName pCard1 = new PhysicalCardWithName(new PhysicalCard(Game.YUGIOH, 111, Status.Good, "this is a valid description"), "name1");
//        PhysicalCardWithName pCard2 = new PhysicalCardWithName(new PhysicalCard(Game.YUGIOH, 222, Status.Good, "this is a valid description"), "name2");
//        PhysicalCardWithName pCard3 = new PhysicalCardWithName(new PhysicalCard(Game.YUGIOH, 333, Status.Good, "this is a valid description"), "name3");
//        PhysicalCardWithName pCard4 = new PhysicalCardWithName(new PhysicalCard(Game.YUGIOH, 444, Status.Good, "this is a valid description"), "name4");
//
//        senderDeck.setData(Arrays.asList(pCard1, pCard2), pCard1.getId());
//        receiverDeck.setData(Arrays.asList(pCard3, pCard4), pCard4.getId());

//        ctrl.replay();
//        newExchangeActivity.createProposal(senderDeck, receiverDeck);
//        ctrl.verify();
//    }

}
