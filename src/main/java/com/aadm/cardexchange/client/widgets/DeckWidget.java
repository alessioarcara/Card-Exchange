package com.aadm.cardexchange.client.widgets;

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

    public DeckWidget(String name) {
        initWidget(uiBinder.createAndBindUi(this));
        deckName.setInnerText(name);
        for (int i = 0; i < 20; i++) {
            cardsContainer.add(new PhysicalCardWidget());
        }
    }
    
    interface DeckUIBinder extends UiBinder<Widget, DeckWidget> {
    }
}