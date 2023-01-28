package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.Deck;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface DecksView extends IsWidget {

    void setData(List<Deck> data);

    void resetData();

    void setPresenter(Presenter presenter);

    interface Presenter {
    }
}
