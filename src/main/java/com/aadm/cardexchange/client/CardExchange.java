package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.places.CardsPlace;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.routes.AppActivityMapper;
import com.aadm.cardexchange.client.routes.AppPlaceHistoryMapper;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;

public class CardExchange implements EntryPoint {
  private final Place defaultPlace = new HomePlace();
  private final SimplePanel appWidget = new SimplePanel();

  public void onModuleLoad() {
    // UserServiceAsync loginService = GWT.create(UserService.class);
    // loginService.me(new BaseAsyncCallback<Boolean>() {
    // @Override
    // public void onSuccess(Boolean loggedIn) {
    // Window.alert("Logged in");
    // }
    // });

    // Create ClientFactory using deferred binding so we can replace with different
    // impls in gwt.xml
    ClientFactory clientFactory = new ClientFactoryImpl();
    // ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();
    PlaceController placeController = clientFactory.getPlaceController();

    // Start ActivityManager for the main widget with our ActivityMapper
    ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
    ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
    activityManager.setDisplay(appWidget);

    /*
     * link between Places and Activities is EventBus
     */

    // Start PlaceHistoryHandler with our PlaceHistoryMapper
    AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
    PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
    historyHandler.register(placeController, eventBus, defaultPlace);

    RootPanel root = RootPanel.get("wrapper");
    appWidget.setStyleName("main");

    HTMLPanel appSidebar = new HTMLPanel("");
    appSidebar.addStyleName("sidebar");
    Hyperlink homeLink = new Hyperlink("Home", historyMapper.getToken(defaultPlace));
    Hyperlink cardsLink = new Hyperlink("Cards", historyMapper.getToken(new CardsPlace()));
    appSidebar.add(homeLink);
    appSidebar.add(cardsLink);

    root.add(appSidebar);
    root.add(appWidget);

    // Goes to place represented on URL or default place
    historyHandler.handleCurrentHistory();
  }
}
