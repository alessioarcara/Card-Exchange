package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setReceiverDeckName(String receiverUserEmail);
    void setSenderDeck(List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId);
    void setReceiverDeck(List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId);

    void setPresenter(NewExchangePresenter newExchangePresenter);

    void setPresenter(ProposalPresenter proposalPresenter);

    void showAlert(String message);

    interface NewExchangePresenter {
        void createProposal(List<PhysicalCard> senderDeckSelectedCards, List<PhysicalCard> receiverDeckSelectedCards);
    }

    interface ProposalPresenter {

        void acceptProposal();
    }
}
