package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.handlers.ImperativeHandleAddCardToDeckModal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class AddCardToDeckModalWidget extends DialogBox {
    @UiField
    StatusWidget status;
    @UiField
    TextArea description;
    @UiField
    Button noButton;
    @UiField
    Button yesButton;
    private static final AddCardToDeckModalUiBinder uiBinder = GWT.create(AddCardToDeckModalUiBinder.class);

    public AddCardToDeckModalWidget(ImperativeHandleAddCardToDeckModal parent) {
        setWidget(uiBinder.createAndBindUi(this));
        setAutoHideEnabled(true);
        setModal(true);
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setStyleName("my-Modal");
        noButton.addClickHandler(clickEvent -> hide());
        yesButton.addClickHandler(clickEvent -> parent.onClickModalYes(status.getSelection(), description.getValue()));
    }

    @Override
    public void hide() {
        status.clearSelection();
        description.setText("");
        super.hide();
    }

    interface AddCardToDeckModalUiBinder extends UiBinder<Widget, AddCardToDeckModalWidget> {
    }
}