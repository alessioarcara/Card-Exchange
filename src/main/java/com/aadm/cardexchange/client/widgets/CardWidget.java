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
    DivElement mainPropertiesDiv;
    @UiField
    DivElement otherPropertiesDiv;
    @UiField
    Image image;
    @UiField
    PushButton detailsButton;

    public CardWidget(ImperativeHandleCard parent, CardDecorator card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        image.setPixelSize(90, 131);

        String mainProperties = createMainPropertyHTML("Type", card.getType());
        String otherProperties = "";
        String imageUrl = "";
        Game game;
        if (card instanceof YuGiOhCardDecorator) {
            imageUrl = ((YuGiOhCardDecorator) card).getImageUrl();
            mainProperties += createMainPropertyHTML("Race", ((YuGiOhCardDecorator) card).getRace());
            game = Game.YuGiOh;
        } else if (card instanceof PokemonCardDecorator) {
            imageUrl = ((PokemonCardDecorator) card).getImageUrl();
            mainProperties += createMainPropertyHTML("Artist", ((PokemonCardDecorator) card).getArtist());
            mainProperties += createMainPropertyHTML("Rarity", ((PokemonCardDecorator) card).getRarity());
            otherProperties += createOtherPropertyHTML("First Edition", ((PokemonCardDecorator) card).getIsFirstEdition());
            otherProperties += createOtherPropertyHTML("Holo", ((PokemonCardDecorator) card).getIsHolo());
            otherProperties += createOtherPropertyHTML("Normal", ((PokemonCardDecorator) card).getIsNormal());
            otherProperties += createOtherPropertyHTML("Reverse", ((PokemonCardDecorator) card).getIsReverse());
            otherProperties += createOtherPropertyHTML("Promo", ((PokemonCardDecorator) card).getIsPromo());
            game = Game.Pokemon;
        } else if (card instanceof MagicCardDecorator) {
            mainProperties += createMainPropertyHTML("Artist", ((MagicCardDecorator) card).getArtist());
            mainProperties += createMainPropertyHTML("Rarity", ((MagicCardDecorator) card).getRarity());
            otherProperties += createOtherPropertyHTML("Foil", ((MagicCardDecorator) card).getHasFoil());
            otherProperties += createOtherPropertyHTML("Alternative", ((MagicCardDecorator) card).getIsAlternative());
            otherProperties += createOtherPropertyHTML("Full Art", ((MagicCardDecorator) card).getIsFullArt());
            otherProperties += createOtherPropertyHTML("Promo", ((MagicCardDecorator) card).getIsPromo());
            otherProperties += createOtherPropertyHTML("Reprint", ((MagicCardDecorator) card).getIsReprint());
            game = Game.Magic;
        } else {
            game = null;
        }
        image.setUrl(imageUrl);
        mainPropertiesDiv.setInnerHTML(mainProperties);
        otherPropertiesDiv.setInnerHTML(otherProperties);
        detailsButton.addClickHandler(clickEvent -> parent.onOpenDetailsClick(game, card.getId()));
        image.addErrorHandler((errorEvent) -> image.setUrl(GWT.getHostPageBaseURL() + DEFAULT_IMAGE_PATHS.get(game)));
    }

    private String createMainPropertyHTML(String detail, String text) {
        return "<div>" +
                "<div style=\"font-weight: bold\">" + detail + ":</div>" +
                text +
                "</div>";
    }

    private String createOtherPropertyHTML(String property, boolean isTrue) {
        return isTrue ? "<div>" + property + "</div>" : "";
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
