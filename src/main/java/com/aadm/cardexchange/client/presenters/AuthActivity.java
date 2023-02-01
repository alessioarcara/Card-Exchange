package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.views.AuthMode;
import com.aadm.cardexchange.client.views.AuthView;
import com.aadm.cardexchange.shared.AuthServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;


public class AuthActivity extends AbstractActivity implements AuthView.Presenter {
    private final AuthView view;
    private final AuthServiceAsync rpcService;
    private final AuthSubject authSubject;
    private final PlaceController placeController;
    private final RegExp regExp = RegExp.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public AuthActivity(AuthView view, AuthServiceAsync rpcService, AuthSubject authSubject, PlaceController placeController) {
        this.view = view;
        this.rpcService = rpcService;
        this.authSubject = authSubject;
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
        if (!regExp.test(email) || password.length() < 8) {
            view.displayAlert("Incorrect credentials");
        } else {
            AsyncCallback<String> callback = new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof AuthException) {
                        view.displayAlert(((AuthException) caught).getErrorMessage());
                    } else {
                        view.displayAlert("Unexpected error occurred");
                    }
                }

                @Override
                public void onSuccess(String result) {
                    view.setAuthToken(result);
                    authSubject.setToken(result);
                    goTo(new HomePlace());
                }
            };
            if (authMode == AuthMode.Login) {
                rpcService.signin(email, password, callback);
            } else if (authMode == AuthMode.Signup) {
                rpcService.signup(email, password, callback);
            }
        }
    }

    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}