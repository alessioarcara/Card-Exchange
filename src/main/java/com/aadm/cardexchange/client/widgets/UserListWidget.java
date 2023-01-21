package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserListWidget extends Composite {
    private static final UserListUIBinder uiBinder = GWT.create(UserListUIBinder.class);
    @UiField
    HeadingElement tableHeading;
    @UiField
    TableSectionElement tableBody;

    @UiConstructor public UserListWidget(String title, String[] users) {
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerHTML(title);
        for (String user: users) {
            Element tr = DOM.createTR();
            Element td1 = DOM.createTD();
            td1.setAttribute("colspan", "2");
            Element td2 = DOM.createTD();
            td1.setInnerHTML(user);
            td2.setInnerHTML("1 (Ottimo)");
            DOM.appendChild(tr, td1);
            DOM.appendChild(tr, td2);
            DOM.appendChild(tableBody, tr);
        }
    }

    interface UserListUIBinder extends UiBinder<Widget, UserListWidget> {
    }
}
