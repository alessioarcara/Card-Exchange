package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface HomeView extends IsWidget {
    void setPresenter(Presenter presenter);

    void setData(List<Card> data);

    interface Presenter {
        void goTo(Place place);

        void fetchGameCards(Game game);

        List<Card> filterGameCards(List<String> categoryInputNames, List<String> categoryInputValues,
                                   String textInputName, String textInputValue,
                                   List<String> variantInputNames, List<Boolean> variantInputValues);
    }
}
