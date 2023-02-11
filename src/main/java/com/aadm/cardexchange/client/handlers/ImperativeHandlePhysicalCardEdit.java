package com.aadm.cardexchange.client.handlers;

import com.aadm.cardexchange.shared.models.PhysicalCard;

public interface ImperativeHandlePhysicalCardEdit {
    void onConfirmCardEdit(String deckName, PhysicalCard editedPcard);
}
