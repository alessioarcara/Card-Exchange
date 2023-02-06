package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCardWithName;

import java.util.List;
import java.util.function.BiConsumer;

public interface ImperativeHandleDeck {
    void onShowDeck(String deckName, BiConsumer<List<PhysicalCardWithName>, String> setDeckData);
}
