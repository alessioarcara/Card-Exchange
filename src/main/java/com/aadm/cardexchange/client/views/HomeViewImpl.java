package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.places.CardPlace;
import com.aadm.cardexchange.client.widgets.CardWidget;
import com.aadm.cardexchange.client.widgets.GameFiltersWidget;
import com.aadm.cardexchange.client.widgets.ImperativeHandleCard;
import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PropertyType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.*;
import java.util.logging.Logger;


public class HomeViewImpl extends Composite implements HomeView, ImperativeHandleCard {
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
    @UiField
    PushButton applyFiltersButton;
    @UiField
    PushButton cleanFiltersButton;
    private Presenter presenter;
    private boolean isGameChanged;

    public HomeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        magicRadio.addValueChangeHandler(e -> onGameChanged(Game.MAGIC));
        pokemonRadio.addValueChangeHandler(e -> onGameChanged(Game.POKEMON));
        yugiohRadio.addValueChangeHandler(e -> onGameChanged(Game.YUGIOH));
        applyFiltersButton.addClickHandler(e -> handleFiltersApply());
        cleanFiltersButton.addClickHandler(e -> handleFiltersClean());
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setData(List<Card> data) {
        cardsPanel.clear();
        // lookup tables
        // init tables
        Set<String> textPropertyNames = new HashSet<>();
        Map<String, Set<String>> catProperties = new HashMap<>();
        Set<String> variants = new HashSet<>();
        Logger.getLogger("prweesa").info("pasdas");

        Logger.getLogger("prweesa").info("pasdas");
        // set tables
        data.forEach(card -> {
            card.getProperties().forEach(prop -> {
                PropertyType propType = prop.getType();
                String propName = prop.getName();
                if (propType == PropertyType.TEXT)
                    textPropertyNames.add(propName);
                else if (propType == PropertyType.CATEGORICAL) {
                    catProperties.computeIfAbsent(propName, k -> new HashSet<>());
                    catProperties.get(propName).add(prop.getValue());
                }
            });
            variants.addAll(card.getVariants());
            cardsPanel.add(new CardWidget(this, card));
        });
        if (!isGameChanged) {
            setFilters(textPropertyNames.toArray(new String[0]), new ArrayList<>(catProperties.values()), variants.toArray(new String[0]));
            isGameChanged = true;
        }
    }

    private void setFilters(String[] textPropertyNames, List<Set<String>> catProperties, String[] variants) {
        filters.setCategories(catProperties);
        filters.setTextOptions(textPropertyNames);
        filters.setCheckBoxes(variants);
    }

    private void handleFiltersClean() {
        filters.categoryListBoxes.forEach(listBox -> listBox.setItemSelected(0, true));
        filters.textOptions.setItemSelected(0, true);
        filters.textInput.setText("");
        filters.checkBoxes.forEach(checkBox -> checkBox.setValue(false));
        setData(presenter.filterGameCards(
                Collections.emptyList(),
                Collections.emptyList(),
                filters.textOptions.getSelectedValue(),
                filters.textInput.getText(),
                Collections.emptyList(),
                Collections.emptyList()
        ));
    }

    private void handleFiltersApply() {
        List<String> categoryInputNames = new ArrayList<>();
        List<String> categoryInputValues = new ArrayList<>();
        List<String> variantInputNames = new ArrayList<>();
        List<Boolean> variantInputValues = new ArrayList<>();
        for (CheckBox checkBox : filters.checkBoxes) {
            variantInputNames.add(checkBox.getText());
            variantInputValues.add(checkBox.getValue());
        }
        for (ListBox listBox : filters.categoryListBoxes) {
            categoryInputNames.add(listBox.getName());
            categoryInputValues.add(listBox.getSelectedItemText());
        }
        setData(presenter.filterGameCards(
                categoryInputNames,
                categoryInputValues,
                filters.textOptions.getSelectedValue(),
                filters.textInput.getText(),
                variantInputNames,
                variantInputValues
        ));
    }

    @Override
    public void onOpenDetailsClick(Game game, int id) {
        presenter.goTo(new CardPlace(game, id));
    }

    private void onGameChanged(Game game) {
        presenter.fetchGameCards(game);
        isGameChanged = false;
    }

    interface HomeViewImplUIBinder extends UiBinder<Widget, HomeViewImpl> {
    }
}
