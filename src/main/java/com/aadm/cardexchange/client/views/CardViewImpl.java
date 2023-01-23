package com.aadm.cardexchange.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CardViewImpl extends Composite implements CardView {

    private static final CardsViewImplUIBinder uiBinder = GWT.create(CardsViewImplUIBinder.class);

    private Presenter presenter;

    public CardViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface CardsViewImplUIBinder extends UiBinder<Widget, CardViewImpl> {
    }
}
