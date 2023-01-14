package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class CardWidget extends Composite {
    private static final CardUIBinder uiBinder = GWT.create(CardUIBinder.class);
    @UiField
    DivElement nameDiv;
    @UiField
    DivElement descDiv;
    @UiField
    DivElement typeDiv;
    @UiField
    PushButton detailsButton;

    public CardWidget(ImperativeHandleCard parent, CardDecorator card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        descDiv.setInnerHTML(card.getDescription());
        typeDiv.setInnerHTML(card.getType());
        detailsButton.addClickHandler(clickEvent -> parent.handleClickCard());
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
