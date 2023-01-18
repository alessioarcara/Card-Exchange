package com.aadm.cardexchange.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class AuthenticationViewImpl extends Composite implements AuthenticationView{
    private static final AuthenticationViewImplUIBinder uiBinder = GWT.create(AuthenticationViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    TextBox emailField;
    @UiField
    PasswordTextBox passwordField;
    @UiField
    PushButton loginOrSignupButton;
    @UiField
    PushButton switchModeButton;

    public AuthenticationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        switchModeButton.addClickHandler((ClickEvent) -> {
            if (loginOrSignupButton.getText().equals("Login")) {
                loginOrSignupButton.setText("Signup");
                switchModeButton.getUpFace().setText("Switch to Login");
            } else {
                loginOrSignupButton.setText("Login");
                switchModeButton.getUpFace().setText("Switch to Signup");
            }
        });

        loginOrSignupButton.addClickHandler((ClickEvent) -> {
            if (loginOrSignupButton.getText().equals("Login")) {
                presenter.login(emailField.getText(), passwordField.getText());
            } else {
                presenter.signup(emailField.getText(), passwordField.getText());
            }
        });
    }

    public void resetFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    @Override
    public void setPresenter(Presenter presenter) {this.presenter = presenter;}

    interface AuthenticationViewImplUIBinder extends UiBinder<Widget, AuthenticationViewImpl> {
    }
}
