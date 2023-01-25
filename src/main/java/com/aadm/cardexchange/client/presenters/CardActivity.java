package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.AuthSubject.AuthSubject;
import com.aadm.cardexchange.client.AuthSubject.Observer;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CardActivity extends AbstractActivity implements CardView.Presenter, Observer {
    private final CardPlace place;
    private final CardView view;
    private final CardServiceAsync rpcService;
    private final AuthSubject authSubject;

    public CardActivity(CardPlace place, CardView view, CardServiceAsync rpcService, AuthSubject authSubject) {
        this.place = place;
        this.view = view;
        this.rpcService = rpcService;
        this.authSubject = authSubject;
        authSubject.attach(this);
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());
        fetchCard();
        update();
    }

    @Override
    public void update() {
        view.createUserWidgets(authSubject.isLoggedIn());
    }

    public void fetchCard() {
        rpcService.getGameCard(place.getGame(), place.getCardId(), new BaseAsyncCallback<CardDecorator>() {
            @Override
            public void onSuccess(CardDecorator result) {
                view.setData(result);
            }
        });
    }
}
