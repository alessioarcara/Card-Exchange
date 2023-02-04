package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setData(String receiverUserEmail, String selectedCardId, String token);
    void setSenderDeck(List<PhysicalCardWithName> physicalCards);
    void setReceiverDeck(List<PhysicalCardWithName> physicalCards);
    void setPresenter(Presenter presenter);

    interface Presenter {

    }
}
