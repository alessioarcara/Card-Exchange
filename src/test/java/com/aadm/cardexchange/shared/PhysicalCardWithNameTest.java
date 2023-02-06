package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.server.MockCardData;
import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhysicalCardWithNameTest {
    private Card card;
    private PhysicalCard physicalCard;
    private PhysicalCardWithName physicalCardWithName;

    @BeforeEach
    public void initialize() {
        card = MockCardData.createPokemonDummyCard();
        String sampleDesc = "this is a valid test description";
        physicalCard = new PhysicalCard(Game.POKEMON, card.getId(), Status.Good, sampleDesc);
        physicalCardWithName = new PhysicalCardWithName(physicalCard, card.getName());
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(card.getName(), physicalCardWithName.getName());
    }

    @Test
    public void testEqualsForPhysicalCard() {
        Assertions.assertTrue(physicalCardWithName.equals(physicalCard));
    }

    @Test
    public void testEqualsForSameFieldsDifferentPhysicalCardWithName() {
        Assertions.assertTrue(physicalCardWithName.equals(new PhysicalCardWithName(physicalCard, card.getName())));
    }
}
