package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface HomeView extends IsWidget {
    void setPresenter(Presenter presenter);

    void setData(List<CardDecorator> data);

    interface Presenter {
        void goTo(Place place);

        void fetchGameCards(Game game);
    }
}
