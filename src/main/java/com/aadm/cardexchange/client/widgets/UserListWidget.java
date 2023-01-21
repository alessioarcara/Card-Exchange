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

    @UiConstructor public UserListWidget(String title, String[] users) {
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
        for (String user: users) {
            addRow(user);
        }
    }

    private void setupTable() {
        table = new FlexTable();
        TableSectionElement tHead = ((TableElement) ((Element) table.getElement())).createTHead();
        TableRowElement row = tHead.insertRow(0);
        row.insertCell(0).setInnerText("User");
        row.insertCell(1).setInnerText("State");
        row.insertCell(2).setInnerText("");
    }

    private void addRow(String email) {
        int numRows = (table.getRowCount());
        table.setText(numRows, 0, email);
        table.setText(numRows, 1, "1 (Ottimo)");
        table.setWidget(numRows, 2, new Button("Exchange"));
    }

    interface UserListUIBinder extends UiBinder<Widget, UserListWidget> {
    }
}
