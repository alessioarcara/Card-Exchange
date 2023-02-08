package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.ExchangesPlace;
import com.aadm.cardexchange.client.places.ProposalPlace;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.ProposalPayload;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;

public class ProposalActivity extends AbstractActivity implements NewExchangeView.ProposalPresenter {
    private final ProposalPlace place;
    private final NewExchangeView view;
    private final ExchangeServiceAsync exchangeService;
    private final AuthSubject authSubject;
    private final PlaceController placeController;

    public ProposalActivity(ProposalPlace place, NewExchangeView view, ExchangeServiceAsync exchangeService, AuthSubject authSubject, PlaceController placeController) {
        this.place = place;
        this.view = view;
        this.exchangeService = exchangeService;
        this.authSubject = authSubject;
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        setDecksData();
        setButtons();
    }

    @Override
    public void onStop() {
        view.resetAll();
    }

    private void setDecksData() {
        exchangeService.getProposalCards(authSubject.getToken(), place.getProposalId(), new AsyncCallback<ProposalPayload>() {
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
            public void onSuccess(ProposalPayload payload) {
                view.setSenderDeck(payload.getSenderCards(), null);
                view.setReceiverDeck(payload.getReceiverCards(), null);
                view.setReceiverDeckName(payload.getReceiverEmail());
                view.setAcceptButtonEnabled(true);
            }
        });
    }

    private void setButtons() {
        Button cancelButton = new Button("ics-p", (ClickHandler) e -> {
            this.goTo(new ExchangesPlace());
        });
        Button acceptButton = new Button("acc-p", (ClickHandler) e -> {
            this.acceptProposal();
        });
    }

    @Override
    public void acceptProposal() {
        Window.alert( "Accepted proposal : " + place.getProposalId());
    }

    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
