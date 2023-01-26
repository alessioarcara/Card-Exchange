package com.aadm.cardexchange.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;

public class DecksViewImpl extends Composite implements DecksView {
    private static final DecksViewImpl.DecksViewImplUIBinder uiBinder = GWT.create(DecksViewImpl.DecksViewImplUIBinder.class);
    Presenter presenter;

    public DecksViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(DecksView.Presenter presenter) {
        this.presenter = presenter;
    }

    interface DecksViewImplUIBinder extends UiBinder<Widget, DecksViewImpl> {
    }
}
