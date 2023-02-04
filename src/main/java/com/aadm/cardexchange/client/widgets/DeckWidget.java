package com.aadm.cardexchange.client.widgets;

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

import java.util.ArrayList;
import java.util.List;

public class DeckWidget extends Composite {
    private static final DeckUIBinder uiBinder = GWT.create(DeckUIBinder.class);
    @UiField
    HeadingElement deckName;
    @UiField
    HTMLPanel cards;
    @UiField
    Button showButton;
    boolean isVisible = false;

    @UiConstructor
    public DeckWidget(ImperativeHandleDeck parent, String name) {
        initWidget(uiBinder.createAndBindUi(this));
        deckName.setInnerText(name);
        isVisible = (parent == null);
        showButton.setVisible(!isVisible);

        cards.setVisible(isVisible);
        if (!isVisible) {
            showButton.addClickHandler(e -> {
                if (isVisible = !isVisible) {
                    parent.onShowDeck(name, this::setData);
                }
                cards.setVisible(isVisible);
            });
        }
    }

    public void setData(List<PhysicalCardWithName> data) {
        cards.clear();
        for (PhysicalCardWithName pCard : data) {
            PhysicalCardWidget pCardWidget = new PhysicalCardWidget(pCard);
            cards.add(pCardWidget);
        }
    }

    public void setData(List<PhysicalCardWithName> data, String selectedCardId) {
        cards.clear();
        for (PhysicalCardWithName pCard : data) {
            PhysicalCardWidget pCardWidget = new PhysicalCardWidget(pCard);
            if (pCard.getId().equals(selectedCardId)) {
                pCardWidget.setSelected();
            }
            cards.add(pCardWidget);
        }
    }

    public List<PhysicalCardWidget> getDeckCards() {
        List<PhysicalCardWidget> tmp = new ArrayList<>();
        for (int i = 0; i < cards.getWidgetCount(); i++) {
            tmp.add((PhysicalCardWidget) cards.getWidget(i));
        }
        return tmp;
    }

    interface DeckUIBinder extends UiBinder<Widget, DeckWidget> {
    }
}