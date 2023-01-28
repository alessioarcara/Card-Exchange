package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.aadm.cardexchange.shared.models.Deck;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class DecksViewImpl extends Composite implements DecksView {
    private static final DecksViewImpl.DecksViewImplUIBinder uiBinder = GWT.create(DecksViewImpl.DecksViewImplUIBinder.class);
    Presenter presenter;

    @UiField
    HTMLPanel decksContainer;

    public DecksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setData(List<Deck> data) {
        decksContainer.clear();
        for (Deck deck : data) {
            decksContainer.add(new DeckWidget(deck));
        }
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
