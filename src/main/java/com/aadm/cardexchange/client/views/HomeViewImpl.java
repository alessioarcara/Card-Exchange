package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.widgets.CardWidget;
import com.aadm.cardexchange.client.widgets.FunctionInterface;
import com.aadm.cardexchange.client.widgets.GameFiltersWidget;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HomeViewImpl extends Composite implements HomeView, FunctionInterface {
    private static final HomeViewImplUIBinder uiBinder = GWT.create(HomeViewImplUIBinder.class);
    @UiField
    RadioButton magicRadio;
    @UiField
    RadioButton pokemonRadio;
    @UiField
    RadioButton yugiohRadio;
    @UiField
    HTMLPanel cardsPanel;
    @UiField
    GameFiltersWidget filters;
    private Presenter presenter;

    public HomeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        magicRadio.addValueChangeHandler(e -> onValueChanged(e, Game.Magic));
        pokemonRadio.addValueChangeHandler(e -> onValueChanged(e, Game.Pokemon));
        yugiohRadio.addValueChangeHandler(e -> onValueChanged(e, Game.YuGiOh));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setData(List<CardDecorator> data) {
        cardsPanel.clear();
        filters.specialAttributeOptions.clear();
        filters.typeOptions.clear();
        cardsPanel.getElement().setInnerHTML("");
        Set<String> uniqueSpecialAttributes = new HashSet<>();
        Set<String> uniqueTypes = new HashSet<>();
        data.forEach(card -> {
            uniqueTypes.add(card.getType());
            if (card instanceof MagicCardDecorator) {
                uniqueSpecialAttributes.add(((MagicCardDecorator) card).getRarity());
            } else if (card instanceof PokemonCardDecorator) {
                uniqueSpecialAttributes.add(((PokemonCardDecorator) card).getRarity());
            } else {
                uniqueSpecialAttributes.add(((YuGiOhCardDecorator) card).getRace());

            }
            cardsPanel.add(new CardWidget(this, card));
        });
        uniqueTypes.forEach(type -> filters.typeOptions.addItem(type));
        uniqueSpecialAttributes.forEach(specialAttribute -> filters.specialAttributeOptions.addItem(specialAttribute));
    }

    @Override
    public void handleClickCard() {
        presenter.goTo(new CardPlace());
    }

    interface HomeViewImplUIBinder extends UiBinder<Widget, HomeViewImpl> {
    }

    private void onValueChanged(ValueChangeEvent<Boolean> e, Game game) {
        presenter.fetchGameCards(game);
        filters.handleGameChange(game);
    }
}
