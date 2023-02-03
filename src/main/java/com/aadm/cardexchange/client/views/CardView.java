package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.Card;
import com.google.gwt.user.client.ui.IsWidget;

public interface CardView extends IsWidget {
    void setData(Card data);

    void hideModal();

    void displayErrorAlert(String errorMessage);

    void displaySuccessAlert();

    void createUserWidgets(boolean isLoggedIn);

    void setPresenter(Presenter presenter);

    interface Presenter {
        void fetchCard();

        void addCardToDeck(String deckName, String status, String description);
    }
}
