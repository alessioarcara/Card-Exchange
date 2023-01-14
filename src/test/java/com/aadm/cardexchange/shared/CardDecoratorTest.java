package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.CardImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardDecoratorTest implements CardTestConstants {
    private CardDecorator card;
    private CardDecorator differentCard;

    @BeforeEach
    public void initialize() {
        card = new CardDecorator(new CardImpl(cardName, cardDesc, cardType));
        differentCard = new CardDecorator(new CardImpl("test", "test", "test"));
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, card.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, card.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, card.getType());
    }

    @Test
    public void testEqualsForItself() {
        Assertions.assertTrue(card.equals(card));
    }

    @Test
    public void testEqualsForDifferentFieldsObject() {
        Assertions.assertFalse(card.equals(new Object()));
    }

    @Test
    public void testEqualsForDifferentFieldsCard() {
        Assertions.assertFalse(card.equals(differentCard));
    }

    @Test
    public void testEqualsForEqualFieldsCard() {
        Assertions.assertTrue(card.equals(new CardDecorator(new CardImpl(cardName, cardDesc, cardType))));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(card.hashCode() == differentCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(card.hashCode() == new CardDecorator(new CardImpl(cardName, cardDesc, cardType)).hashCode());
    }
}
