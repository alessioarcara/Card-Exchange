package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;


public class NewExchangeViewImpl extends Composite implements NewExchangeView {
    private static final NewExchangeViewImpl.NewExchangeViewImplUIBinder uiBinder = GWT.create(NewExchangeViewImpl.NewExchangeViewImplUIBinder.class);
    Presenter presenter;
    String selectedCardId;
    @UiField(provided=true)
    DeckWidget senderDeck;
    @UiField(provided=true)
    DeckWidget receiverDeck;
    @UiField
    Button cancelButton;
    @UiField
    Button acceptButton;

    public NewExchangeViewImpl() {
        this.senderDeck = new DeckWidget(null, "Your Owned Cards");
        this.receiverDeck = new DeckWidget(null, "");
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setData(String receiverUserEmail, String selectedCardId) {
        receiverDeck.setDeckName(receiverUserEmail);
        this.selectedCardId = selectedCardId;
    }

    public void setSenderDeck(List<PhysicalCardWithName> physicalCards) {
        senderDeck.setData(physicalCards, null);
    }

    public void setReceiverDeck(List<PhysicalCardWithName> physicalCards) {
        receiverDeck.setData(physicalCards, selectedCardId);
    }

    @Override
    public void setPresenter(NewExchangeView.Presenter presenter) {
        this.presenter = presenter;
    }

    interface NewExchangeViewImplUIBinder extends UiBinder<Widget, NewExchangeViewImpl> {
    }
}
