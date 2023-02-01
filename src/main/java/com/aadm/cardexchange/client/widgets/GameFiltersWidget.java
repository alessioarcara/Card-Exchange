package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.*;

public class GameFiltersWidget extends Composite {
    private static final GameFiltersUIBinder uiBinder = GWT.create(GameFiltersUIBinder.class);
    @UiField
    public TextBox textInput;
    @UiField
    public ListBox specialAttributeOptions;
    @UiField
    public ListBox typeOptions;
    @UiField
    public ListBox textOptions;
    public List<CheckBox> checkBoxes;
    @UiField
    SpanElement specialAttributeSpan;
    @UiField
    HTMLPanel checkboxesPanel;
    Map<Game, List<String>> gameTextFieldsMap;
    Map<Game, List<String>> gameBooleanFieldsMap;

    @UiConstructor
    public GameFiltersWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        gameTextFieldsMap = new HashMap<>();
        gameTextFieldsMap.put(Game.MAGIC, Arrays.asList("Name", "Artist"));
        gameTextFieldsMap.put(Game.POKEMON, Arrays.asList("Name", "Artist"));
        gameTextFieldsMap.put(Game.YUGIOH, Collections.singletonList("Name"));

        gameBooleanFieldsMap = new HashMap<>();
        gameBooleanFieldsMap.put(Game.MAGIC, Arrays.asList("hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint"));
        gameBooleanFieldsMap.put(Game.POKEMON, Arrays.asList("isFirstEdition", "isHolo", "isNormal", "isReverse", "isPromo"));
        gameBooleanFieldsMap.put(Game.YUGIOH, Collections.emptyList());
    }

    public void handleGameChange(Game game) {
        specialAttributeSpan.setInnerHTML((game == Game.MAGIC || game == Game.POKEMON) ? "Rarity" : "Race");
        textOptions.clear();
        checkboxesPanel.clear();
        checkBoxes = new ArrayList<>();
        for (String textField : gameTextFieldsMap.get(game))
            textOptions.addItem(textField);
        for (String booleanField : gameBooleanFieldsMap.get(game)) {
            CheckBox checkBox = new CheckBox(booleanField);
            checkboxesPanel.add(checkBox);
            checkBoxes.add(checkBox);
        }
    }

    interface GameFiltersUIBinder extends UiBinder<Widget, GameFiltersWidget> {
    }
}
