package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;
import java.util.List;

public class DeckWidget extends Composite implements ImperativeHandlePhysicalCard {
    private static final DeckUIBinder uiBinder = GWT.create(DeckUIBinder.class);
    @UiField
    HeadingElement deckName;
    @UiField
    HTMLPanel cards;
    @UiField
    Button showButton;
    boolean isVisible;
    List<PhysicalCard> deckSelectedCards;
    ImperativeHandleCardsSelection parent2;

    @UiConstructor
    public DeckWidget(ImperativeHandleDeck parent1, ImperativeHandleCardsSelection parent2, String name) {
        initWidget(uiBinder.createAndBindUi(this));
        setDeckName(name);
        isVisible = (parent1 == null);
        showButton.setVisible(!isVisible);

        cards.setVisible(isVisible);
        if (!isVisible) {
            showButton.addClickHandler(e -> {
                if (isVisible = !isVisible) {
                    parent1.onShowDeck(name, this::setData);
                }
                cards.setVisible(isVisible);
            });
        }
        this.parent2 = parent2;
    }

    public void setData(List<PhysicalCardWithName> data, String selectedCardId) {
        cards.clear();
        for (PhysicalCardWithName pCard : data) {
            PhysicalCardWidget pCardWidget = new PhysicalCardWidget(pCard, this);
            if (pCard.getId().equals(selectedCardId)) {
                pCardWidget.setSelected();
            }
            cards.add(pCardWidget);
        }
        onChangeSelection();
    }

    public void setDeckName(String name) {
        deckName.setInnerText(name);
    }

    public List<PhysicalCard> getDeckSelectedCards() {
        return deckSelectedCards;
    }

    void setDeckSelectedCards() {
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
        setDeckSelectedCards();
        parent2.onChangeSelectedCards();
    }

    interface DeckUIBinder extends UiBinder<Widget, DeckWidget> {
    }
}