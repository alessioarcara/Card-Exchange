package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class UserListWidget extends Composite {
    private static final UserListUIBinder uiBinder = GWT.create(UserListUIBinder.class);
    @UiField
    HeadingElement tableHeading;
    @UiField(provided = true)
    FlexTable table;
    boolean showExchangeButton;
    ImperativeHandlerExchangeCard parent;

    @UiConstructor
    public UserListWidget(String title, String[] users, boolean showExchangeButton, ImperativeHandlerExchangeCard parent) {
        this.showExchangeButton = showExchangeButton;
        this.parent = parent;
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
        for (String user: users) {
            addRow(user, new Button("Exchange"));
        }
    }

    private void setupTable() {
        table = new FlexTable();
        TableSectionElement tHead = ((TableElement) ((Element) table.getElement())).createTHead();
        TableRowElement row = tHead.insertRow(0);
        row.insertCell(0).setInnerText("User");
        row.insertCell(1).setInnerText("State");
        if (showExchangeButton) row.insertCell(2).setInnerText("");
    }

    private void addRow(String email, Button button) {
        int numRows = (table.getRowCount());
        table.setText(numRows, 0, email);
        table.setText(numRows, 1, "1 (Ottimo)");
        button.addClickHandler(clickEvent -> parent.onClickExchange(email, "y123123"));
        if (showExchangeButton) table.setWidget(numRows, 2, button);
    }

    interface UserListUIBinder extends UiBinder<Widget, UserListWidget> {
    }
}
