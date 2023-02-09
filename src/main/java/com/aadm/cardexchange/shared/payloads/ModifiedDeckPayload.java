package com.aadm.cardexchange.shared.payloads;

import com.aadm.cardexchange.shared.models.PhysicalCardWithName;

import java.io.Serializable;
import java.util.List;

public class ModifiedDeckPayload implements Serializable {
    private static final long serialVersionUID = 778850352936154377L;
    String deckName;
    List<PhysicalCardWithName> physicalCardsWithName;

    public ModifiedDeckPayload(String deckName, List<PhysicalCardWithName> physicalCardsWithName) {
        this.deckName = deckName;
        this.physicalCardsWithName = physicalCardsWithName;
    }

    public ModifiedDeckPayload() {
    }

    public String getDeckName() {
        return deckName;
    }

    public List<PhysicalCardWithName> getPhysicalCardsWithName() {
        return physicalCardsWithName;
    }
}
