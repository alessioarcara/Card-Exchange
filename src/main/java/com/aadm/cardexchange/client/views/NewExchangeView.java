package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setData(String title, String cancelTextButton, String acceptTextButton);

    void setSenderDeck(boolean isClickable, List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId, String senderEmail);

    void setReceiverDeck(boolean isClickable, List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId, String receiverEmail);

    void setPresenter(NewExchangePresenter newExchangePresenter);

    void setPresenter(ExchangePresenter exchangePresenter);

    void setAcceptButtonEnabled(boolean enabled);

    void showAlert(String message);

    void setNewExchangeButtons();

    void setExchangeButtons();

    void resetHandlers();

    interface NewExchangePresenter {
        void createProposal(List<PhysicalCard> senderDeckSelectedCards, List<PhysicalCard> receiverDeckSelectedCards);
    }

    interface ExchangePresenter {

        void acceptExchangeProposal();

        void refuseOrWithdrawProposal();

        void goTo(Place place);
    }
}
