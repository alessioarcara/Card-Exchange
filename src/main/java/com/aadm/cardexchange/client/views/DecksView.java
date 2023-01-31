package com.aadm.cardexchange.client.views;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface DecksView extends IsWidget {

    void setData(List<String> data);

    void resetData();

    void setPresenter(Presenter presenter);

    interface Presenter {
        void fetchUserDeck();
    }
}
