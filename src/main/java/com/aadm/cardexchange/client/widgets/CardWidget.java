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

    public CardWidget(ImperativeHandleCard parent, CardDecorator card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        descDiv.setInnerHTML("<b>Description:</b><br>" + card.getDescription());
        typeDiv.setInnerHTML("<b>Type:</b><br>" + card.getType());
        imageDiv.setPixelSize(90, 131);

        if (card instanceof YuGiOhCardDecorator) {
            imageDiv.setUrl(((YuGiOhCardDecorator) card).getSmallImageUrl());
            details.setInnerHTML("<b>Race</b>: " + ((YuGiOhCardDecorator) card).getRace());
        }
        else if (card instanceof PokemonCardDecorator) {
            imageDiv.setUrl(((PokemonCardDecorator) card).getImageUrl());
            String val = "<br><b>Artist</b>: " + ((PokemonCardDecorator) card).getArtist();
            val += "<br><b>Rarity</b>: " + ((PokemonCardDecorator) card).getRarity();
            val += (((PokemonCardDecorator) card).getIsFirstEdition() ? "<br><b>First edition</b>" : "");
            val += (((PokemonCardDecorator) card).getIsHolo() ? "<br><b>Holo</b>" : "");
            val += (((PokemonCardDecorator) card).getIsNormal() ? "<br><b>Normal</b>" : "");
            val += (((PokemonCardDecorator) card).getIsReverse() ? "<br><b>Reverse</b>" : "");
            val += (((PokemonCardDecorator) card).getIsPromo() ? "<br><b>Promo</b>" : "");
            details.setInnerHTML(val);
        }
        else if (card instanceof MagicCardDecorator) {
            String val = "<br><b>Artist</b>: " + ((MagicCardDecorator) card).getArtist();
            val += "<br><b>Rarity</b>: " + ((MagicCardDecorator) card).getRarity();
            val += (((MagicCardDecorator) card).getHasFoil() ? "<br><b>Foil</b>" : "");
            val += (((MagicCardDecorator) card).getIsAlternative() ? "<br><b>Alternative</b>" : "");
            val += (((MagicCardDecorator) card).getIsFullArt() ? "<br><b>Full Art</b>" : "");
            val += (((MagicCardDecorator) card).getIsPromo() ? "<br><b>Promo</b>" : "");
            details.setInnerHTML(val);
            imageDiv.setPixelSize(0, 0);
        }

        detailsButton.addClickHandler(clickEvent -> parent.handleClickCard());
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
