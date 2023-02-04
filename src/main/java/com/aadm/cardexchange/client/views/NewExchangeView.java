package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setData(String receiverUserEmail, String selectedCardId, String token);
    void setSenderDeck(List<PhysicalCardWithName> physicalCardDecorators);
    void setReceiverDeck(List<PhysicalCardWithName> physicalCardDecorators);
    void setPresenter(Presenter presenter);

    interface Presenter {
        void createProposal(DeckWidget senderDeck, DeckWidget receiverDeck);
    }
}
