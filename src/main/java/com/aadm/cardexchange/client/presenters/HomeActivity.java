package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.ClientFactory;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.views.HomeView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class HomeActivity extends AbstractActivity implements HomeView.Presenter {
    // Used to obtain views, eventBus, placeController
    private final ClientFactory clientFactory;

    public HomeActivity(HomePlace place, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        HomeView homeView = clientFactory.getHomeView();
        homeView.setPresenter(this);
        containerWidget.setWidget(homeView.asWidget());
    }

    /*
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        return null;
    }

    /**
     * Navigate to a new Place in the browser
     */
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }
}
