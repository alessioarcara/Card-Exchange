package com.aadm.cardexchange.client.handlers;

import java.util.function.Consumer;

public interface ImperativeHandleDeckRemove {
    void onClickRemoveCustomDeck(String deck, Consumer<Boolean> isRemoved);
}
