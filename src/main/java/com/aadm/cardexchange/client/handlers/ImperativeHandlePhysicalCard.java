package com.aadm.cardexchange.client.handlers;

import com.aadm.cardexchange.shared.models.PhysicalCard;

import java.util.function.Consumer;

public interface ImperativeHandlePhysicalCard {
    void onChangeSelection();

    void onClickDeleteButton(PhysicalCard pCard, Consumer<Boolean> isRemoved);
}
