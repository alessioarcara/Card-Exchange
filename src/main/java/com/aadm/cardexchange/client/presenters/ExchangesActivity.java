package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.ExchangesView;
import com.aadm.cardexchange.shared.ExchangeServiceAsync;
import com.aadm.cardexchange.shared.models.Proposal;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.List;

public class ExchangesActivity extends AbstractActivity implements ExchangesView.Presenter {
    private final ExchangesView view;
    private final ExchangeServiceAsync rpcService;
    private final AuthSubject authSubject;

    public ExchangesActivity(ExchangesView view, ExchangeServiceAsync rpcService, AuthSubject authSubject) {
        this.view = view;
        this.rpcService = rpcService;
        this.authSubject = authSubject;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        fetchSentProposals();
        fetchReceivedProposals();
    }

    private void fetchSentProposals() {
        rpcService.getProposalListSend(authSubject.getToken(), new BaseAsyncCallback<List<Proposal>>() {
            @Override
            public void onSuccess(List<Proposal> proposals) {
                view.setFromYouProposalList(proposals);
            }
        });
    }

    private void fetchReceivedProposals() {
        rpcService.getProposalListReceived(authSubject.getToken(), new BaseAsyncCallback<List<Proposal>>() {
            @Override
            public void onSuccess(List<Proposal> proposals) {
                view.setToYouProposalList(proposals);
            }
        });
    }

    @Override
    public void onStop() {
        view.resetProposalLists();
    }
}
