package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.DeckWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class DecksViewImpl extends Composite implements DecksView {
    private static final DecksViewImpl.DecksViewImplUIBinder uiBinder = GWT.create(DecksViewImpl.DecksViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    HTMLPanel decksContainer;

    public DecksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

    }

    @Override
    public void setPresenter(DecksView.Presenter presenter) {
        this.presenter = presenter;

        String[] mockDeckNames = {"Owned", "Wished", "Custom 1", "Custom 2", "Custom 3"};
/*        for (String deckName: mockDeckNames) {
            decksContainer.add(new DeckWidget(deckName));
        }*/
    }

    interface DecksViewImplUIBinder extends UiBinder<Widget, DecksViewImpl> {
    }
}
