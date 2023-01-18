package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.views.AuthenticationView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AuthenticationActivity extends AbstractActivity implements AuthenticationView.Presenter {
    private final AuthenticationView view;
    private final PlaceController placeController;

    public AuthenticationActivity(AuthenticationView view, PlaceController placeController) {
        this.view = view;
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
    }

    @Override
    public void onStop() {
        view.resetFields();
    }

    public void login(String email, String password) {
        if (!email.equals("") && !password.equals("")) {
            goTo(new HomePlace());
        } else {
            Window.alert("Login failed. Please check the inserted fields");
            view.resetFields();
        }
    }

    public void signup(String email, String password) {
        if (email.contains("@") && password.length()>=8) {
            goTo(new HomePlace());
        } else {
            Window.alert("Signup failed. Please enter a valid email and a password with a minimum length of 8 characters");
            view.resetFields();
        }
    }

    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}