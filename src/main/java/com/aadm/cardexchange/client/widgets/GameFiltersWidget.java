package com.aadm.cardexchange.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameFiltersWidget extends Composite {
    private static final GameFiltersUIBinder uiBinder = GWT.create(GameFiltersUIBinder.class);
    @UiField
    public TextBox textInput;
    @UiField
    public HTMLPanel categoriesPanel;
    public List<ListBox> categoryListBoxes;
    @UiField
    public ListBox textOptions;
    public List<CheckBox> checkBoxes;
    @UiField
    HTMLPanel checkboxesPanel;

    @UiConstructor
    public GameFiltersWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        categoryListBoxes = new ArrayList<>();
        checkBoxes = new ArrayList<>();
    }

    public void setCategories(List<Set<String>> catProperties) {
        categoryListBoxes.clear();
        catProperties.forEach(prop -> {
            ListBox listBox = new ListBox();
            listBox.addItem("all");
            for (String option : prop) {
                listBox.addItem(option);
            }
            categoryListBoxes.add(listBox);
            categoriesPanel.add(listBox);
        });
    }

    public void setTextOptions(String[] textProperties) {
        textOptions.clear();
        for (String textProperty : textProperties) {
            textOptions.addItem(textProperty);
        }
    }

    public void setCheckBoxes(String[] variants) {
        checkboxesPanel.clear();
        checkBoxes.clear();
        for (String variant : variants) {
            CheckBox checkBox = new CheckBox(variant);
            checkboxesPanel.add(checkBox);
            checkBoxes.add(checkBox);
        }
    }

    interface GameFiltersUIBinder extends UiBinder<Widget, GameFiltersWidget> {
    }
}
