package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.handlers.ImperativeHandleProposalList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
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
    }

    public void addRow(int id, String date, String email, ImperativeHandleProposalList rowClickHandler) {
        int numRows = (table.getRowCount());
        table.setText(numRows, 0, String.valueOf(id));
        table.setText(numRows, 1, date);
        table.setText(numRows, 2, email);
        Element proposalRow = table.getRowFormatter().getElement(numRows);
        proposalRow.setTitle("view more");
        DOM.sinkEvents(proposalRow, Event.ONCLICK);
        DOM.setEventListener(proposalRow, event -> {
            if (Event.ONCLICK == event.getTypeInt()) {
                rowClickHandler.onClickProposalRow(id);
            }
        });
    }

    interface ProposalListUiBinder extends UiBinder<Widget, ProposalListWidget> {
    }
}
