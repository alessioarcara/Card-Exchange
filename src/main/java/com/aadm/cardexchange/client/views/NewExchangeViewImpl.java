package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardSelection;
import com.aadm.cardexchange.client.places.ExchangesPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;


public class NewExchangeViewImpl extends Composite implements NewExchangeView, ImperativeHandlePhysicalCardSelection {
    private static final NewExchangeViewImpl.NewExchangeViewImplUIBinder uiBinder = GWT.create(NewExchangeViewImpl.NewExchangeViewImplUIBinder.class);
    NewExchangePresenter newExchangePresenter;
    ProposalPresenter proposalPresenter;
    @UiField
    HeadingElement pageTitle;
    @UiField
    HeadingElement pageSubtitle;
    @UiField(provided = true)
    DeckWidget senderDeck;
    @UiField(provided = true)
    DeckWidget receiverDeck;
    @UiField(provided = true)
    Button cancelButton;
    @UiField(provided = true)
    Button acceptButton;

    public NewExchangeViewImpl() {
        this.senderDeck = new DeckWidget(null, null, this, null, "Your Owned Cards");
        this.receiverDeck = new DeckWidget(null, null, this, null, "");
        this.cancelButton = new Button();
        this.acceptButton = new Button();
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setReceiverDeckName(String receiverUserEmail) {
        receiverDeck.setDeckName(receiverUserEmail);
    }

    public void setSenderDeck(List<PhysicalCardWithName> physicalCards, String selectedCardId) {
        senderDeck.setData(physicalCards, selectedCardId);
    }

    public void setReceiverDeck(List<PhysicalCardWithName> physicalCards, String selectedCardId) {
        receiverDeck.setData(physicalCards, selectedCardId);
    }

    @Override
    public void setPresenter(NewExchangePresenter newExchangePresenter) {
        this.newExchangePresenter = newExchangePresenter;
        pageTitle.setInnerText("New Exchange Page");
        pageSubtitle.setInnerText("Select the cards you want to exchange from both decks and propose a new exchange");

        cancelButton.addClickHandler(e -> newExchangePresenter.goTo(new HomePlace()));
        this.acceptButton.addClickHandler(e -> newExchangePresenter.createProposal(senderDeck.getDeckSelectedCards(), receiverDeck.getDeckSelectedCards()));
    }

    @Override
    public void setPresenter(ProposalPresenter proposalPresenter) {
        this.proposalPresenter = proposalPresenter;
        pageTitle.setInnerText("Proposal Page");
        pageSubtitle.setInnerText("Check the cards in the proposal before accepting or refusing it");

        cancelButton.addClickHandler(e -> proposalPresenter.goTo(new ExchangesPlace()));
        acceptButton.addClickHandler(e -> proposalPresenter.acceptProposal());
    }

    @Override
    public void setAcceptButtonEnabled(boolean enabled) {
        acceptButton.setEnabled(true);
    }

    @Override
    public void showAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void onChangeSelection() {
        acceptButton.setEnabled(!receiverDeck.getDeckSelectedCards().isEmpty() && !senderDeck.getDeckSelectedCards().isEmpty());
    }

    @Override
    public void resetAll() {
        this.newExchangePresenter = null;
        this.proposalPresenter = null;
        cancelButton.addClickHandler(e -> {});
        acceptButton.addClickHandler(e -> {});
    }

    interface NewExchangeViewImplUIBinder extends UiBinder<Widget, NewExchangeViewImpl> {
    }

}
