package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setData(boolean clickable, String title, String subtitle);

    void setSenderDeck(List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId);
    void setReceiverDeck(List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId,  String receiverUserEmail);

    void setPresenter(NewExchangePresenter newExchangePresenter);

    void setPresenter(ExchangePresenter exchangePresenter);

    void setAcceptButtonEnabled(boolean enabled);

    void showAlert(String message);

    void setNewExchangeButtons();

    void setExchangeButtons();

    void resetHandlers();

    interface NewExchangePresenter {
        void createProposal(List<PhysicalCard> senderDeckSelectedCards, List<PhysicalCard> receiverDeckSelectedCards);
        void goTo(Place place);
    }

    interface ExchangePresenter {

        void acceptExchangeProposal();

        void refuseOrWithdrawProposal();

        void goTo(Place place);
    }
}
