package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.Card;
import com.google.gwt.user.client.ui.IsWidget;

public interface CardsView extends IsWidget {
    void setPresenter(Presenter presenter);

    void setData(Card[] data);

    void handleAddData(String cardName, String cardDesc);

    interface Presenter {
        boolean addCard(String cardName, String cardDesc);

        boolean removeCard(String cardId);
    }
}
