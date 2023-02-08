package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class ProposalListWidget extends Composite {
    private static final ProposalListUiBinder uiBinder = GWT.create(ProposalListUiBinder.class);
    @UiField
    HeadingElement tableHeading;
    @UiField(provided = true)
    FlexTable table;

    public ProposalListWidget(String title, String otherUser) {
        setupTable(otherUser);
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
    }

    private void setupTable(String otherUser) {
        table = new FlexTable();
        TableSectionElement tHead = ((TableElement) ((Element) table.getElement())).createTHead();
        TableRowElement row = tHead.insertRow(0);
        row.insertCell(0).setInnerText("ID");
        row.insertCell(1).setInnerText("Date");
        row.insertCell(2).setInnerText(otherUser);
        addRow("12212", "08-02-2023", "alessiacrimaldi@virgilio.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
        addRow("12212", "08-02-2023", "test@test.it");
    }

    private void addRow(String ID, String date, String email) {
        int numRows = (table.getRowCount());
        table.setText(numRows, 0, ID);
        table.setText(numRows, 1, date);
        table.setText(numRows, 2, email);
    }

    interface ProposalListUiBinder extends UiBinder<Widget, ProposalListWidget> {
    }
}
