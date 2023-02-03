package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.widgets.*;
import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.Property;
import com.aadm.cardexchange.shared.models.PropertyType;
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
    DivElement variantsDiv;
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

    public void setData(Card data) {
        Game game;
        String imageUrl = "";
        StringBuilder categories = new StringBuilder();
        StringBuilder variants = new StringBuilder();

        for (Property prop : data.getProperties()) {
            if (prop.getType() == PropertyType.CATEGORICAL) {
                categories.append("<div>").append(prop.getValue()).append("</div>");
            }
        }

//        if (data instanceof MagicCard) {
//            game = Game.MAGIC;
//            artist = ((MagicCard) data).getArtist();
//            rarity = ((MagicCard) data).getRarity();
//        } else if (data instanceof PokemonCard) {
//            game = Game.POKEMON;
//            imageUrl = ((PokemonCard) data).getImageUrl();
//            artist = ((PokemonCard) data).getArtist();
//            rarity = ((PokemonCard) data).getRarity();
//        } else if (data instanceof YuGiOhCard) {
//            game = Game.YUGIOH;
//            imageUrl = ((YuGiOhCard) data).getImageUrl();
//            race = ((YuGiOhCard) data).getRace();
//        } else {
//            game = null;
//        }
//
//        for (String variant : data.getVariants()) {
//            variants.append("<div>").append(variant).append("</div>");
//        }
//
//        cardImage.addErrorHandler((error) -> cardImage.setUrl(GWT.getHostPageBaseURL() + DefaultImagePathLookupTable.getPath(game)));
//        cardGame.setInnerHTML(game != null ? game.name() : "");
//        cardName.setInnerHTML(data.getName());
//        cardImage.setUrl(imageUrl);
//        cardDescription.setInnerHTML(data.getDescription());
//        cardOptionType.setInnerHTML(data.getType());
//        cardOptionArtist.setInnerHTML(artist);
//        cardOptionRarity.setInnerHTML(rarity);
//        cardOptionRace.setInnerHTML(race);
//        if (!artist.isEmpty()) optionArtist.getStyle().clearDisplay();
//        if (!rarity.isEmpty()) optionRarity.getStyle().clearDisplay();
//        if (!race.isEmpty()) optionRace.getStyle().clearDisplay();
        variantsDiv.setInnerHTML(String.valueOf(variants));
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
        } else if (addCardToDeckWidget.getDeckName().equals("Wished")) {
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
