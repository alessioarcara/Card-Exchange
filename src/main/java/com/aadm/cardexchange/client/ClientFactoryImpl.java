package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.views.*;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public class ClientFactoryImpl implements ClientFactory {
    private static final EventBus eventBus = new SimpleEventBus();
    private static final PlaceController placeController = new PlaceController(eventBus);
    private static final HomeView helloView = new HomeViewImpl();
    private static final CardView cardView = new CardViewImpl();
    private static final AuthView authView = new AuthViewImpl();

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public HomeView getHomeView() {
        return helloView;
    }

    @Override
    public CardView getCardView() {
        return cardView;
    }

    @Override
    public AuthView getAuthView() {
        return authView;
    }
}
