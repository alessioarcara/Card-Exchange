package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.server.MockCardData;
import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhysicalCardDecoratorTest {
    private Card card;
    private PhysicalCardImpl physicalCard;
    private PhysicalCardDecorator physicalCardDecorator;

    @BeforeEach
    public void initialize() {
        card = MockCardData.createPokemonDummyCard();
        String sampleDesc = "this is a valid test description";
        physicalCard = new PhysicalCardImpl(Game.POKEMON, card.getId(), Status.Good, sampleDesc);
        physicalCardDecorator = new PhysicalCardDecorator(physicalCard, card.getName());
    }

    @Test
    public void testGetId() {
        Assertions.assertEquals(physicalCard.getId(), physicalCardDecorator.getId());
    }

    @Test
    public void testGetCardId() {
        Assertions.assertEquals(physicalCard.getCardId(), physicalCardDecorator.getCardId());
    }

    @Test
    public void testGetStatus() {
        Assertions.assertEquals(physicalCard.getStatus(), physicalCardDecorator.getStatus());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(physicalCard.getDescription(), physicalCardDecorator.getDescription());
    }

    @Test
    public void testGetGameType() {
        Assertions.assertEquals(physicalCard.getGameType(), physicalCardDecorator.getGameType());
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(card.getName(), physicalCardDecorator.getName());
    }

    @Test
    public void testEqualsForPhysicalCard() {
        Assertions.assertFalse(physicalCardDecorator.equals(physicalCard));
    }

    @Test
    public void testEqualsForDifferentPhysicalCardDecorator() {
        Assertions.assertFalse(physicalCardDecorator.equals(new PhysicalCardDecorator(physicalCard, card.getName())));
    }
}
