package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setReceiverDeckName(String receiverUserEmail);
    void setSenderDeck(List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId);
    void setReceiverDeck(List<PhysicalCardWithName> physicalCardDecorators, String selectedCardId);

    void setPresenter(NewExchangePresenter newExchangePresenter);

    void setPresenter(ProposalPresenter proposalPresenter);

    void setAcceptButtonEnabled(boolean enabled);

    void showAlert(String message);

    void resetAll();

    interface NewExchangePresenter {
        void createProposal(List<PhysicalCard> senderDeckSelectedCards, List<PhysicalCard> receiverDeckSelectedCards);
        void goTo(Place place);
    }

    interface ProposalPresenter {

        void acceptProposal();

        void goTo(Place place);
    }
}
