package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class NewExchangeActivity extends AbstractActivity implements NewExchangeView.Presenter {
    private final NewExchangeView view;
    private final NewExchangePlace place;
    private final PlaceController placeController;

    public NewExchangeActivity(NewExchangeView view, NewExchangePlace place, PlaceController placeController) {
        this.view = view;
        this.place = place;
        this.placeController = placeController;
    }
    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        view.setData(place.getReceiverUserEmail(), place.getSelectedCardId());
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
