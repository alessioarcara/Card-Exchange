package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.Deck;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class DeckWidget extends Composite {
    private static final DeckUIBinder uiBinder = GWT.create(DeckUIBinder.class);
    @UiField
    HeadingElement deckName;
    @UiField
    HTMLPanel cards;
    @UiField
    Button showButton;
    boolean isVisible = false;

    public DeckWidget(Deck deck) {
        initWidget(uiBinder.createAndBindUi(this));
        deckName.setInnerText(deck.getName());
        for (Integer physicalCardInteger : deck.getPhysicalCards()) {
            cards.add(new PhysicalCardWidget(physicalCardInteger));
        }
        cards.setVisible(isVisible);
        showButton.addClickHandler(e -> {
            cards.setVisible(!isVisible);
            isVisible = !isVisible;
        });
    }

    interface DeckUIBinder extends UiBinder<Widget, DeckWidget> {
    }
}