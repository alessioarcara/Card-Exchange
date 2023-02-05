package com.aadm.cardexchange.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NewExchangeViewImpl extends Composite implements NewExchangeView {
    private static final NewExchangeViewImpl.NewExchangeViewImplUIBinder uiBinder = GWT.create(NewExchangeViewImpl.NewExchangeViewImplUIBinder.class);
    Presenter presenter;

    @UiField
    HeadingElement pageTitle; //temporary element to be able to see the page has been loaded
    @UiField
    DivElement receiverEmail;
    @UiField
    DivElement selectedCard;

    public NewExchangeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setData(String receiverUserEmail, String selectedCardId) {
        pageTitle.setInnerHTML("New Exchange Page");
        receiverEmail.setInnerHTML(receiverUserEmail);
        selectedCard.setInnerHTML(selectedCardId);
    }

    @Override
    public void setPresenter(NewExchangeView.Presenter presenter) {
        this.presenter = presenter;
    }

    interface NewExchangeViewImplUIBinder extends UiBinder<Widget, NewExchangeViewImpl> {
    }
}
