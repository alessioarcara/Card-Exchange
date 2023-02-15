package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.handlers.ImperativeHandleSidebar;
import com.aadm.cardexchange.client.routes.RouteConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class SidebarWidget extends Composite implements RouteConstants {
    private static final SidebarUiBinder uiBinder = GWT.create(SidebarUiBinder.class);
    @UiField
    HTMLPanel links;
    @UiField
    ParagraphElement userEmail;
    Button button;

    public SidebarWidget(ImperativeHandleSidebar parent) {
        initWidget(uiBinder.createAndBindUi(this));
        button = new Button("Logout", (ClickHandler) event -> parent.onClickLogout());
        button.setStyleName("");
    }

    public void setLinks(String email, boolean isLoggedIn) {
        links.clear();
        userEmail.setInnerText(email != null ? "User: " + email : "");
        links.add(new Hyperlink("Home", homeLink));
        if (!isLoggedIn) {
            links.add(new Hyperlink("Auth", authLink));
        } else {
            links.add(new Hyperlink("Decks", decksLink));
            links.add(new Hyperlink("Exchanges", exchangesLink));
            links.add(button);
        }
    }

    interface SidebarUiBinder extends UiBinder<Widget, SidebarWidget> {
    }
}