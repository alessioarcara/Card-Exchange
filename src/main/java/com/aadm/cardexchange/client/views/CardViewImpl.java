package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.CardWidget;
import com.aadm.cardexchange.client.widgets.UserListWidget;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CardViewImpl extends Composite implements CardView {
    private static final CardsViewImplUIBinder uiBinder = GWT.create(CardsViewImplUIBinder.class);
    @UiField
    SpanElement cardGame;
    @UiField
    HeadingElement cardName;
    @UiField
    Image cardImage;
    @UiField
    HTMLPanel userLists;
    Presenter presenter;

    public CardViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        userLists.add(new UserListWidget("Owned by", new String[]{"alessio.arcara@hotmail.com", "davide.fermi@gmail.com", "alessia.crimaldi@virgilio.it", "alessio.arcara@hotmail.com", "davide.fermi@gmail.com", "alessia.crimaldi@virgilio.it"}));
        userLists.add(new UserListWidget("Desired by", new String[]{"matteo.sacco04@studio.unibo.it"}));
    }

    public void setData(CardDecorator data) {
        String gameType = "";
        String imageUrl = "";
        if (data instanceof MagicCardDecorator) {
            gameType = "Magic";
            imageUrl = CardWidget.DEFAULT_IMAGE;
        } else if (data instanceof PokemonCardDecorator) {
            gameType = "Pokemon";
            imageUrl = ((PokemonCardDecorator) data).getImageUrl();
        } else if (data instanceof YuGiOhCardDecorator) {
            gameType = "YuGiOh";
            imageUrl = ((YuGiOhCardDecorator) data).getImageUrl();
        }
        cardGame.setInnerHTML(gameType);
        cardName.setInnerHTML(data.getName());
        cardImage.setUrl(imageUrl);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface CardsViewImplUIBinder extends UiBinder<Widget, CardViewImpl> {
    }
}
