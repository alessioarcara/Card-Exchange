package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.handlers.ImperativeHandleCardsSelection;
import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;


public class NewExchangeViewImpl extends Composite implements NewExchangeView, ImperativeHandleCardsSelection {
    private static final NewExchangeViewImpl.NewExchangeViewImplUIBinder uiBinder = GWT.create(NewExchangeViewImpl.NewExchangeViewImplUIBinder.class);
    Presenter presenter;
    @UiField(provided = true)
    DeckWidget senderDeck;
    @UiField(provided = true)
    DeckWidget receiverDeck;
    @UiField
    Button cancelButton;
    @UiField
    Button acceptButton;

    public NewExchangeViewImpl() {
        this.senderDeck = new DeckWidget(null, this, "Your Owned Cards");
        this.receiverDeck = new DeckWidget(null, this, "");
        initWidget(uiBinder.createAndBindUi(this));
        cancelButton.addClickHandler(e -> Window.alert("Abort Exchange"));
        acceptButton.addClickHandler(e -> presenter.createProposal(senderDeck.getDeckSelectedCards(), receiverDeck.getDeckSelectedCards()));
    }

    public void setReceiverDeckName(String receiverUserEmail) {
        receiverDeck.setDeckName(receiverUserEmail);
    }

    public void setSenderDeck(List<PhysicalCardWithName> physicalCards) {
        senderDeck.setData(physicalCards, null);
    }

    public void setReceiverDeck(List<PhysicalCardWithName> physicalCards, String selectedCardId) {
        receiverDeck.setData(physicalCards, selectedCardId);
    }

    @Override
    public void setPresenter(NewExchangeView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void onChangeSelectedCards() {
        acceptButton.setEnabled(!receiverDeck.getDeckSelectedCards().isEmpty() && !senderDeck.getDeckSelectedCards().isEmpty());
    }

    interface NewExchangeViewImplUIBinder extends UiBinder<Widget, NewExchangeViewImpl> {
    }
}
