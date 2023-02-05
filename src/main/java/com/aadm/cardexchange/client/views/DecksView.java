package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;
import java.util.function.BiConsumer;

public interface DecksView extends IsWidget {

    void setData(List<String> data);

    void resetData();

    void setPresenter(Presenter presenter);

    interface Presenter {
        void fetchUserDeck(String deckName, BiConsumer<List<PhysicalCardWithName>, String> setDeckData);
    }
}
