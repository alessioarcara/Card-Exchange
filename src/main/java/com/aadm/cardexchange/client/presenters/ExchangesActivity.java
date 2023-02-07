package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.views.ExchangesView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ExchangesActivity extends AbstractActivity implements ExchangesView.Presenter {
    private final ExchangesView view;

    public ExchangesActivity(ExchangesView view) {
        this.view = view;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
    }
}
