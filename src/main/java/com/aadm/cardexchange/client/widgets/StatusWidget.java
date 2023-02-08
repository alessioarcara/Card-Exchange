package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class StatusWidget extends Composite {
    private static final StatusWidgetUiBinder uiBinder = GWT.create(StatusWidgetUiBinder.class);
    @UiField
    ListBox status;

    public StatusWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public String getSelection() {
        return status.getSelectedValue();
    }

    public void clearSelection() {
        status.setItemSelected(0, true);
    }

    interface StatusWidgetUiBinder extends UiBinder<Widget, StatusWidget> {
    }
}