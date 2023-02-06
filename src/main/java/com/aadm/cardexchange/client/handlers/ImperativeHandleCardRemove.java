package com.aadm.cardexchange.client.handlers;

import com.aadm.cardexchange.shared.models.Deck;
import com.aadm.cardexchange.shared.models.PhysicalCard;

import java.util.List;
import java.util.function.Consumer;

public interface ImperativeHandleCardRemove {
    void onClickDeleteButton(PhysicalCard pCard, Consumer<List<Deck>> isRemoved);

}
