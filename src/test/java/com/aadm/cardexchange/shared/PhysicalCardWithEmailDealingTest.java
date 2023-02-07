package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.server.MockCardData;
import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhysicalCardWithEmailDealingTest {
    private PhysicalCard physicalCard;
    private PhysicalCard physicalCardOwned;
    private PhysicalCardWithEmail physicalCardWithEmail;
    private PhysicalCardWithEmailDealing physicalCardWithEmailDealing;

    @BeforeEach
    public void initialize() {
        Card card = MockCardData.createPokemonDummyCard();
        String sampleDesc = "this is a valid test description";
        physicalCard = new PhysicalCard(Game.POKEMON, card.getId(), Status.Good, sampleDesc);
        physicalCardOwned = new PhysicalCard(Game.POKEMON, card.getId(), Status.Excellent, sampleDesc);
        physicalCardWithEmail = new PhysicalCardWithEmail(physicalCard, "test@test.it");
        physicalCardWithEmailDealing = new PhysicalCardWithEmailDealing(physicalCardWithEmail, physicalCardOwned.getId());
    }
    @Test
    public void testGetIdPhysicalCardOwned() {
        Assertions.assertEquals(physicalCardOwned.getId(), physicalCardWithEmailDealing.getIdPhysicalCarPawn());
    }
}
