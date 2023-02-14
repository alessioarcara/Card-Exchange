package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardSelection;
import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class NewExchangeViewImpl extends Composite implements NewExchangeView, ImperativeHandlePhysicalCardSelection {
    private static final NewExchangeViewImpl.NewExchangeViewImplUIBinder uiBinder = GWT.create(NewExchangeViewImpl.NewExchangeViewImplUIBinder.class);
    NewExchangePresenter newExchangePresenter;
    ExchangePresenter exchangePresenter;
    @UiField
    HeadingElement pageTitle;
    @UiField
    SimplePanel senderDeckContainer;
    @UiField
    SimplePanel receiverDeckContainer;
    @UiField
    Button cancelButton;
    @UiField
    Button acceptButton;
    List<HandlerRegistration> handlers;
    DeckWidget senderDeck;
    DeckWidget receiverDeck;

    public NewExchangeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        handlers = new ArrayList<>();
    }

    @Override
    public void setData(String title, String cancelTextButton, String acceptTextButton) {
        pageTitle.setInnerText(title);
        cancelButton.setText(cancelTextButton);
        acceptButton.setText(acceptTextButton);
    }

    private DeckWidget createDeck(boolean clickable, String deckName) {
        return new DeckWidget(null, null, clickable ? this : null, null, deckName);
    }

    @Override
    public void setSenderDeck(boolean isClickable, List<PhysicalCardWithName> physicalCards, String selectedCardId, String senderEmail) {
        senderDeck = createDeck(isClickable, "Proposal sender: " + senderEmail);
        senderDeck.setData(physicalCards, selectedCardId);
        senderDeckContainer.add(senderDeck);
    }

    @Override
    public void setReceiverDeck(boolean isClickable, List<PhysicalCardWithName> physicalCards, String selectedCardId, String receiverEmail) {
        receiverDeck = createDeck(isClickable, "Proposal receiver: " + receiverEmail);
        receiverDeck.setData(physicalCards, selectedCardId);
        receiverDeckContainer.add(receiverDeck);
    }

    @Override
    public void setPresenter(NewExchangePresenter newExchangePresenter) {
        this.exchangePresenter = null;
        this.newExchangePresenter = newExchangePresenter;
    }

    @Override
    public void setPresenter(ExchangePresenter exchangePresenter) {
        this.newExchangePresenter = null;
        this.exchangePresenter = exchangePresenter;
    }

    private String confirmMessage(String str) {
        return "Do you want to " + str + " this exchange proposal?";
    }

    @Override
    public void setNewExchangeButtons() {
        handlers.add(cancelButton.addClickHandler(e -> History.back()));
        handlers.add(acceptButton.addClickHandler(e -> {
            if (Window.confirm(confirmMessage("send")))
                newExchangePresenter.createProposal(senderDeck.getDeckSelectedCards(), receiverDeck.getDeckSelectedCards());
        }));
    }

    @Override
    public void setExchangeButtons() {
        handlers.add(cancelButton.addClickHandler(e -> {
            if (Window.confirm(confirmMessage("cancel"))) {
                exchangePresenter.refuseOrWithdrawProposal();
                History.back();
            }
        }));
        handlers.add(acceptButton.addClickHandler(e -> {
            if (Window.confirm(confirmMessage("accept"))) {
                exchangePresenter.acceptExchangeProposal();
                History.back();
            }
        }));
    }

    @Override
    public void resetHandlers() {
        handlers.forEach(HandlerRegistration::removeHandler);
        senderDeckContainer.clear();
        receiverDeckContainer.clear();
    }

    @Override
    public void setAcceptButtonEnabled(boolean enabled) {
        acceptButton.setEnabled(enabled);
    }

    @Override
    public void showAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void onChangeSelection() {
        acceptButton.setEnabled(receiverDeck != null && receiverDeck.getDeckSelectedCards() != null && !receiverDeck.getDeckSelectedCards().isEmpty()
                && senderDeck != null && senderDeck.getDeckSelectedCards() != null && !senderDeck.getDeckSelectedCards().isEmpty());
    }

    interface NewExchangeViewImplUIBinder extends UiBinder<Widget, NewExchangeViewImpl> {
    }

}
