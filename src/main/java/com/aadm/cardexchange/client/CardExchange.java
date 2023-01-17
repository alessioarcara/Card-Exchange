package com.aadm.cardexchange.client;

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
    ClientFactory clientFactory = new ClientFactoryImpl();
    EventBus eventBus = clientFactory.getEventBus();
    PlaceController placeController = clientFactory.getPlaceController();

    // Start ActivityManager for the main widget with our ActivityMapper
    ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
    ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
    activityManager.setDisplay(appWidget);

    // Start PlaceHistoryHandler with our PlaceHistoryMapper
    AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
    PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
    historyHandler.register(placeController, eventBus, defaultPlace);

    RootPanel root = RootPanel.get("layout");
    appWidget.setStyleName("main");

    HTMLPanel appSidebar = new HTMLPanel("");
    appSidebar.addStyleName("sidebar");
    Hyperlink homeLink = new Hyperlink("Home", historyMapper.getToken(defaultPlace));
    appSidebar.add(homeLink);

    root.add(appSidebar);
    root.add(appWidget);

    // Goes to place represented on URL or default place
    historyHandler.handleCurrentHistory();
  }
}
