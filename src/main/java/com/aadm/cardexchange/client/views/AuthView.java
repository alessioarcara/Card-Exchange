package com.aadm.cardexchange.client.views;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AuthView extends IsWidget {

    void setPresenter(Presenter presenter);

    void displayAlert(String message);

    void setAuthToken(String token);

    void resetFields();

    interface Presenter {
        void authenticate(AuthMode authMode, String email, String password);

        void goTo(Place place);
    }
}