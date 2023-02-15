package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCardWithEmail;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.function.Function;

public class UserListWidget extends BaseListWidget {
    private static final UserListUIBinder uiBinder = GWT.create(UserListUIBinder.class);
    private static final String NO_USERS_TEXT = "No users";
    boolean showExchangeButton;

    @UiConstructor
    public UserListWidget(String title, boolean showExchangeButton) {
        this.showExchangeButton = showExchangeButton;
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
        setNoItemsText(NO_USERS_TEXT);
    }

    @Override
    protected void setupTableHeader(TableRowElement row) {
        row.insertCell(0).setInnerText("User");
        row.insertCell(1).setInnerText("Status");
        if (showExchangeButton) row.insertCell(2).setInnerText("");
    }

    public void setTable(List<? extends PhysicalCardWithEmail> pCards, Function<PhysicalCardWithEmail, Button> createButton) {
        if (!pCards.isEmpty()) table.removeRow(noItemsRow);
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
