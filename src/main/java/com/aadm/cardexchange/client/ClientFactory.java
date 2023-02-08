package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.views.*;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {
    EventBus getEventBus();

    PlaceController getPlaceController();

    HomeView getHomeView();

    CardView getCardView();

    AuthView getAuthView();

    DecksView getDecksView();

    AuthSubject getAuthSubject();

    NewExchangeView getNewExchangeView();

    ExchangesView getExchangesView();
}
