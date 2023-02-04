package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.aadm.cardexchange.shared.CardTestConstants.*;

public class PhysicalCardDecoratorTest {
    private CardDecorator card;
    private PhysicalCardImpl physicalCard;
    private PhysicalCardDecorator physicalCardDecorator;

    @BeforeEach
    public void initialize() {
        card = new CardDecorator(new CardImpl(cardName, cardDesc, cardType));
        String sampleDesc = "this is a valid test description";
        physicalCard = new PhysicalCardImpl(Game.YUGIOH, card.getId(), Status.Good, sampleDesc);
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
