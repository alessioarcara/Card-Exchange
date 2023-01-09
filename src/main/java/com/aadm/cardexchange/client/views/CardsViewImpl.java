package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.CardWidget;
import com.aadm.cardexchange.client.widgets.FunctionInterface;
import com.aadm.cardexchange.shared.models.Card;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class CardsViewImpl extends Composite implements CardsView, FunctionInterface {

    private static final CardsViewImplUIBinder uiBinder = GWT.create(CardsViewImplUIBinder.class);
    @UiField
    HTMLPanel cardsPanel;
    @UiField
    DivElement placeholder;
    private Presenter presenter;

    public CardsViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("addButton")
    public void handleClick(ClickEvent e) {
        handleAddData("testName", "testDesc");
    }

    @Override
    public void setData(Card[] data) {
        placeholder.removeFromParent();
        for (Card card : data) {
            cardsPanel.add(new CardWidget(this, card.getName(), card.getDescription()));
        }
    }

    @Override
    public void handleAddData(String cardName, String cardDesc) {
        if (presenter.addCard(cardName, cardDesc)) {
            cardsPanel.add(new CardWidget(this, cardName, cardDesc));
        } else {
            Window.alert("Error adding card");
        }
    }

    @Override
    public void handleRemoveData(CardWidget w) {
        if (presenter.removeCard(w.getId())) {
            cardsPanel.remove(w);
        } else {
            Window.alert("Error removing card");
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface CardsViewImplUIBinder extends UiBinder<Widget, CardsViewImpl> {
    }
}
