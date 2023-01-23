package com.aadm.cardexchange.shared.models;

import java.io.Serializable;

public interface Card extends Serializable {
    String getName();

    String getDescription();

    String getType();
}
