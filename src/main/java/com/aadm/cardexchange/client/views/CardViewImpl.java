package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.*;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class CardViewImpl extends Composite implements CardView, ImperativeHandleAddCardToDeck, ImperativeHandleAddCardToDeckModal {
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
    HTMLPanel addCardToDeckContainer;
    @UiField
    HTMLPanel userLists;
    Presenter presenter;
    AddCardToDeckWidget addCardToDeckWidget = new AddCardToDeckWidget(this);
    DialogBox dialog = new AddCardToDeckModalWidget(this);

    public CardViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setData(CardDecorator data) {
        String gameType = "";
        String imageUrl = "";
        String errorImage = "placeholders/";
        String artist = "";
        String rarity = "";
        String race = "";
        boolean hasOtherProperties = false;

        setPropertiesUnvisible();

        if (data instanceof MagicCardDecorator) {
            gameType = "Magic";
            errorImage += "magic-placeholder.png";
            artist = ((MagicCardDecorator) data).getArtist();
            rarity = ((MagicCardDecorator) data).getRarity();
            hasOtherProperties = isOtherPropertyVisible(((MagicCardDecorator) data).getHasFoil(), false, cardOptionFoil);
            hasOtherProperties = isOtherPropertyVisible(((MagicCardDecorator) data).getIsAlternative(), hasOtherProperties, cardOptionAlternative);
            hasOtherProperties = isOtherPropertyVisible(((MagicCardDecorator) data).getIsFullArt(), hasOtherProperties, cardOptionFullArt);
            hasOtherProperties = isOtherPropertyVisible(((MagicCardDecorator) data).getIsPromo(), hasOtherProperties, cardOptionPromo);
            hasOtherProperties = isOtherPropertyVisible(((MagicCardDecorator) data).getIsReprint(), hasOtherProperties, cardOptionReprint);
        } else if (data instanceof PokemonCardDecorator) {
            gameType = "Pokemon";
            errorImage += "pokemon-placeholder.png";
            imageUrl = ((PokemonCardDecorator) data).getImageUrl();
            artist = ((PokemonCardDecorator) data).getArtist();
            rarity = ((PokemonCardDecorator) data).getRarity();
            hasOtherProperties = isOtherPropertyVisible(((PokemonCardDecorator) data).getIsFirstEdition(), false, cardOptionFirstEd);
            hasOtherProperties = isOtherPropertyVisible(((PokemonCardDecorator) data).getIsHolo(), hasOtherProperties, cardOptionHolo);
            hasOtherProperties = isOtherPropertyVisible(((PokemonCardDecorator) data).getIsNormal(), hasOtherProperties, cardOptionNormal);
            hasOtherProperties = isOtherPropertyVisible(((PokemonCardDecorator) data).getIsReverse(), hasOtherProperties, cardOptionReverse);
            hasOtherProperties = isOtherPropertyVisible(((PokemonCardDecorator) data).getIsPromo(), hasOtherProperties, cardOptionPromo);
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
        if (!artist.isEmpty()) optionArtist.getStyle().clearDisplay();
        if (!rarity.isEmpty()) optionRarity.getStyle().clearDisplay();
        if (!race.isEmpty()) optionRace.getStyle().clearDisplay();
        if (hasOtherProperties) optionOtherProperties.getStyle().clearDisplay();
    }

    private void setPropertiesUnvisible() {
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
    }

    private boolean isOtherPropertyVisible(boolean propertyIsTrue, boolean hasOtherProperties, DivElement elem) {
        if (propertyIsTrue) {
            hasOtherProperties = true;
            elem.getStyle().clearDisplay();
        }
        return hasOtherProperties;
    }

    @Override
    public void createUserWidgets(boolean isLoggedIn) {
        addCardToDeckContainer.clear();
        userLists.clear();
        // create AddCartToDeckWidget
        if (isLoggedIn) {
            addCardToDeckContainer.add(addCardToDeckWidget);
        }
        // create UserListWidget 'Exchange' buttons
        userLists.add(new UserListWidget(
                "Owned by",
                new String[]{"alessio.arcara@hotmail.com", "davide.fermi@gmail.com", "alessia.crimaldi@virgilio.it", "alessio.arcara@hotmail.com", "davide.fermi@gmail.com", "alessia.crimaldi@virgilio.it"},
                isLoggedIn
        ));
        userLists.add(new UserListWidget(
                "Wished by",
                new String[]{"matteo.sacco04@studio.unibo.it"},
                isLoggedIn));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClickAddToDeck() {
        dialog.center();
        dialog.setModal(true);
        dialog.show();
        if (addCardToDeckWidget.getDeckName().equals("Owned")) {
            dialog.setText("Do you own this card?");
        } else if(addCardToDeckWidget.getDeckName().equals("Wished")) {
            dialog.setText("Do you wish this card?");
        } else {
            dialog.setText("YOU MUST SELECT A DECK!");
        }
    }

    @Override
    public void hideModal() {
        dialog.hide();
    }

    @Override
    public void onClickModalNo() {
        hideModal();
    }

    @Override
    public void onClickModalYes(String status, String description) {
        presenter.addCardToDeck(addCardToDeckWidget.getDeckName(), status, description);
    }

    @Override
    public void displayErrorAlert(String errorMessage) {
        Window.alert(errorMessage);
    }

    @Override
    public void displaySuccessAlert() {
        Window.alert("Success! Card added to " + addCardToDeckWidget.getDeckName() + " deck");
    }

    interface CardsViewImplUIBinder extends UiBinder<Widget, CardViewImpl> {
    }
}
