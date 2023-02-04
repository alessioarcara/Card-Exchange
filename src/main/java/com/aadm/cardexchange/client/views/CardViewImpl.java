package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.client.places.NewExchangePlace;
import com.aadm.cardexchange.client.utils.DefaultImagePathLookupTable;
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
import java.util.List;

public class CardViewImpl extends Composite implements CardView, ImperativeHandleAddCardToDeck, ImperativeHandleAddCardToDeckModal, ImperativeHandleUserList {
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
    HTMLPanel addCardToDeckContainer;
    @UiField
    HTMLPanel userLists;
    Presenter presenter;
    AddCardToDeckWidget addCardToDeckWidget = new AddCardToDeckWidget(this);
    DialogBox dialog = new AddCardToDeckModalWidget(this);
    UserListWidget ownedByUserList;

    public CardViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setData(Card data) {
        Game game;
        String imageUrl = "";
        String artist = "";
        String rarity = "";
        String race = "";
        StringBuilder otherProperties = new StringBuilder();

        setPropertiesUnvisible();

        if (data instanceof MagicCard) {
            game = Game.MAGIC;
            artist = ((MagicCard) data).getArtist();
            rarity = ((MagicCard) data).getRarity();
        } else if (data instanceof PokemonCard) {
            game = Game.POKEMON;
            imageUrl = ((PokemonCard) data).getImageUrl();
            artist = ((PokemonCard) data).getArtist();
            rarity = ((PokemonCard) data).getRarity();
        } else if (data instanceof YuGiOhCard) {
            game = Game.YUGIOH;
            imageUrl = ((YuGiOhCard) data).getImageUrl();
            race = ((YuGiOhCard) data).getRace();
        } else {
            game = null;
        }

        for (String variant : data.getVariants()) {
            otherProperties.append("<div>").append(variant).append("</div>");
        }

        cardImage.addErrorHandler((error) -> cardImage.setUrl(GWT.getHostPageBaseURL() + DefaultImagePathLookupTable.getPath(game)));
        cardGame.setInnerHTML(game != null ? game.name() : "");
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
        optionOtherProperties.setInnerHTML(String.valueOf(otherProperties));
    }

    private void setPropertiesUnvisible() {
        optionArtist.setAttribute("style", "display: none");
        optionRarity.setAttribute("style", "display: none");
        optionRace.setAttribute("style", "display: none");
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
        ownedByUserList = new UserListWidget("Owned by", isLoggedIn, this);
        userLists.add(ownedByUserList);
        userLists.add(new UserListWidget("Wished by", isLoggedIn, this));
    }

    @Override
    public void setOwnedByUserList(List<PhysicalCardWithEmail> pCards) {
        ownedByUserList.setTable(pCards);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClickAddToDeck() {
        dialog.center();
        dialog.setModal(true);
        if (addCardToDeckWidget.getDeckName().equals("Owned")) {
            dialog.setText("Do you own this card?");
        } else if (addCardToDeckWidget.getDeckName().equals("Wished")) {
            dialog.setText("Do you wish this card?");
        } else {
            dialog.setText("YOU MUST SELECT A DECK!");
        }
        dialog.show();
    }

    @Override
    public void hideModal() {
        dialog.hide();
    }

    @Override
    public String getDeckSelected() {
        return addCardToDeckWidget.getDeckName();
    }

    @Override
    public void onClickModalYes(String status, String description) {
        presenter.addCardToDeck(addCardToDeckWidget.getDeckName(), status, description);
    }

    @Override
    public void displayAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void onClickExchange(String receiverUserEmail, String selectedCardId) {
        presenter.goTo(new NewExchangePlace(selectedCardId, receiverUserEmail));
    }

    interface CardsViewImplUIBinder extends UiBinder<Widget, CardViewImpl> {
    }
}
