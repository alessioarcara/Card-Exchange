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
            val += "<br>" + (((PokemonCardDecorator) card).getIsFirstEdition() ? "<b>First edition</b>" : "<s>First edition</s>");
            val += "<br>" + (((PokemonCardDecorator) card).getIsHolo() ? "<b>Holo</b>" : "<s>Holo</s>");
            val += "<br>" + (((PokemonCardDecorator) card).getIsNormal() ? "<b>Normal</b>" : "<s>Normal</s>");
            val += "<br>" + (((PokemonCardDecorator) card).getIsReverse() ? "<b>Reverse</b>" : "<s>Reverse</s>");
            val += "<br>" + (((PokemonCardDecorator) card).getIsPromo() ? "<b>Promo</b>" : "<s>Promo</s>");
            details.setInnerHTML(val);
        }
        if (card instanceof MagicCardDecorator) {
            String val = "<br>Artist: " + ((MagicCardDecorator) card).getArtist();
            val += "<br>Rarity: " + ((MagicCardDecorator) card).getRarity();
            val += "<br>" + (((MagicCardDecorator) card).getHasFoil() ? "<b>Foil</b>" : "<s>Foil</s>");
            val += "<br>" + (((MagicCardDecorator) card).getIsAlternative() ? "<b>Alternative</b>" : "<s>Alternative</s>");
            val += "<br>" + (((MagicCardDecorator) card).getIsFullArt() ? "<b>Full Art</b>" : "<s>Full Art</s>");
            val += "<br>" + (((MagicCardDecorator) card).getIsPromo() ? "<b>Promo</b>" : "<s>Promo</s>");
            details.setInnerHTML(val);
            imageDiv.setPixelSize(0, 0);
        }

        detailsButton.addClickHandler(clickEvent -> parent.handleClickCard());

        if (!(card instanceof MagicCardDecorator)) {
            imageDiv.setPixelSize(90, 131);
        }
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
