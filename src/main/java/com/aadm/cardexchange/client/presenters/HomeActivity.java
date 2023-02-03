package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.HomeView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AbstractActivity implements HomeView.Presenter {
    private final HomeView view;
    private final CardServiceAsync rpcService;
    private final PlaceController placeController;
    private List<Card> cards;

    public HomeActivity(HomeView view, CardServiceAsync rpcService, PlaceController placeController) {
        this.view = view;
        this.rpcService = rpcService;
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());
    }

    public void fetchGameCards(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }
        rpcService.getGameCards(game, new BaseAsyncCallback<List<Card>>() {
            @Override
            public void onSuccess(List<Card> result) {
                view.setData(result);
                cards = result;
            }
        });
    }

    public List<Card> filterGameCards(List<String> categoryInputNames, List<String> categoryInputValues,
                                      String textInputName, String textInputValue,
                                      List<String> variantInputNames, List<Boolean> variantInputValues) {
        List<Card> filteredCards = new ArrayList<>();
        for (Card card : cards) {
            boolean shouldSkip = false;
            if (!textInputValue.isEmpty()) {
                String cardText = card.getProperty(textInputName);
                if (!cardText.toLowerCase().contains(textInputValue.toLowerCase())) {
                    shouldSkip = true;
                }
            }
            if (!(categoryInputNames.isEmpty() && categoryInputValues.isEmpty())) {
                for (int i = 0; i < categoryInputNames.size(); i++) {
                    String name = categoryInputNames.get(i);
                    String value = categoryInputValues.get(i);
                    if (!card.getProperty(name).equals(value)) {
                        shouldSkip = true;
                    }
                }
            }
            if (!(variantInputNames.isEmpty() && variantInputValues.isEmpty())) {
                for (int i = 0; i < variantInputNames.size(); i++) {
                    String name = variantInputNames.get(i);
                    Boolean value = variantInputValues.get(i);
                    if (value && !card.getVariants().contains(name)) {
                        shouldSkip = true;
                        break;
                    }
                }
            }
            if (!shouldSkip) {
                filteredCards.add(card);
            }
        }
        return filteredCards;
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
