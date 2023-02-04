package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.HomeView;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.aadm.cardexchange.shared.models.*;
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

    public List<Card> filterGameCards(String specialAttributeValue, String typeValue, String textInputName, String textInputValue,
                                      List<String> booleanInputNames, List<Boolean> booleanInputValues) {
        List<Card> filteredCards = new ArrayList<>();
        for (Card card : cards) {
            boolean shouldSkip = false;
            if (!textInputValue.isEmpty()) {
                String cardText = "";
                switch (textInputName) {
                    case "Name":
                        cardText = card.getName();
                        break;
                    case "Description":
                        cardText = card.getDescription();
                        break;
                    case "Artist":
                        cardText = card instanceof MagicCard ?
                                ((MagicCard) card).getArtist() :
                                card instanceof PokemonCard ?
                                        ((PokemonCard) card).getArtist() : "";
                        break;
                }
                if (!cardText.toLowerCase().contains(textInputValue.toLowerCase())) {
                    shouldSkip = true;
                }
            }
            if (!specialAttributeValue.equals("all")) {
                if (card instanceof MagicCard ?
                        !specialAttributeValue.equals(((MagicCard) card).getRarity()) :
                        card instanceof PokemonCard ?
                                !specialAttributeValue.equals(((PokemonCard) card).getRarity()) :
                                card instanceof YuGiOhCard &&
                                        !specialAttributeValue.equals(((YuGiOhCard) card).getRace())
                ) {
                    shouldSkip = true;
                }
            }
            if (!typeValue.equals("all")) {
                if (!typeValue.equals(card.getType())) {
                    shouldSkip = true;
                }
            }
            if (!(booleanInputNames.isEmpty() && booleanInputValues.isEmpty())) {
                for (int i = 0; i < booleanInputNames.size(); i++) {
                    String name = booleanInputNames.get(i);
                    Boolean value = booleanInputValues.get(i);
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
