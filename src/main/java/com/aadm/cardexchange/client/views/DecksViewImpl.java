package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.client.widgets.ImperativeHandleDeck;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.function.BiConsumer;

public class DecksViewImpl extends Composite implements DecksView, ImperativeHandleDeck {
    private static final DecksViewImpl.DecksViewImplUIBinder uiBinder = GWT.create(DecksViewImpl.DecksViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    HTMLPanel decksContainer;

    public DecksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setData(List<String> data) {
        for (String deckName : data) {
            decksContainer.add(new DeckWidget(this, deckName));
        }
    }

    @Override
    public void onShowDeck(String deckName, BiConsumer<List<PhysicalCardWithName>, String> setDeckData) {
        presenter.fetchUserDeck(deckName, setDeckData);
    }

    @Override
    public void resetData() {
        decksContainer.clear();
    }

    @Override
    public void setPresenter(DecksView.Presenter presenter) {
        this.presenter = presenter;
    }

    interface DecksViewImplUIBinder extends UiBinder<Widget, DecksViewImpl> {
    }
}
