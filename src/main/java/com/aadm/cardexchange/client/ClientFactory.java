package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.views.AuthenticationView;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.client.views.HomeView;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {
    EventBus getEventBus();

    PlaceController getPlaceController();

    HomeView getHomeView();

    CardView getCardView();

    AuthenticationView getAuthenticationView();
}
