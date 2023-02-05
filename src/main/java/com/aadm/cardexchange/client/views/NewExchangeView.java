package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setReceiverDeckName(String receiverUserEmail);
    void setSenderDeck(List<PhysicalCardWithName> physicalCardDecorators);
    void setReceiverDeck(List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId);
    void setPresenter(Presenter presenter);
    void showAlert(String message);

    interface Presenter {
        void createProposal(List<PhysicalCard> senderDeckSelectedCards, List<PhysicalCard> receiverDeckSelectedCards);
    }
}
