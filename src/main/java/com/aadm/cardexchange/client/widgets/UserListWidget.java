package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCardWithEmail;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.function.Function;

public class UserListWidget extends Composite {
    private static final UserListUIBinder uiBinder = GWT.create(UserListUIBinder.class);
    private static final String NO_CARDS_TEXT = "No users";
    @UiField
    HeadingElement tableHeading;
    @UiField(provided = true)
    FlexTable table;
    int noUsersRow;
    boolean showExchangeButton;

    @UiConstructor
    public UserListWidget(String title, boolean showExchangeButton) {
        this.showExchangeButton = showExchangeButton;
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
    }

    private void setNoUsersText() {
        noUsersRow = table.getRowCount();
        table.setText(noUsersRow, 0, NO_CARDS_TEXT);
        table.getFlexCellFormatter().setColSpan(noUsersRow, 0, 3);
    }

    private void setupTable() {
        table = new FlexTable();
        TableSectionElement tHead = ((TableElement) ((Element) table.getElement())).createTHead();
        TableRowElement row = tHead.insertRow(0);
        row.insertCell(0).setInnerText("User");
        row.insertCell(1).setInnerText("Status");
        setNoUsersText();
        if (showExchangeButton) row.insertCell(2).setInnerText("");
    }

    public void setTable(List<? extends PhysicalCardWithEmail> pCards, Function<PhysicalCardWithEmail, Button> createButton) {
        if (!pCards.isEmpty()) table.removeRow(noUsersRow);
        pCards.forEach(pCard -> addRow(pCard.getEmail(), pCard.getStatus(), createButton.apply(pCard)));
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
