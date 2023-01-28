package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public interface PhysicalCard extends Serializable {
    String getId();

    int getCardId();

    Status getStatus();

    String getDescription();

    Game getGameType();
}
