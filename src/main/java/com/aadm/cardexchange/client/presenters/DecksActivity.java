package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.views.DecksView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class DecksActivity extends AbstractActivity implements DecksView.Presenter {
    private final DecksView view;

    public DecksActivity(DecksView view) {
        this.view = view;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
    }
}
