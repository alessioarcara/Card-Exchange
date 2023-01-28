package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class DeckWidget extends Composite {
    private static final DeckUIBinder uiBinder = GWT.create(DeckUIBinder.class);
    @UiField
    HeadingElement deckName;
    @UiField
    HTMLPanel cardsContainer;


    public DeckWidget(Deck deck) {
        initWidget(uiBinder.createAndBindUi(this));
        /*for (PhysicalCard card : deck.getPhysicalCards()) {
            cardsContainer.add(new PhysicalCardWidget(card, ));
        }*/
        deckName.setInnerText(deck.getName());
    }

    interface DeckUIBinder extends UiBinder<Widget, DeckWidget> {
    }
}