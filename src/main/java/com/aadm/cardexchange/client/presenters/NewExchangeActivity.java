package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.views.NewExchangeView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class NewExchangeActivity extends AbstractActivity implements NewExchangeView.Presenter {
    private final NewExchangeView view;
    private final NewExchangePlace place;

    public NewExchangeActivity(NewExchangeView view, NewExchangePlace place) {
        this.view = view;
        this.place = place;
    }
    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        view.setData(place.getReceiverUserEmail(), place.getSelectedCardId());
    }
}
