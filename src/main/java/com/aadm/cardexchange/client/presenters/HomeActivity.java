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
    private List<CardDecorator> cards;

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
        rpcService.getGameCards(game, new BaseAsyncCallback<List<CardDecorator>>() {
            @Override
            public void onSuccess(List<CardDecorator> result) {
                view.setData(result);
                cards = result;
            }
        });
    }

    public List<CardDecorator> filterGameCards(String specialAttributeValue, String typeValue, String textInputName, String textInputValue,
                                               List<String> booleanInputNames, List<Boolean> booleanInputValues) {
        List<CardDecorator> filteredCards = new ArrayList<>();
        for (CardDecorator card : cards) {
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
                        cardText = card instanceof MagicCardDecorator ?
                                ((MagicCardDecorator) card).getArtist() :
                                card instanceof PokemonCardDecorator ?
                                        ((PokemonCardDecorator) card).getArtist() : "";
                        break;
                }
                if (!cardText.toLowerCase().contains(textInputValue.toLowerCase())) {
                    continue;
                }
            } else if (!specialAttributeValue.equals("all")) {
                if (card instanceof MagicCardDecorator ?
                        !specialAttributeValue.equals(((MagicCardDecorator) card).getRarity()) :
                        card instanceof PokemonCardDecorator ?
                                !specialAttributeValue.equals(((PokemonCardDecorator) card).getRarity()) :
                                card instanceof YuGiOhCardDecorator &&
                                        !specialAttributeValue.equals(((YuGiOhCardDecorator) card).getRace())
                ) {
                    continue;
                }
            } else if (!typeValue.equals("all")) {
                if (!typeValue.equals(card.getType())) {
                    continue;
                }
            } else if (!(booleanInputNames.isEmpty() && booleanInputValues.isEmpty())) {

            }
            filteredCards.add(card);
        }
        return filteredCards;
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
