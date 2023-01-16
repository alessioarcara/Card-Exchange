package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.views.AuthenticationView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AuthenticationActivity extends AbstractActivity implements AuthenticationView.Presenter {
    private final AuthenticationView view;

    public AuthenticationActivity(AuthenticationView view) {
        this.view = view;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
    }
}
