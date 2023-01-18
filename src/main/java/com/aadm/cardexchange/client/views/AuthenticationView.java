package com.aadm.cardexchange.client.views;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AuthenticationView extends IsWidget {

    void setPresenter(Presenter presenter);

    void resetFields();
    
    interface Presenter {
        void login(String email, String password);

        void signup(String email, String password);

        void goTo(Place place);
    }
}