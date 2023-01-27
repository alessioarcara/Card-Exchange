package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class PhysicalCardWidget extends Composite {
    private static final PhysicalCardUiBinder uiBinder = GWT.create(PhysicalCardUiBinder.class);
    @UiField
    HTMLPanel cardContainer;
    @UiField
    Button deleteButton;

    public PhysicalCardWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        cardContainer.addDomHandler(e -> {
            Window.alert("HAI CLICCATO SULLA CARTA");
        }, ClickEvent.getType());

        deleteButton.addClickHandler(e -> {
            e.stopPropagation();
            Window.alert("CANCELLATO");
        });
    }

    interface PhysicalCardUiBinder extends UiBinder<Widget, PhysicalCardWidget> {
    }
}