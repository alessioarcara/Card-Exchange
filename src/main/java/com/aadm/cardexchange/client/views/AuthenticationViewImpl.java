package com.aadm.cardexchange.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AuthenticationViewImpl extends Composite implements AuthenticationView{
    private static final AuthenticationViewImplUIBinder uiBinder = GWT.create(AuthenticationViewImplUIBinder.class);

    Presenter presenter;

    @UiField
    TextBox emailField;
    @UiField
    PushButton loginButton;

    // 1) sistemare l'interfaccia di AuthenticatioViewImpl
    // 2) due button: uno per registrarmi/autenticarmi e l'altro per passare tra autenticazione e registrazione
    //    [Signin/Signup] [Change to signin/Change to signup]
    // 3) nel presenter definire due funzioni per signin e signup
    // 4) prendere i dati di email e password e mandarli al presenter (ovvero richiamare le due funzioni del presenter dalla view)
    // 5) ad utente loggato, ritornare alla Home e cambiare l'Hyperlink nella sidebar Auth in Logout
    public AuthenticationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        loginButton.addClickHandler((ClickEvent) -> Window.alert("Prova"));
        emailField.setValue("Prova");
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface AuthenticationViewImplUIBinder extends UiBinder<Widget, AuthenticationViewImpl> {
    }
}
