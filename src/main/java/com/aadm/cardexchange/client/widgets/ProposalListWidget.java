package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.handlers.ImperativeHandleProposalList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class ProposalListWidget extends BaseListWidget {
    private static final ProposalListUiBinder uiBinder = GWT.create(ProposalListUiBinder.class);
    private static final String NO_PROPOSALS_TEXT = "No proposals";
    String otherUser;
    int size;

    public ProposalListWidget(String title, String otherUser) {
        this.otherUser = otherUser;
        this.size = 0;
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
        setNoItemsText(NO_PROPOSALS_TEXT);
    }

    @Override
    protected void setupTableHeader(TableRowElement row) {
        row.insertCell(0).setInnerText("ID");
        row.insertCell(1).setInnerText("Date");
        row.insertCell(2).setInnerText(otherUser);
    }

    public void addRow(int id, String date, String email, ImperativeHandleProposalList rowClickHandler) {
        if (size++ == 0) table.removeRow(noItemsRow);
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

    public void resetTable() {
        table.removeAllRows();
        size = 0;
        setNoItemsText(NO_PROPOSALS_TEXT);
    }

    interface ProposalListUiBinder extends UiBinder<Widget, ProposalListWidget> {
    }
}
