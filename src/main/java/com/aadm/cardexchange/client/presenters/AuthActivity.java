package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.views.AuthMode;
import com.aadm.cardexchange.client.views.AuthView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AuthActivity extends AbstractActivity implements AuthView.Presenter {
    private final AuthView view;
    private final PlaceController placeController;
    private final RegExp regExp = RegExp.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public AuthActivity(AuthView view, PlaceController placeController) {
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

    public void authenticate(AuthMode authMode, String email, String password) {
        // if to remove when calling signin and signup in try-catch
        if (!regExp.test(email) || password.length() < 8) {
            view.displayIncorrectCredentialsAlert();
            throw new IllegalArgumentException("Incorrect credentials.");
        }
        try {
            if (authMode == AuthMode.Login) {
                // call signin and catch errors
            } else if (authMode == AuthMode.Signup) {
                // call signup and catch errors
            }
            goTo(new HomePlace());
        } catch (Exception e) {
            view.resetFields();
        }
    }

    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}