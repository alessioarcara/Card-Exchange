package com.aadm.cardexchange.client.widgets;

import com.google.gwt.dom.client.*;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

public abstract class BaseListWidget extends Composite {
    protected int noItemsRow;
    @UiField
    HeadingElement tableHeading;
    @UiField(provided = true)
    FlexTable table;

    protected void setNoItemsText(String text) {
        noItemsRow = table.getRowCount();
        table.setText(noItemsRow, 0, text);
        table.getFlexCellFormatter().setColSpan(noItemsRow, 0, 3);
    }

    protected void setupTable() {
        table = new FlexTable();
        TableSectionElement tHead = ((TableElement) ((Element) table.getElement())).createTHead();
        TableRowElement row = tHead.insertRow(0);
        setupTableHeader(row);
    }

    protected abstract void setupTableHeader(TableRowElement row);
}
