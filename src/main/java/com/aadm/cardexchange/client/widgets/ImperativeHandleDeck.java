package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCardDecorator;

import java.util.List;
import java.util.function.Consumer;

public interface ImperativeHandleDeck {
    void onShowDeck(String deckName, Consumer<List<PhysicalCardDecorator>> setDeckData);
}
