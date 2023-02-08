package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.Proposal;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface ExchangesView extends IsWidget {
    void setFromYouProposalList(List<Proposal> proposals);

    void setToYouProposalList(List<Proposal> proposals);

    void setPresenter(Presenter presenter);

    interface Presenter {

    }
}
