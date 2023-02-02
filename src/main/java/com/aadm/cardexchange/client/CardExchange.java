package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.auth.Observer;
import com.aadm.cardexchange.client.places.HomePlace;
import com.aadm.cardexchange.client.routes.AppActivityMapper;
import com.aadm.cardexchange.client.routes.AppPlaceHistoryMapper;
import com.aadm.cardexchange.client.utils.IgnoreAsyncCallback;
import com.aadm.cardexchange.client.widgets.ImperativeHandleSidebar;
import com.aadm.cardexchange.client.widgets.SidebarWidget;
import com.aadm.cardexchange.shared.AuthService;
import com.aadm.cardexchange.shared.AuthServiceAsync;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;


public class CardExchange implements EntryPoint, Observer, ImperativeHandleSidebar {
    private final SimplePanel appWidget = new SimplePanel();
    private final SidebarWidget appSidebar = new SidebarWidget(this);
    private AuthServiceAsync authService;
    private AuthSubject authSubject;
    private PlaceController placeController;

    public void onModuleLoad() {
        ClientFactory clientFactory = new ClientFactoryImpl();
        EventBus eventBus = clientFactory.getEventBus();
        placeController = clientFactory.getPlaceController();
        authService = GWT.create(AuthService.class);
        authSubject = clientFactory.getAuthSubject();
        authSubject.attach(this);

        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(appWidget);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        AppPlaceHistoryMapper historyMapper = new AppPlaceHistoryMapper(authSubject);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, new HomePlace());

        RootPanel root = RootPanel.get("layout");
        appWidget.setStyleName("main");
        update();

        root.add(appSidebar);
        root.add(appWidget);

        // Goes to place represented on URL or default place
        historyHandler.handleCurrentHistory();
    }

    @Override
    public void onClickLogout() {
        authService.logout(authSubject.getToken(), new IgnoreAsyncCallback<>());
        Cookies.removeCookie("token");
        authSubject.setToken(null);
        placeController.goTo(new HomePlace());
    }

    @Override
    public void update() {
        appSidebar.setLinks(authSubject.isLoggedIn());
    }
}
