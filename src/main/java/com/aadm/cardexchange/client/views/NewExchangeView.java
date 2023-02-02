package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCardDecorator;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface NewExchangeView extends IsWidget {

    void setData(String receiverUserEmail, String selectedCardId, String token);
    void setSenderDeck(List<PhysicalCardDecorator> physicalCardDecorators);
    void setReceiverDeck(List<PhysicalCardDecorator> physicalCardDecorators);
    void setPresenter(Presenter presenter);

    interface Presenter {

    }
}
