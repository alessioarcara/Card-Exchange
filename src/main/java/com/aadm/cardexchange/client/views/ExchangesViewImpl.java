package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.ProposalListWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExchangesViewImpl extends Composite implements ExchangesView {
    private static final ExchangesViewImplUIBinder uiBinder = GWT.create(ExchangesViewImplUIBinder.class);
    @UiField
    HTMLPanel proposalLists;

    public ExchangesViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        proposalLists.add(new ProposalListWidget("From you", "Receiver"));
        proposalLists.add(new ProposalListWidget("To you", "Sender"));
    }

    @Override
    public void setPresenter(Presenter presenter) {
    }

    interface ExchangesViewImplUIBinder extends UiBinder<Widget, ExchangesViewImpl> {
    }
}
