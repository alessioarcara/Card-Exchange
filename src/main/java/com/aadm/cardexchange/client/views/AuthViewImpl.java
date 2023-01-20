package com.aadm.cardexchange.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class AuthViewImpl extends Composite implements AuthView {
    private static final AuthenticationViewImplUIBinder uiBinder = GWT.create(AuthenticationViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    TextBox emailField;
    @UiField
    PasswordTextBox passwordField;
    @UiField
    PushButton authButton;
    @UiField
    PushButton switchModeButton;

    public AuthViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        switchModeButton.addClickHandler((ClickEvent) -> {
            if (authButton.getText().equals(AuthMode.Login.name())) {
                authButton.setText(AuthMode.Signup.name());
                switchModeButton.getUpFace().setText("Switch to " + AuthMode.Login.name());
            } else {
                authButton.setText(AuthMode.Login.name());
                switchModeButton.getUpFace().setText("Switch to " + AuthMode.Signup.name());
            }
        });

        authButton.addClickHandler((ClickEvent) -> {
            if (authButton.getText().equals(AuthMode.Login.name())) {
                presenter.authenticate(AuthMode.Login, emailField.getText(), passwordField.getText());
            } else {
                presenter.authenticate(AuthMode.Signup, emailField.getText(), passwordField.getText());
            }
        });
    }

    @Override
    public void displayIncorrectCredentialsAlert() {
        Window.alert("Incorrect credentials.");
    }

    public void resetFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface AuthenticationViewImplUIBinder extends UiBinder<Widget, AuthViewImpl> {
    }
}
