package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.ProposalPlace;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        setProposalDecks();
    }

    private void setProposalDecks() {
        List<PhysicalCard> senderCards = new LinkedList<PhysicalCard>(){{
            add(new PhysicalCard(Game.MAGIC, 111, Status.Good, "this is a valid description"));
            add(new PhysicalCard(Game.YUGIOH, 222, Status.Excellent, "this is a valid description"));
            add(new PhysicalCard(Game.POKEMON, 333, Status.Fair, "this is a valid description"));
        }};

        List<PhysicalCard> receiverCards = new LinkedList<PhysicalCard>(){{
            add(new PhysicalCard(Game.MAGIC, 555, Status.Damaged, "this is a valid description"));
            add(new PhysicalCard(Game.YUGIOH, 666, Status.Good, "this is a valid description"));
            add(new PhysicalCard(Game.POKEMON, 777, Status.Excellent, "this is a valid description"));
        }};
        Proposal proposal = new Proposal("test@test.it", "receiver@test.it", senderCards, receiverCards);

        setData(0 /* proposal.getId() */);
        view.setReceiverDeckName(proposal.getReceiverUserEmail());
    }

    private void setData(int proposalId) {
        exchangeService.getProposalCards(authSubject.getToken(), proposalId, new AsyncCallback<Map<String, List<PhysicalCardWithName>>>() {

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
            public void onSuccess(Map<String, List<PhysicalCardWithName>> physicalCardWithNames) {
                view.setSenderDeck(physicalCardWithNames.get("Sender"), null);
                view.setReceiverDeck(physicalCardWithNames.get("Receiver"), null);
            }
        });
    }

    @Override
    public void acceptProposal() {
        Window.alert( "" + place.getProposalId());
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
