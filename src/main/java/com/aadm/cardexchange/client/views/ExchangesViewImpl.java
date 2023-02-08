package com.aadm.cardexchange.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ExchangesViewImpl extends Composite implements ExchangesView {
    private static final ExchangesViewImplUIBinder uiBinder = GWT.create(ExchangesViewImplUIBinder.class);

    public ExchangesViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(Presenter presenter) {
    }

    interface ExchangesViewImplUIBinder extends UiBinder<Widget, ExchangesViewImpl> {
    }
}
