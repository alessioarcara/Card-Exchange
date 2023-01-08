package com.aadm.cardexchange.client.views;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface HomeView extends IsWidget {
    void setPresenter(Presenter presenter);

    interface Presenter {
        void goTo(Place place);
    }
}
