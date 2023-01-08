package com.aadm.cardexchange.client.routes;

import com.aadm.cardexchange.client.ClientFactory;
import com.aadm.cardexchange.client.places.CardsPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.presenters.CardsActivity;
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
            return new HomeActivity((HomePlace) place, clientFactory);
        if (place instanceof CardsPlace)
            return new CardsActivity(clientFactory.getCardsView(), GWT.create(CardService.class));
        return null;
    }
}
