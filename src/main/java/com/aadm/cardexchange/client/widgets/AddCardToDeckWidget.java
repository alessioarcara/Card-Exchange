package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class AddCardToDeckWidget extends Composite {
    private static final AddCardToDeckUiBinder uiBinder = GWT.create(AddCardToDeckUiBinder.class);
    @UiField
    PushButton addToDeckButton;
    @UiField
    ListBox deckListBox;

    public AddCardToDeckWidget(ImperativeHandleAddCardToDeck parent) {
        initWidget(uiBinder.createAndBindUi(this));
        addToDeckButton.addClickHandler(clickEvent -> parent.onClickAddToDeck());
    }

    public String getDeckName() {
        return deckListBox.getSelectedValue();
    }

    interface AddCardToDeckUiBinder extends UiBinder<Widget, AddCardToDeckWidget> {
    }
}