package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

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

        view.setData(place.getReceiverUserEmail(), place.getSelectedCardId(), authSubject.getToken());
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
