package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.PhysicalCardWithEmail;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface CardView extends IsWidget {
    void setData(Card data);

    void hideModal();

    String getDeckSelected();

    void displayAlert(String message);

    void createUserWidgets(boolean isLoggedIn);

    void setWishedByUserList(List<? extends PhysicalCardWithEmail> pCards);

    void setOwnedByUserList(List<PhysicalCardWithEmail> pCards);

    void setPresenter(Presenter presenter);

    interface Presenter {
        void goTo(Place place);

        void fetchCard();

        void addCardToDeck(String deckName, String status, String description);
    }
}
