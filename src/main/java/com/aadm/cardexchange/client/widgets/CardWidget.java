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
    public static final String DEFAULT_IMAGE = "https://orig10.deviantart.net/69f2/f/2016/289/4/1/ygo_card_backing__final__by_icycatelf-dal6wsb.png";
    @UiField
    DivElement nameDiv;
    @UiField
    DivElement descDiv;
    @UiField
    DivElement typeDiv;
    @UiField
    DivElement details;
    @UiField
    Image image;
    @UiField
    PushButton detailsButton;

    public CardWidget(ImperativeHandleCard parent, CardDecorator card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        descDiv.setInnerHTML("<b>Description:</b><br>" + card.getDescription());
        typeDiv.setInnerHTML("<b>Type:</b><br>" + card.getType());
        image.setPixelSize(90, 131);

        String html = "";
        String imageUrl = "";
        Game game;
        if (card instanceof YuGiOhCardDecorator) {
            imageUrl = ((YuGiOhCardDecorator) card).getImageUrl();
            html += "<b>Race</b>: " + ((YuGiOhCardDecorator) card).getRace();
            game = Game.YuGiOh;
        } else if (card instanceof PokemonCardDecorator) {
            imageUrl = ((PokemonCardDecorator) card).getImageUrl();
            html += "<br><b>Artist</b>: " + ((PokemonCardDecorator) card).getArtist();
            html += "<br><b>Rarity</b>: " + ((PokemonCardDecorator) card).getRarity();
            html += (((PokemonCardDecorator) card).getIsFirstEdition() ? "<br><b>First edition</b>" : "");
            html += (((PokemonCardDecorator) card).getIsHolo() ? "<br><b>Holo</b>" : "");
            html += (((PokemonCardDecorator) card).getIsNormal() ? "<br><b>Normal</b>" : "");
            html += (((PokemonCardDecorator) card).getIsReverse() ? "<br><b>Reverse</b>" : "");
            html += (((PokemonCardDecorator) card).getIsPromo() ? "<br><b>Promo</b>" : "");
            game = Game.Pokemon;
        } else if (card instanceof MagicCardDecorator) {
            html += "<br><b>Artist</b>: " + ((MagicCardDecorator) card).getArtist();
            html += "<br><b>Rarity</b>: " + ((MagicCardDecorator) card).getRarity();
            html += (((MagicCardDecorator) card).getHasFoil() ? "<br><b>Foil</b>" : "");
            html += (((MagicCardDecorator) card).getIsAlternative() ? "<br><b>Alternative</b>" : "");
            html += (((MagicCardDecorator) card).getIsFullArt() ? "<br><b>Full Art</b>" : "");
            html += (((MagicCardDecorator) card).getIsPromo() ? "<br><b>Promo</b>" : "");
            game = Game.Magic;
        } else {
            game = null;
        }

        image.setUrl(imageUrl);
        details.setInnerHTML(html);
        detailsButton.addClickHandler(clickEvent -> parent.onOpenDetailsClick(game, card.getId()));
        image.addErrorHandler((errorEvent) -> image.setUrl(DEFAULT_IMAGE));
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
