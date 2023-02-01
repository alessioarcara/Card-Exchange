package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface CardView extends IsWidget {
    void setData(CardDecorator data);
    void hideModal();
    void displayErrorAlert(String errorMessage);
    void displaySuccessAlert();
    void createUserWidgets(boolean isLoggedIn);
    void setPresenter(Presenter presenter);

    interface Presenter {
        void goTo(Place place);
        void fetchCard();
        void addCardToDeck(String deckName, String status, String description);
    }
}
