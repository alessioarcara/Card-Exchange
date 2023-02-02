package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.auth.Observer;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CardActivity extends AbstractActivity implements CardView.Presenter, Observer {
    private final CardPlace place;
    private final CardView view;
    private final CardServiceAsync cardService;
    private final DeckServiceAsync deckService;
    private final AuthSubject authSubject;
    private final PlaceController placeController;

    public CardActivity(CardPlace place, CardView view, CardServiceAsync cardService, DeckServiceAsync deckService, AuthSubject authSubject, PlaceController placeController) {
        this.place = place;
        this.view = view;
        this.cardService = cardService;
        this.deckService = deckService;
        this.authSubject = authSubject;
        this.placeController = placeController;
        authSubject.attach(this);
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());
        fetchCard();
        update();
    }

    @Override
    public void update() {
        view.createUserWidgets(authSubject.isLoggedIn());
    }

    public void fetchCard() {
        cardService.getGameCard(place.getGame(), place.getCardId(), new BaseAsyncCallback<CardDecorator>() {
            @Override
            public void onSuccess(CardDecorator result) {
                view.setData(result);
            }
        });
    }

    public void addCardToDeck(String deckName, String status, String description) {
        deckService.addPhysicalCardToDeck(
                authSubject.getToken(),
                place.getGame(),
                deckName,
                place.getCardId(),
                Status.getStatus(Integer.parseInt(status)),
                description,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof AuthException) {
                            view.displayErrorAlert(((AuthException) caught).getErrorMessage());
                        } else if (caught instanceof InputException) {
                            view.displayErrorAlert(((InputException) caught).getErrorMessage());
                        } else {
                            view.displayErrorAlert(caught.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            view.displaySuccessAlert();
                            view.hideModal();
                        } else {
                            view.displayErrorAlert("Deck not found.");
                        }
                    }
                }
        );
    }

    @Override
    public void goTo(Place place) { placeController.goTo(place); }
}
