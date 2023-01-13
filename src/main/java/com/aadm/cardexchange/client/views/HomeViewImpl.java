package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.widgets.CardWidget;
import com.aadm.cardexchange.client.widgets.FunctionInterface;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;


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
        cardsPanel.getElement().setInnerHTML("");
        for (CardDecorator card : data) {
            cardsPanel.add(new CardWidget(this, card.getName(), card.getDescription(), card.getType()));
        }
    }

    @Override
    public void handleClickCard() {
        presenter.goTo(new CardPlace());
    }

    interface HomeViewImplUIBinder extends UiBinder<Widget, HomeViewImpl> {
    }

    private void onValueChanged(ValueChangeEvent<Boolean> e, Game game) {
        presenter.fetchGameCards(game);
    }
}
