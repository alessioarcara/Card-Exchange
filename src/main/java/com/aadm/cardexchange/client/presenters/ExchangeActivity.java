package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.ExchangesPlace;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.exceptions.ProposalNotFoundException;
import com.aadm.cardexchange.shared.payloads.ProposalPayload;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ExchangeActivity extends AbstractActivity implements NewExchangeView.ExchangePresenter {
    private final ExchangesPlace place;
    private final NewExchangeView view;
    private final ExchangeServiceAsync exchangeService;
    private final AuthSubject authSubject;
    private final PlaceController placeController;

    public ExchangeActivity(ExchangesPlace place, NewExchangeView view, ExchangeServiceAsync exchangeService, AuthSubject authSubject, PlaceController placeController) {
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
        view.setExchangeButtons();
        view.setData(false, "Exchange proposal Page", "Check the cards in the exchange proposal before accepting or refusing it");
        fetchProposalData();
    }

    private void fetchProposalData() {
        exchangeService.getProposal(authSubject.getToken(), place.getProposalId(), new AsyncCallback<ProposalPayload>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthException) {
                    view.showAlert(((AuthException) caught).getErrorMessage());
                } else if (caught instanceof InputException) {
                    view.showAlert(((InputException) caught).getErrorMessage());
                } else if (caught instanceof ProposalNotFoundException) {
                    view.showAlert(((ProposalNotFoundException) caught).getErrorMessage());
                } else {
                    view.showAlert("Internal server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(ProposalPayload payload) {
                view.setSenderDeck(payload.getSenderCards(), null);
                view.setReceiverDeck(payload.getReceiverCards(), null, payload.getReceiverEmail());
                view.setAcceptButtonEnabled(true);
            }
        });
    }

    @Override
    public void onStop() {
        view.resetHandlers();
    }

    @Override
    public void acceptExchangeProposal() {
        exchangeService.acceptProposal(authSubject.getToken(), place.getProposalId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthException)
                    view.showAlert(((AuthException) caught).getErrorMessage());
                else if (caught instanceof ProposalNotFoundException)
                    view.showAlert(((ProposalNotFoundException) caught).getErrorMessage());
                else
                    view.showAlert("Internal server error: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    view.showAlert("Successfully accepted proposal: " +  place.getProposalId());
                    goTo(new ExchangesPlace(null));
                }
                else
                    view.showAlert("It seems this proposal doesn't exist anymore");
            }
        });
    }

    @Override
    public void refuseOrWithdrawProposal() {
        exchangeService.refuseOrWithdrawProposal(authSubject.getToken(), place.getProposalId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthException)
                    view.showAlert(((AuthException) caught).getErrorMessage());
                else if (caught instanceof InputException)
                    view.showAlert(((InputException) caught).getErrorMessage());
                else if (caught instanceof NullPointerException)
                    view.showAlert(caught.getMessage());
                else
                    view.showAlert("Internal server error: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    view.showAlert("Successfully refused/withdrawn proposal: " + place.getProposalId());
                    goTo(new ExchangesPlace(null));
                }
                else
                    view.showAlert("It seems this proposal doesn't exist anymore");
            }
        });
    }

    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
