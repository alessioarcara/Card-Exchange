package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.CardsView;
import com.aadm.cardexchange.shared.CardImpl;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CardsActivity extends AbstractActivity implements CardsView.Presenter {
    private final CardServiceAsync rpcService;
    private final CardsView view;

    public CardsActivity(CardsView view, CardServiceAsync rpcService) {
        this.view = view;
        this.rpcService = rpcService;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());

        fetchCards();
    }

    public void fetchCards() {
        rpcService.getCards(new BaseAsyncCallback<CardImpl[]>() {
            @Override
            public void onSuccess(CardImpl[] result) {
                view.setData(result);
            }
        });
    }

    @Override
    public boolean addCard(String cardName, String cardDesc) {
        // TODO: RPC addCard(String cardName, String cardDesc)
        return true;
    }

    @Override
    public boolean removeCard(String cardId) {
        // TODO: RPC removeCard(String cardId)
        return true;
    }
}
