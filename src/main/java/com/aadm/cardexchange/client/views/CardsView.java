package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.CardImpl;
import com.google.gwt.user.client.ui.IsWidget;

public interface CardsView extends IsWidget {
    void setPresenter(Presenter presenter);

    void setData(CardImpl[] data);

    void handleAddData(String cardName, String cardDesc);

    interface Presenter {
        boolean addCard(String cardName, String cardDesc);

        boolean removeCard(String cardId);
    }
}
