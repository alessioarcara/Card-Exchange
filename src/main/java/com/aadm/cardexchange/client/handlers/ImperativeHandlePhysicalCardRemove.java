package com.aadm.cardexchange.client.handlers;

import com.aadm.cardexchange.shared.models.PhysicalCard;

public interface ImperativeHandlePhysicalCardRemove {
    void onClickDeleteButton(PhysicalCard pCard, Consumer<List<Deck>> isRemoved);

}
