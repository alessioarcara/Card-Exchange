package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.handlers.ImperativeHandleSidebar;
import com.aadm.cardexchange.client.routes.RouteConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class SidebarWidget extends Composite implements RouteConstants {
    private static final SidebarUiBinder uiBinder = GWT.create(SidebarUiBinder.class);
    @UiField
    HTMLPanel links;
    Button button;

    public SidebarWidget(ImperativeHandleSidebar parent) {
        initWidget(uiBinder.createAndBindUi(this));
        button = new Button("Logout", (ClickHandler) event -> parent.onClickLogout());
    }

    public void setLinks(boolean isLoggedIn) {
        links.clear();
        links.add(new Hyperlink("Home", homeLink));
        if (!isLoggedIn) {
            links.add(new Hyperlink("Auth", authLink));
        } else {
            links.add(new Hyperlink("Decks", decksLink));
            links.add(button);
        }
    }

    interface SidebarUiBinder extends UiBinder<Widget, SidebarWidget> {
    }
}