package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCardDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

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

    public DeckWidget(ImperativeHandleDeck parent, String name) {
        initWidget(uiBinder.createAndBindUi(this));
        deckName.setInnerText(name);

        cards.setVisible(isVisible);
        showButton.addClickHandler(e -> {
            if (isVisible = !isVisible) {
                parent.onShowDeck(name, this::setData);
            }
            cards.setVisible(isVisible);
        });
    }

    public void setData(List<PhysicalCardDecorator> data) {
        cards.clear();
        for (PhysicalCardDecorator pCard : data) {
            cards.add(new PhysicalCardWidget(pCard));
        }
    }

    interface DeckUIBinder extends UiBinder<Widget, DeckWidget> {
    }
}