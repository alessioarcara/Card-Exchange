package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.views.*;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public class ClientFactoryImpl implements ClientFactory {
    private static final EventBus eventBus = new SimpleEventBus();
    private static final PlaceController placeController = new PlaceController(eventBus);
    private static final AuthSubject authSubject = new AuthSubject();
    private static final HomeView homeView = new HomeViewImpl();
    private static final CardView cardView = new CardViewImpl();
    private static final AuthView authView = new AuthViewImpl();
    private static final DecksView decksView = new DecksViewImpl();
    private static final NewExchangeView newExchangeView = new NewExchangeViewImpl();

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
        return homeView;
    }

    @Override
    public CardView getCardView() {
        return cardView;
    }

    @Override
    public AuthView getAuthView() {
        return authView;
    }

    @Override
    public DecksView getDecksView() {
        return decksView;
    }

    @Override
    public AuthSubject getAuthSubject() {
        return authSubject;
    }

    public NewExchangeView getNewExchangeView() {
        return newExchangeView;
    }
}
