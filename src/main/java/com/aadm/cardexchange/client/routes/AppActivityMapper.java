package com.aadm.cardexchange.client.routes;

import com.aadm.cardexchange.client.ClientFactory;
import com.aadm.cardexchange.client.places.*;
import com.aadm.cardexchange.client.presenters.*;
import com.aadm.cardexchange.shared.AuthService;
import com.aadm.cardexchange.shared.CardService;
import com.aadm.cardexchange.shared.DeckService;
import com.aadm.cardexchange.shared.ExchangeService;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {
    private final ClientFactory clientFactory;

    public AppActivityMapper(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof HomePlace)
            return new HomeActivity(clientFactory.getHomeView(), GWT.create(CardService.class), clientFactory.getPlaceController());
        if (place instanceof CardPlace)
            return new CardActivity((CardPlace) place, clientFactory.getCardView(), GWT.create(CardService.class), GWT.create(DeckService.class),
                    clientFactory.getAuthSubject(), clientFactory.getPlaceController());
        if (place instanceof AuthPlace)
            return new AuthActivity(clientFactory.getAuthView(), GWT.create(AuthService.class), clientFactory.getAuthSubject(), clientFactory.getPlaceController());
        if (place instanceof DecksPlace)
            return new DecksActivity(clientFactory.getDecksView(), GWT.create(DeckService.class), clientFactory.getAuthSubject());
        if (place instanceof NewExchangePlace)
            return new NewExchangeActivity((NewExchangePlace) place, clientFactory.getNewExchangeView(), GWT.create(DeckService.class), GWT.create(ExchangeService.class),
                    clientFactory.getAuthSubject(), clientFactory.getPlaceController());
        if (place instanceof ExchangesPlace)
            return new ExchangesActivity(clientFactory.getExchangesView());
        if (place instanceof ProposalPlace)
            return new ProposalActivity((ProposalPlace) place, clientFactory.getNewExchangeView(), GWT.create(ExchangeService.class),
                    clientFactory.getAuthSubject(), clientFactory.getPlaceController());
        return null;
    }
}
