package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CardActivity extends AbstractActivity implements CardView.Presenter {
    private final CardView view;
    private final CardServiceAsync rpcService;
    private final CardPlace place;

    public CardActivity(CardPlace place, CardView view, CardServiceAsync rpcService) {
        this.place = place;
        this.view = view;
        this.rpcService = rpcService;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());
    }
}
