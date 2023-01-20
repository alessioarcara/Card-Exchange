package com.aadm.cardexchange.client.routes;

import com.aadm.cardexchange.client.ClientFactory;
import com.aadm.cardexchange.client.places.AuthPlace;
import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.presenters.AuthActivity;
import com.aadm.cardexchange.client.presenters.CardActivity;
import com.aadm.cardexchange.client.presenters.HomeActivity;
import com.aadm.cardexchange.shared.CardService;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {

    private final ClientFactory clientFactory;

    public AppActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof HomePlace)
            return new HomeActivity(clientFactory.getHomeView(), GWT.create(CardService.class), clientFactory.getPlaceController());
        if (place instanceof CardPlace)
            return new CardActivity((CardPlace) place, clientFactory.getCardView(), GWT.create(CardService.class));
        if (place instanceof AuthPlace)
            return new AuthActivity(clientFactory.getAuthenticationView(), clientFactory.getPlaceController());
        return null;
    }
}
