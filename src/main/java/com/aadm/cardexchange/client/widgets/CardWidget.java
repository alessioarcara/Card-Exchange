package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class CardWidget extends Composite {
    private static final CardUIBinder uiBinder = GWT.create(CardUIBinder.class);
    @UiField
    DivElement nameDiv;
    @UiField
    DivElement typeDiv;
    @UiField
    DivElement detailsDiv;

    @UiField
    DivElement propertiesDiv;
    @UiField
    Image image;
    @UiField
    PushButton detailsButton;

    public CardWidget(ImperativeHandleCard parent, CardDecorator card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        typeDiv.setInnerHTML("<b>Type:</b><br>" + card.getType());
        image.setPixelSize(90, 131);

        String details = "";
        String properties = "";
        String imageUrl = "";
        String errorImage = "placeholders/";
        Game game;
        if (card instanceof YuGiOhCardDecorator) {
            imageUrl = ((YuGiOhCardDecorator) card).getImageUrl();
            errorImage += "yugioh-placeholder.png";
            details += "<br><b>Race</b>:<br>" + ((YuGiOhCardDecorator) card).getRace();
            game = Game.YuGiOh;
        } else if (card instanceof PokemonCardDecorator) {
            imageUrl = ((PokemonCardDecorator) card).getImageUrl();
            errorImage += "pokemon-placeholder.png";
            details += "<br><b>Artist</b>:<br>" + ((PokemonCardDecorator) card).getArtist();
            details += "<br><br><b>Rarity</b>:<br>" + ((PokemonCardDecorator) card).getRarity();
            properties += (((PokemonCardDecorator) card).getIsFirstEdition() ? "<div><b>First edition</b></div>" : "");
            properties += (((PokemonCardDecorator) card).getIsHolo() ? "<div><b>Holo</b></div>" : "");
            properties += (((PokemonCardDecorator) card).getIsNormal() ? "<div><b>Normal</b></div>" : "");
            properties += (((PokemonCardDecorator) card).getIsReverse() ? "<div><b>Reverse</b></div>" : "");
            properties += (((PokemonCardDecorator) card).getIsPromo() ? "<div><b>Promo</b></div>" : "");
            game = Game.Pokemon;
        } else if (card instanceof MagicCardDecorator) {
            errorImage += "magic-placeholder.png";
            details += "<br><b>Artist</b>:<br>" + ((MagicCardDecorator) card).getArtist();
            details += "<br><br><b>Rarity</b>:<br>" + ((MagicCardDecorator) card).getRarity();
            properties += (((MagicCardDecorator) card).getHasFoil() ? "<div><b>Foil</b></div>" : "");
            properties += (((MagicCardDecorator) card).getIsAlternative() ? "<div><b>Alternative</b></div>" : "");
            properties += (((MagicCardDecorator) card).getIsFullArt() ? "<div><b>Full Art</b></div>" : "");
            properties += (((MagicCardDecorator) card).getIsPromo() ? "<div><b>Promo</b></div>" : "");
            game = Game.Pokemon;
        } else {
            game = null;
        }

        image.setUrl(imageUrl);
        detailsDiv.setInnerHTML(details);
        propertiesDiv.setInnerHTML(properties);
        detailsButton.addClickHandler(clickEvent -> parent.onOpenDetailsClick(game, card.getId()));
        String finalErrorImage = errorImage;
        image.addErrorHandler((errorEvent) -> image.setUrl(GWT.getHostPageBaseURL() + finalErrorImage));
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
