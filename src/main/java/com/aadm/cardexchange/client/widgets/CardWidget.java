package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.utils.DefaultImagePathLookupTable;
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
    DivElement detailsDiv;
    @UiField
    DivElement propertiesDiv;
    @UiField
    Image image;
    @UiField
    PushButton detailsButton;

    public CardWidget(ImperativeHandleCard parent, Card card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        image.setPixelSize(90, 131);

        String details = createDetailHTML("Type", card.getType());
        StringBuilder properties = new StringBuilder();
        String imageUrl = "";
        Game game;

        if (card instanceof YuGiOhCard) {
            imageUrl = ((YuGiOhCard) card).getImageUrl();
            details += createDetailHTML("Race", ((YuGiOhCard) card).getRace());
            game = Game.YUGIOH;
        } else if (card instanceof PokemonCard) {
            imageUrl = ((PokemonCard) card).getImageUrl();
            details += createDetailHTML("Artist", ((PokemonCard) card).getArtist());
            details += createDetailHTML("Rarity", ((PokemonCard) card).getRarity());
            game = Game.POKEMON;
        } else if (card instanceof MagicCard) {
            details += createDetailHTML("Artist", ((MagicCard) card).getArtist());
            details += createDetailHTML("Rarity", ((MagicCard) card).getRarity());
            game = Game.MAGIC;
        } else {
            game = null;
        }

        for (String variant : card.getVariants()) {
            properties.append("<div>").append(variant).append("</div>");
        }

        image.setUrl(imageUrl);
        detailsDiv.setInnerHTML(details);
        propertiesDiv.setInnerHTML(String.valueOf(properties));
        detailsButton.addClickHandler(clickEvent -> parent.onOpenDetailsClick(game, card.getId()));
        image.addErrorHandler((errorEvent) -> image.setUrl(GWT.getHostPageBaseURL() + DefaultImagePathLookupTable.getPath(game)));
    }

    private String createDetailHTML(String detail, String text) {
        return "<div>" +
                "<div style=\"font-weight: bold\">" + detail + ":</div>" +
                text +
                "</div>";
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
