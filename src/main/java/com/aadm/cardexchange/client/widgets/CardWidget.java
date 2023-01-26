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

import java.util.HashMap;
import java.util.Map;

public class CardWidget extends Composite {
    private static final CardUIBinder uiBinder = GWT.create(CardUIBinder.class);
    private static final Map<Game, String> DEFAULT_IMAGE_PATHS = new HashMap<Game, String>() {{
        put(Game.Magic, "placeholders/magic-placeholder.png");
        put(Game.Pokemon, "placeholders/pokemon-placeholder.png");
        put(Game.YuGiOh, "placeholders/yugioh-placeholder.png");
    }};
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

    public CardWidget(ImperativeHandleCard parent, CardDecorator card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        image.setPixelSize(90, 131);

        String details = createDetailHTML("Type", card.getType());
        String properties = "";
        String imageUrl = "";
        Game game;

        if (card instanceof YuGiOhCardDecorator) {
            imageUrl = ((YuGiOhCardDecorator) card).getImageUrl();
            details += createDetailHTML("Race", ((YuGiOhCardDecorator) card).getRace());
            game = Game.YuGiOh;
        } else if (card instanceof PokemonCardDecorator) {
            imageUrl = ((PokemonCardDecorator) card).getImageUrl();
            details += createDetailHTML("Artist", ((PokemonCardDecorator) card).getArtist());
            details += createDetailHTML("Rarity", ((PokemonCardDecorator) card).getRarity());
            properties += createPropertyHTML("First Edition", ((PokemonCardDecorator) card).getIsFirstEdition());
            properties += createPropertyHTML("Holo", ((PokemonCardDecorator) card).getIsHolo());
            properties += createPropertyHTML("Normal", ((PokemonCardDecorator) card).getIsNormal());
            properties += createPropertyHTML("Reverse", ((PokemonCardDecorator) card).getIsReverse());
            properties += createPropertyHTML("Promo", ((PokemonCardDecorator) card).getIsPromo());
            game = Game.Pokemon;
        } else if (card instanceof MagicCardDecorator) {
            details += createDetailHTML("Artist", ((MagicCardDecorator) card).getArtist());
            details += createDetailHTML("Rarity", ((MagicCardDecorator) card).getRarity());
            properties += createPropertyHTML("Foil", ((MagicCardDecorator) card).getHasFoil());
            properties += createPropertyHTML("Alternative", ((MagicCardDecorator) card).getIsAlternative());
            properties += createPropertyHTML("Full Art", ((MagicCardDecorator) card).getIsFullArt());
            properties += createPropertyHTML("Promo", ((MagicCardDecorator) card).getIsPromo());
            properties += createPropertyHTML("Reprint", ((MagicCardDecorator) card).getIsReprint());
            game = Game.Magic;
        } else {
            game = null;
        }

        image.setUrl(imageUrl);
        detailsDiv.setInnerHTML(details);
        propertiesDiv.setInnerHTML(properties);
        detailsButton.addClickHandler(clickEvent -> parent.onOpenDetailsClick(game, card.getId()));
        image.addErrorHandler((errorEvent) -> image.setUrl(GWT.getHostPageBaseURL() + DEFAULT_IMAGE_PATHS.get(game)));
    }

    private String createDetailHTML(String detail, String text) {
        return "<div>" +
                "<div style=\"font-weight: bold\">" + detail + ":</div>" +
                text +
                "</div>";
    }

    private String createPropertyHTML(String property, boolean isTrue) {
        return isTrue ? "<div>" + property + "</div>" : "";
    }


    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
