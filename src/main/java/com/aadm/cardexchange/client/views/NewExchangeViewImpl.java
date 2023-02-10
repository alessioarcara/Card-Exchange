package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardSelection;
import com.aadm.cardexchange.client.places.ExchangesPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
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
    HeadingElement pageSubtitle;
    @UiField
    HTMLPanel exchangeDecks;
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
    public void setData(boolean clickable, String title, String subtitle) {
        pageTitle.setInnerText(title);
        pageSubtitle.setInnerText(subtitle);
        exchangeDecks.clear();
        senderDeck = createDeck(clickable, "Your Owned Cards");
        receiverDeck = createDeck(clickable, "");
        exchangeDecks.add(senderDeck);
        exchangeDecks.add(receiverDeck);
    }

    private DeckWidget createDeck(boolean clickable, String deckName) {
        return new DeckWidget(null, null, clickable ? this : null, null, deckName);
    }

    @Override
    public void setSenderDeck(List<PhysicalCardWithName> physicalCards, String selectedCardId) {
        senderDeck.setData(physicalCards, selectedCardId);
    }

    @Override
    public void setReceiverDeck(List<PhysicalCardWithName> physicalCards, String selectedCardId, String receiverUserEmail) {
        receiverDeck.setData(physicalCards, selectedCardId);
        receiverDeck.setDeckName(receiverUserEmail);
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

    @Override
    public void setNewExchangeButtons() {
        handlers.add(cancelButton.addClickHandler(e -> newExchangePresenter.goTo(new HomePlace())));
        handlers.add(acceptButton.addClickHandler(e -> newExchangePresenter.createProposal(senderDeck.getDeckSelectedCards(), receiverDeck.getDeckSelectedCards())));
    }

    @Override
    public void setExchangeButtons() {
        handlers.add(cancelButton.addClickHandler(e -> exchangePresenter.goTo(new ExchangesPlace(null))));
        handlers.add(acceptButton.addClickHandler(e -> exchangePresenter.acceptExchangeProposal()));
    }

    @Override
    public void resetHandlers() {
        handlers.forEach(HandlerRegistration::removeHandler);
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

    interface NewExchangeViewImplUIBinder extends UiBinder<Widget, NewExchangeViewImpl> {
    }

}
