package com.aadm.cardexchange.client.views;

import com.google.gwt.user.client.ui.IsWidget;

public interface NewExchangeView extends IsWidget {

    void setData(String receiverUserEmail, String selectedCardId);
    void setPresenter(Presenter presenter);

    interface Presenter {

    }
}
