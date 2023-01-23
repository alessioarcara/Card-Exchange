package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.AuthSubject.AuthSubject;
import com.aadm.cardexchange.client.views.AuthView;
import com.aadm.cardexchange.client.views.CardView;
import com.aadm.cardexchange.client.views.HomeView;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {
    EventBus getEventBus();

    PlaceController getPlaceController();

    HomeView getHomeView();

    CardView getCardView();

    AuthView getAuthView();

    AuthSubject getAuthSubject();
}
