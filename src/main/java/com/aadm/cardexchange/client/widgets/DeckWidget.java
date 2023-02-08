package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.handlers.*;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class DeckWidget extends Composite implements ImperativeHandlePhysicalCardSelection, ImperativeHandlePhysicalCardRemove, ImperativeHandlePhysicalCardEdit {
    private static final DeckUIBinder uiBinder = GWT.create(DeckUIBinder.class);
    @UiField
    HeadingElement deckName;
    @UiField
    HTMLPanel cards;
    @UiField
    Button showButton;
    boolean isVisible;
    List<PhysicalCard> deckSelectedCards;
    ImperativeHandleDeck deckHandler;
    ImperativeHandlePhysicalCardSelection cardSelectionHandler;
    ImperativeHandlePhysicalCardEdit cardEditHandler;
    @UiField
    HTMLPanel actions;

    @UiConstructor
    public DeckWidget(ImperativeHandleDeck deckHandler, ImperativeHandleDeckRemove customDeckHandler, ImperativeHandlePhysicalCardSelection cardSelectionHandler,
                      ImperativeHandlePhysicalCardEdit cardEditHandler, String name) {
        this.deckHandler = deckHandler;
        this.cardSelectionHandler = cardSelectionHandler;
        this.cardEditHandler = cardEditHandler;
        initWidget(uiBinder.createAndBindUi(this));
        setDeckName(name);
        isVisible = (deckHandler == null);
        showButton.setVisible(!isVisible);
        cards.setVisible(isVisible);

        if (!isVisible) {
            showButton.addClickHandler(e -> {
                if (isVisible = !isVisible) {
                    deckHandler.onShowDeck(name, this::setData);
                }
                cards.setVisible(isVisible);
            });
        }

        if (customDeckHandler != null) {
            Button removeButton = new Button("x", (ClickHandler) e -> {
                if (Window.confirm("Are you sure you want to remove this deck?"))
                    customDeckHandler.onClickRemoveCustomDeck(deckName.getInnerText(), (Boolean isRemoved) -> {
                        if (isRemoved) {
                            removeFromParent();
                        } else {
                            Window.alert("Deck cannot be deleted. It may be a default deck or does not exist.");
                        }
                    });
            });
            removeButton.setStyleName("deckButton");
            actions.add(removeButton);
        }
    }

    public void setData(List<PhysicalCardWithName> data, String selectedCardId) {
        cards.clear();
        for (PhysicalCardWithName pCard : data) {
            PhysicalCardWidget pCardWidget = createPhysicalCardWidget(pCard);
            if (pCard.getId().equals(selectedCardId)) {
                pCardWidget.setSelected();
            }
            cards.add(pCardWidget);
        }
        onChangeSelection();
    }

    // factory
    private PhysicalCardWidget createPhysicalCardWidget(PhysicalCardWithName pCard) {
        return new PhysicalCardWidget(pCard, this, deckHandler != null ? this : null, cardEditHandler != null ? this : null);
    }

    public void setDeckName(String name) {
        deckName.setInnerText(name);
    }

    public List<PhysicalCard> getDeckSelectedCards() {
        return deckSelectedCards;
    }

    private void setDeckSelectedCards() {
        List<PhysicalCard> selectedCards = new LinkedList<>();
        cards.forEach((widget) -> {
            PhysicalCardWidget physicalCardWidget = (PhysicalCardWidget) widget;
            if (physicalCardWidget.getSelected())
                selectedCards.add(physicalCardWidget.getPhysicalCard());
        });
        deckSelectedCards = selectedCards;
    }

    @Override
    public void onChangeSelection() {
        if (cardSelectionHandler != null) {
            setDeckSelectedCards();
            cardSelectionHandler.onChangeSelection();
        }
    }

    @Override
    public void onClickDeleteButton(PhysicalCard pCard, Consumer<Boolean> isRemoved) {
        if (deckHandler != null) {
            deckHandler.onRemovePhysicalCard(deckName.getInnerText(), pCard, isRemoved);
        }
    }

    @Override
    public void onConfirmCardEdit() {
        cardEditHandler.onConfirmCardEdit();
    }

    interface DeckUIBinder extends UiBinder<Widget, DeckWidget> {
    }
}