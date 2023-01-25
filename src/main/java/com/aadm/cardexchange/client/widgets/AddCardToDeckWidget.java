package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AddCardToDeckWidget extends Composite {
    private static final AddCardToDeckWidgetUiBinder addCardTODeckUiBinder = GWT.create(AddCardToDeckWidgetUiBinder.class);

    public AddCardToDeckWidget() {
        initWidget(addCardTODeckUiBinder.createAndBindUi(this));
    }

    interface AddCardToDeckWidgetUiBinder extends UiBinder<Widget, AddCardToDeckWidget> {
    }
}