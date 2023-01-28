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
        physicalCard = new PhysicalCardImpl(Game.YuGiOh, card.getId(), Status.Good, sampleDesc);
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
        Assertions.assertAll(()-> {
            Assertions.assertEquals(Game.YuGiOh, physicalCardDecorator.getGameType());
            Assertions.assertEquals(physicalCard.getGameType(), physicalCardDecorator.getGameType());
        });
    }

    @Test
    public void testGetName() {
        Assertions.assertAll(()-> {
            Assertions.assertEquals(cardName, physicalCardDecorator.getName());
            Assertions.assertEquals(card.getName(), physicalCardDecorator.getName());
        });
    }

    @Test
    public void testEqualsForItself() {
        Assertions.assertTrue(physicalCardDecorator.equals(physicalCardDecorator));
    }

    @Test
    public void testEqualsForDifferentFieldsObject() {
        Assertions.assertFalse(physicalCardDecorator.equals(new Object()));
    }

    @Test
    public void testEqualsForDifferentFieldsCard() {
        PhysicalCardDecorator pCD2 = new PhysicalCardDecorator(physicalCard, "newName");
        Assertions.assertFalse(physicalCardDecorator.equals(pCD2));
    }

    @Test
    public void testEqualsForEqualFieldsCard() {
        PhysicalCardDecorator pCD2 = new PhysicalCardDecorator(physicalCard, card.getName());
        Assertions.assertFalse(physicalCardDecorator.equals(pCD2));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        PhysicalCardDecorator pCD2 = new PhysicalCardDecorator(physicalCard, "newName");
        Assertions.assertFalse(physicalCardDecorator.hashCode() == pCD2.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertFalse(physicalCardDecorator.hashCode() == new PhysicalCardDecorator(physicalCard, cardName).hashCode());
    }
}
