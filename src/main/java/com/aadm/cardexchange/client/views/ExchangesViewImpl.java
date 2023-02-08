package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.handlers.ImperativeHandleProposalList;
import com.aadm.cardexchange.client.widgets.ProposalListWidget;
import com.aadm.cardexchange.shared.models.Proposal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class ExchangesViewImpl extends Composite implements ExchangesView, ImperativeHandleProposalList {
    private static final ExchangesViewImplUIBinder uiBinder = GWT.create(ExchangesViewImplUIBinder.class);
    Presenter presenter;
    @UiField (provided = true)
    ProposalListWidget fromYouProposalList;
    @UiField (provided = true)
    ProposalListWidget toYouProposalList;

    public ExchangesViewImpl() {
        fromYouProposalList = new ProposalListWidget("From you", "Receiver");
        toYouProposalList = new ProposalListWidget("To you", "Sender");
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setFromYouProposalList(List<Proposal> proposals) {
        proposals.forEach(proposal ->
                fromYouProposalList.addRow(proposal.getId(), "08-02-2023", proposal.getReceiverUserEmail(), this)
        );
    }

    @Override
    public void setToYouProposalList(List<Proposal> proposals) {
        proposals.forEach(proposal ->
                toYouProposalList.addRow(proposal.getId(), "08-02-2023", proposal.getSenderUserEmail(), this)
        );
    }

    @Override
    public void onClickProposalRow(int selectedProposalId) {
        // There will be the code for opening the Proposal Page
        Window.alert("This is the " + selectedProposalId + " proposal");
    }

    @Override
    public void resetProposalLists() {
        fromYouProposalList.resetTable();
        toYouProposalList.resetTable();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface ExchangesViewImplUIBinder extends UiBinder<Widget, ExchangesViewImpl> {
    }
}
