package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.google.gwt.user.client.ui.IsWidget;

public interface CardView extends IsWidget {
    void setData(CardDecorator data);
    void setPresenter(Presenter presenter);

    interface Presenter {
        void fetchCard();
    }
}
