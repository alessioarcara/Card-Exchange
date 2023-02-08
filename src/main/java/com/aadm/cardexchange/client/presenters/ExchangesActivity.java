package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.views.ExchangesView;
import com.aadm.cardexchange.shared.models.Proposal;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExchangesActivity extends AbstractActivity implements ExchangesView.Presenter {
    private final ExchangesView view;
    List<Proposal> proposals = Arrays.asList(
            new Proposal("alessiacrimaldi@gmail.com", "alessioarcara@gmail.com", Collections.emptyList(), Collections.emptyList()),
            new Proposal("matteosacco@gmail.com", "davidefermi@gmail.com", Collections.emptyList(), Collections.emptyList())
    );

    public ExchangesActivity(ExchangesView view) {
        this.view = view;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        view.setFromYouProposalList(proposals);
        view.setToYouProposalList(proposals);
    }
}
