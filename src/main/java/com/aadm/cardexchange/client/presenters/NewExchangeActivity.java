package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.List;

public class NewExchangeActivity extends AbstractActivity implements NewExchangeView.NewExchangePresenter {
    private final NewExchangePlace place;
    private final NewExchangeView view;
    private final DeckServiceAsync deckService;
    private final ExchangeServiceAsync exchangeService;
    private final AuthSubject authSubject;
    private final PlaceController placeController;

    public NewExchangeActivity(NewExchangePlace place, NewExchangeView view, DeckServiceAsync deckService, ExchangeServiceAsync exchangeService,
                               AuthSubject authSubject, PlaceController placeController) {
        this.place = place;
        this.view = view;
        this.deckService = deckService;
        this.exchangeService = exchangeService;
        this.authSubject = authSubject;
        this.placeController = placeController;
    }
    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        view.setNewExchangeButtons();
        acceptsOneWidget.setWidget(view.asWidget());
        view.setData(true, "New Exchange Page", "Select the cards you want to exchange from both decks and propose a new exchange");
        fetchMyOwnedDeck();
        fetchUserOwnedDeck();
    }

    private void fetchMyOwnedDeck() {
        deckService.getMyDeck(authSubject.getToken(), "Owned", new BaseAsyncCallback<List<PhysicalCardWithName>>() {
            @Override
            public void onSuccess(List<PhysicalCardWithName> physicalCards) {
                view.setSenderDeck(physicalCards, null);
            }
        });
    }

    private void fetchUserOwnedDeck() {
        deckService.getUserOwnedDeck(place.getReceiverUserEmail(), new BaseAsyncCallback<List<PhysicalCardWithName>>() {
            @Override
            public void onSuccess(List<PhysicalCardWithName> physicalCards) {
                view.setReceiverDeck(physicalCards, place.getSelectedCardId(), place.getReceiverUserEmail());
            }
        });
    }

    @Override
    public void createProposal(List<PhysicalCard> senderDeckSelectedCards, List<PhysicalCard> receiverDeckSelectedCards) {
        if (senderDeckSelectedCards.isEmpty() || receiverDeckSelectedCards.isEmpty()) {
            view.showAlert("Invalid selection error:\nProvide at least one card form each deck");
        }
        else {
            exchangeService.addProposal(authSubject.getToken(), place.getReceiverUserEmail(), senderDeckSelectedCards, receiverDeckSelectedCards, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof AuthException) {
                        view.showAlert(((AuthException) caught).getErrorMessage());
                    } else if (caught instanceof InputException) {
                        view.showAlert(((InputException) caught).getErrorMessage());
                    } else {
                        view.showAlert("Internal server error: " + caught.getMessage());
                    }
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        goTo(new HomePlace());
                    } else {
                        view.showAlert("Something went wrong...\nThis proposal already exists");
                    }
                }
            });
        }
    }

    @Override
    public void onStop() {
        view.resetHandlers();
    }

    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
