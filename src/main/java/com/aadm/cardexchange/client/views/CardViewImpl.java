package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.UserListWidget;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
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
    DivElement cardDescription;
    @UiField
    DivElement cardOptionType;
    @UiField
    DivElement optionArtist;
    @UiField
    DivElement cardOptionArtist;
    @UiField
    DivElement optionRarity;
    @UiField
    DivElement cardOptionRarity;
    @UiField
    DivElement optionRace;
    @UiField
    DivElement cardOptionRace;
    @UiField
    DivElement optionOtherProperties;
    @UiField
    DivElement cardOptionFoil;
    @UiField
    DivElement cardOptionAlternative;
    @UiField
    DivElement cardOptionFullArt;
    @UiField
    DivElement cardOptionReprint;
    @UiField
    DivElement cardOptionFirstEd;
    @UiField
    DivElement cardOptionHolo;
    @UiField
    DivElement cardOptionNormal;
    @UiField
    DivElement cardOptionReverse;
    @UiField
    DivElement cardOptionPromo;
    @UiField
    DivElement addToDeckSection;
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
        String errorImage = "placeholders/";
        String artist = "";
        String rarity = "";
        String race = "";
        boolean hasOptions = false;
        optionArtist.setAttribute("style", "display: none");
        optionRarity.setAttribute("style", "display: none");
        optionRace.setAttribute("style", "display: none");
        optionOtherProperties.setAttribute("style", "display: none");
        cardOptionFoil.setAttribute("style", "display: none");
        cardOptionAlternative.setAttribute("style", "display: none");
        cardOptionFullArt.setAttribute("style", "display: none");
        cardOptionReprint.setAttribute("style", "display: none");
        cardOptionFirstEd.setAttribute("style", "display: none");
        cardOptionHolo.setAttribute("style", "display: none");
        cardOptionNormal.setAttribute("style", "display: none");
        cardOptionReverse.setAttribute("style", "display: none");
        cardOptionPromo.setAttribute("style", "display: none");

        if (data instanceof MagicCardDecorator) {
            gameType = "Magic";
            errorImage += "magic-placeholder.png";
            artist = ((MagicCardDecorator) data).getArtist();
            rarity = ((MagicCardDecorator) data).getRarity();
            if (((MagicCardDecorator) data).getHasFoil()) {
                hasOptions = true;
                cardOptionFoil.getStyle().clearDisplay();
            }
            if (((MagicCardDecorator) data).getIsAlternative()) {
                hasOptions = true;
                cardOptionAlternative.getStyle().clearDisplay();
            }
            if (((MagicCardDecorator) data).getIsFullArt()) {
                hasOptions = true;
                cardOptionFullArt.getStyle().clearDisplay();
            }
            if (((MagicCardDecorator) data).getIsPromo()) {
                hasOptions = true;
                cardOptionPromo.getStyle().clearDisplay();
            }
            if (((MagicCardDecorator) data).getIsReprint()) {
                hasOptions = true;
                cardOptionReprint.getStyle().clearDisplay();
            }
        } else if (data instanceof PokemonCardDecorator) {
            gameType = "Pokemon";
            errorImage += "pokemon-placeholder.png";
            imageUrl = ((PokemonCardDecorator) data).getImageUrl();
            artist = ((PokemonCardDecorator) data).getArtist();
            rarity = ((PokemonCardDecorator) data).getRarity();
            if (((PokemonCardDecorator) data).getIsFirstEdition()) {
                hasOptions = true;
                cardOptionFirstEd.getStyle().clearDisplay();
            }
            if (((PokemonCardDecorator) data).getIsHolo()) {
                hasOptions = true;
                cardOptionHolo.getStyle().clearDisplay();
            }
            if (((PokemonCardDecorator) data).getIsNormal()) {
                hasOptions = true;
                cardOptionNormal.getStyle().clearDisplay();
            }
            if (((PokemonCardDecorator) data).getIsReverse()) {
                hasOptions = true;
                cardOptionReverse.getStyle().clearDisplay();
            }
            if (((PokemonCardDecorator) data).getIsPromo()) {
                hasOptions = true;
                cardOptionPromo.getStyle().clearDisplay();
            }
        } else if (data instanceof YuGiOhCardDecorator) {
            gameType = "YuGiOh";
            errorImage += "yugiho-placeholder.png";
            imageUrl = ((YuGiOhCardDecorator) data).getImageUrl();
            race = ((YuGiOhCardDecorator) data).getRace();
        }

        final String finalErrorMessage = errorImage;
        cardImage.addErrorHandler((error) -> cardImage.setUrl(GWT.getHostPageBaseURL() + finalErrorMessage));
        cardGame.setInnerHTML(gameType);
        cardName.setInnerHTML(data.getName());
        cardImage.setUrl(imageUrl);
        cardDescription.setInnerHTML(data.getDescription());
        cardOptionType.setInnerHTML(data.getType());
        cardOptionArtist.setInnerHTML(artist);
        cardOptionRarity.setInnerHTML(rarity);
        cardOptionRace.setInnerHTML(race);
        if (!artist.isEmpty()) { optionArtist.getStyle().clearDisplay(); }
        if (!rarity.isEmpty()) { optionRarity.getStyle().clearDisplay(); }
        if (!race.isEmpty()) { optionRace.getStyle().clearDisplay(); }
        if (hasOptions) { optionOtherProperties.getStyle().clearDisplay(); }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface CardsViewImplUIBinder extends UiBinder<Widget, CardViewImpl> {
    }
}
