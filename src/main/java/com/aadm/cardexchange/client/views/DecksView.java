package com.aadm.cardexchange.client.views;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.aadm.cardexchange.shared.payloads.ModifiedDeckPayload;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DecksView extends IsWidget {

    void setData(List<String> data);

    void resetData();

    void displayAlert(String message);

    void replaceData(List<ModifiedDeckPayload> data);

    void displayAddedCustomDeck(String deckName);

    void setPresenter(Presenter presenter);

    interface Presenter {
        void fetchUserDeck(String deckName, BiConsumer<List<PhysicalCardWithName>, String> setDeckData);

        void updatePhysicalCard(String deckName, PhysicalCard editedPcard);

        void removePhysicalCardFromDeck(String deckName, PhysicalCard pCard);

        void createCustomDeck(String deckName);

        void deleteCustomDeck(String deckName, Consumer<Boolean> isRemoved);

        void addPhysicalCardsToCustomDeck(String deckName, List<PhysicalCard> pCards, Consumer<List<PhysicalCardWithName>> updateCustomDeck);
    }
}
