package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.handlers.ImperativeHandleCustomDeck;
import com.aadm.cardexchange.client.handlers.ImperativeHandleDeck;
import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardEdit;
import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardSelection;
import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DecksViewImpl extends Composite implements DecksView, ImperativeHandleDeck, ImperativeHandleCustomDeck,
        ImperativeHandlePhysicalCardSelection, ImperativeHandlePhysicalCardEdit {
    private static final DecksViewImpl.DecksViewImplUIBinder uiBinder = GWT.create(DecksViewImpl.DecksViewImplUIBinder.class);
    private static final String DEFAULT_CUSTOM_DECK_TEXT = "Write here your custom deck name";
    Presenter presenter;
    @UiField
    HTMLPanel decksContainer;
    @UiField
    HeadingElement newDeckName;
    DeckWidget ownedDeck;

    public DecksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setData(List<String> data) {
        for (String deckName : data) {
            boolean isOwned = deckName.equals("Owned");
            DeckWidget deck = createDeck(deckName);
            if (isOwned) ownedDeck = deck;
            decksContainer.add(deck);
        }
    }

    @Override
    public void onShowDeck(String deckName, BiConsumer<List<PhysicalCardWithName>, String> setDeckData) {
        presenter.fetchUserDeck(deckName, setDeckData);
    }

    @Override
    public void onRemovePhysicalCard(String deckName, PhysicalCard pCard, Consumer<List<Deck>> isRemoved) {
        presenter.removePhysicalCardFromDeck(deckName, pCard, isRemoved);
    }

    @Override
    public void resetData() {
        decksContainer.clear();
    }

    @Override
    public void displayAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void displayAddedCustomDeck(String deckName) {
        decksContainer.add(createDeck(deckName));
        newDeckName.setInnerText(DEFAULT_CUSTOM_DECK_TEXT);
        onChangeSelection();
    }

    // factory
    private DeckWidget createDeck(String deckName) {
        if (deckName.equals("Owned")) {
            return new DeckWidget(this, null, this, this, deckName);
        } else if (deckName.equals("Wished")) {
            return new DeckWidget(this, null, null, this, deckName);
        } else {
            return new DeckWidget(this, this, null, null, deckName);
        }
    }

    @Override
    public void onShowDeck(String deckName, BiConsumer<List<PhysicalCardWithName>, String> setDeckData) {
        presenter.fetchUserDeck(deckName, setDeckData);
    }

    @Override
    public void onClickRemoveCustomDeck(String deckName, Consumer<Boolean> isRemoved) {
        presenter.deleteCustomDeck(deckName, isRemoved);
    }

    @Override
    public void onClickAddPhysicalCardsToCustomDeck(String deckName, Consumer<List<PhysicalCardWithName>> updateCustomDeck) {
        presenter.addPhysicalCardsToCustomDeck(deckName, ownedDeck.getDeckSelectedCards(), updateCustomDeck);
    }

    @Override
    public void onRemovePhysicalCard(String deckName, PhysicalCard pCard, Consumer<Boolean> isRemoved) {
        presenter.removePhysicalCardFromDeck(deckName, pCard, isRemoved);
    }

    @UiHandler(value = "newDeckButton")
    public void onClickCustomDeckAdd(ClickEvent e) {
        presenter.createCustomDeck(newDeckName.getInnerText());
    }

    @Override
    public void setPresenter(DecksView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onConfirmCardEdit() {
        presenter.updatePhysicalCard();
    }

    @Override
    public void onChangeSelection() {
        for (Widget w : decksContainer)
            ((DeckWidget) w).setAddButtonEnabled(ownedDeck.getDeckSelectedCards() != null && !ownedDeck.getDeckSelectedCards().isEmpty());
    }

    interface DecksViewImplUIBinder extends UiBinder<Widget, DecksViewImpl> {
    }
}
