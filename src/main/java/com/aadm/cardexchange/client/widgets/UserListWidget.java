package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCardWithEmail;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class UserListWidget extends Composite {
    private static final UserListUIBinder uiBinder = GWT.create(UserListUIBinder.class);
    @UiField
    HeadingElement tableHeading;
    @UiField(provided = true)
    FlexTable table;
    boolean showExchangeButton;
    ImperativeHandleUserList parent;

    @UiConstructor
    public UserListWidget(String title, boolean showExchangeButton, ImperativeHandleUserList parent) {
        this.showExchangeButton = showExchangeButton;
        this.parent = parent;
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
    }

    private void setupTable() {
        table = new FlexTable();
        TableSectionElement tHead = ((TableElement) ((Element) table.getElement())).createTHead();
        TableRowElement row = tHead.insertRow(0);
        row.insertCell(0).setInnerText("User");
        row.insertCell(1).setInnerText("Status");
        if (showExchangeButton) row.insertCell(2).setInnerText("");
    }

    public void setTable(List<PhysicalCardWithEmail> pCards) {
        pCards.forEach(pCard -> addRow(pCard.getEmail(), pCard.getStatus(), new Button(
                "Exchange", (ClickHandler) event -> parent.onClickExchange(pCard.getEmail(), "y2")
        )));
    }

    private void addRow(String email, Status status, Button button) {
        int numRows = (table.getRowCount());
        table.setText(numRows, 0, email);
        table.setText(numRows, 1, (status.getValue() + " (" + status.name() + ")"));
        if (showExchangeButton) table.setWidget(numRows, 2, button);
    }

    interface UserListUIBinder extends UiBinder<Widget, UserListWidget> {
    }
}
