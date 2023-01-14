package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.Game;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.*;

public class GameFiltersWidget extends Composite {
    private static final GameFiltersUIBinder uiBinder = GWT.create(GameFiltersUIBinder.class);
    @UiField
    public ListBox specialAttributeOptions;
    @UiField
    public ListBox typeOptions;
    @UiField
    ListBox textOptions;
    @UiField
    HTMLPanel checkboxes;
    Map<Game, List<String>> gameTextFieldsMap;
    Map<Game, List<String>> gameBooleanFieldsMap;

    public GameFiltersWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        gameTextFieldsMap = new HashMap<>();
        gameTextFieldsMap.put(Game.Magic, Arrays.asList("Nome", "Artista", "Description"));
        gameTextFieldsMap.put(Game.Pokemon, Arrays.asList("Nome", "Artista", "Description"));
        gameTextFieldsMap.put(Game.YuGiOh, Arrays.asList("Nome", "Description", "Tipo"));

        gameBooleanFieldsMap = new HashMap<>();
        gameBooleanFieldsMap.put(Game.Magic, Arrays.asList("hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint"));
        gameBooleanFieldsMap.put(Game.Pokemon, Arrays.asList("isFirstEdition", "isHolo", "isNormal", "isReverse", "isPromo"));
        gameBooleanFieldsMap.put(Game.YuGiOh, Collections.emptyList());
    }

    public void handleGameChange(Game game) {
        textOptions.clear();
        checkboxes.clear();
        for (String textField : gameTextFieldsMap.get(game))
            textOptions.addItem(textField);
        for (String booleanField : gameBooleanFieldsMap.get(game))
            checkboxes.add(new CheckBox(booleanField));
    }

    interface GameFiltersUIBinder extends UiBinder<Widget, GameFiltersWidget> {
    }
}
