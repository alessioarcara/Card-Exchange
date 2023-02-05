package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.client.widgets.PhysicalCardWidget;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.aadm.cardexchange.shared.models.Proposal;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.ArrayList;
import java.util.List;

public class NewExchangeActivity extends AbstractActivity implements NewExchangeView.Presenter {
    private final NewExchangePlace place;
    private final NewExchangeView view;
    private final DeckServiceAsync deckService;
    private final AuthSubject authSubject;
    private final PlaceController placeController;

    public NewExchangeActivity(NewExchangePlace place, NewExchangeView view, DeckServiceAsync deckService, AuthSubject authSubject, PlaceController placeController) {
        this.place = place;
        this.view = view;
        this.deckService = deckService;
        this.authSubject = authSubject;
        this.placeController = placeController;
    }
    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        deckService.getMyDeck(authSubject.getToken(), "Owned", new BaseAsyncCallback<List<PhysicalCardWithName>>() {
            @Override
            public void onSuccess(List<PhysicalCardWithName> physicalCardDecorators) {
                view.setSenderDeck(physicalCardDecorators);
            }
        });
        deckService.getUserOwnedDeck(place.getReceiverUserEmail(), new BaseAsyncCallback<List<PhysicalCardWithName>>() {
            @Override
            public void onSuccess(List<PhysicalCardWithName> physicalCardDecorators) {
                view.setReceiverDeck(physicalCardDecorators);
            }
        });

        view.setData(place.getReceiverUserEmail(), place.getSelectedCardId());
    }

    public void createProposal(DeckWidget senderDeck, DeckWidget receiverDeck) {
        List<PhysicalCard> senderSelectedCards = new ArrayList<>();
        List<PhysicalCard> receiverSelectedCards = new ArrayList<>();

        for (PhysicalCardWidget pCard : senderDeck.getDeckCards()) {
            if (pCard.getSelected()) {
                senderSelectedCards.add(pCard.getPCard());
            }
        }
        for (PhysicalCardWidget pCard : receiverDeck.getDeckCards()) {
           if (pCard.getSelected()) {
                receiverSelectedCards.add(pCard.getPCard());
            }
        }

        Proposal proposal = new Proposal(authSubject.getToken(), place.getReceiverUserEmail(), senderSelectedCards, receiverSelectedCards);

        /*
            THIS SECTION IS ONLY FOR DEBUG PURPOSES UNTIL THE EXCHANGE AND THE SELECTION LOGIC ISN'T DONE COMPLETELY
         */
        String message = "sender: ";
        for (PhysicalCard pc: proposal.getSenderPhysicalCards()) {
            message += pc.getId() + " ";
        }
        message += " receiver: ";
        for (PhysicalCard pc: proposal.getReceiverPhysicalCards()) {
            message += pc.getId() + " ";
        }
        Window.alert(message);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
