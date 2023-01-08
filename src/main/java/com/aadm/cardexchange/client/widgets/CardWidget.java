package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CardWidget extends Composite {
    private static final CardUIBinder uiBinder = GWT.create(CardUIBinder.class);
    @UiField
    DivElement nameDiv;
    @UiField
    DivElement descDiv;
    @UiField
    Button deleteButton;

    public CardWidget(FunctionInterface parent, String nameText, String descText) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(nameText);
        descDiv.setInnerHTML(descText);
        deleteButton.addClickHandler(clickEvent -> parent.handleRemoveData(this));
    }

    public String getId() {
        return "qualcosa";
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
