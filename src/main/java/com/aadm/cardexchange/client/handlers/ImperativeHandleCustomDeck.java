package com.aadm.cardexchange.client.handlers;

import com.aadm.cardexchange.shared.models.PhysicalCardWithName;

import java.util.List;
import java.util.function.Consumer;

public interface ImperativeHandleCustomDeck {
    void onClickRemoveCustomDeck(String deck, Consumer<Boolean> isRemoved);

    void onClickAddPhysicalCardsToCustomDeck(Consumer<List<PhysicalCardWithName>> getSelectedPhysicalCards);
}
