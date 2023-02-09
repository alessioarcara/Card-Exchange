package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.server.DummyData;
import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhysicalCardWithEmailTest {
    private PhysicalCard physicalCard;
    private PhysicalCardWithEmail physicalCardWithEmail;

    @BeforeEach
    public void initialize() {
        Card card = DummyData.createPokemonDummyCard();
        String sampleDesc = "this is a valid test description";
        physicalCard = new PhysicalCard(Game.POKEMON, card.getId(), Status.Good, sampleDesc);
        physicalCardWithEmail = new PhysicalCardWithEmail(physicalCard, "test@test.it");
    }

    @Test
    public void testGetEmail() {
        Assertions.assertEquals("test@test.it", physicalCardWithEmail.getEmail());
    }

    @Test
    public void testEqualsForPhysicalCard() {
        Assertions.assertTrue(physicalCardWithEmail.equals(physicalCard));
    }

    @Test
    public void testEqualsForSameFieldsDifferentPhysicalCardWithName() {
        Assertions.assertTrue(physicalCardWithEmail.equals(new PhysicalCardWithEmail(physicalCard, "test@test.it")));
    }
}
