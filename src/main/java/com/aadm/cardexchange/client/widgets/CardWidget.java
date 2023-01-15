package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.MagicCardDecorator;
import com.aadm.cardexchange.shared.models.PokemonCardDecorator;
import com.aadm.cardexchange.shared.models.YuGiOhCardDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class CardWidget extends Composite {
    private static final CardUIBinder uiBinder = GWT.create(CardUIBinder.class);
    @UiField
    DivElement nameDiv;
    @UiField
    DivElement descDiv;
    @UiField
    DivElement typeDiv;

    @UiField
    DivElement details;

    @UiField
    Image imageDiv;
    @UiField
    PushButton detailsButton;

    public CardWidget(FunctionInterface parent, CardDecorator card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        descDiv.setInnerHTML(card.getDescription());
        typeDiv.setInnerHTML(card.getType());

        if (card instanceof YuGiOhCardDecorator) {
            imageDiv.setUrl(((YuGiOhCardDecorator) card).getSmallImageUrl());
            details.setInnerHTML("Race: " + ((YuGiOhCardDecorator) card).getRace());
        }
        if (card instanceof PokemonCardDecorator) {
            imageDiv.setUrl(((PokemonCardDecorator) card).getImageUrl());
            String val = "<br>Artist: " + ((PokemonCardDecorator) card).getArtist();
            val += "<br>Rarity: " + ((PokemonCardDecorator) card).getRarity();
            val += "<br>First edition: " + ((PokemonCardDecorator) card).getIsFirstEdition();
            val += "<br>Holo: " + ((PokemonCardDecorator) card).getIsHolo();
            val += "<br>Normal: " + ((PokemonCardDecorator) card).getIsNormal();
            val += "<br>Reverse: " + ((PokemonCardDecorator) card).getIsReverse();
            val += "<br>Promo: " + ((PokemonCardDecorator) card).getIsPromo();
            details.setInnerHTML(val);
        }
        if (card instanceof MagicCardDecorator) {
            String val = "<br>Artist: " + ((MagicCardDecorator) card).getArtist();
            val += "<br>Rarity: " + ((MagicCardDecorator) card).getRarity();
            val += "<br>Foil: " + ((MagicCardDecorator) card).getHasFoil();
            val += "<br>Alternative: " + ((MagicCardDecorator) card).getIsAlternative();
            val += "<br>Full Art: " + ((MagicCardDecorator) card).getIsFullArt();
            val += "<br>Promo: " + ((MagicCardDecorator) card).getIsPromo();
            details.setInnerHTML(val);
        }


        detailsButton.addClickHandler(clickEvent -> parent.handleClickCard());
        if (!(card instanceof MagicCardDecorator)) {
            imageDiv.setPixelSize(69, 100);
        } else {
            imageDiv.setPixelSize(0, 0);
        }

    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
