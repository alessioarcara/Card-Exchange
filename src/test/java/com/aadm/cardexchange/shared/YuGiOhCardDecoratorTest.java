package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.YuGiOhCardDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YuGiOhCardDecoratorTest implements CardTestConstants {
    private YuGiOhCardDecorator yuGiOhCard;
    private YuGiOhCardDecorator differentYuGiOhCard;

    @BeforeEach
    public void initialize() {
        yuGiOhCard = new YuGiOhCardDecorator(new CardImpl(cardName, cardDesc, cardType), yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl);
        differentYuGiOhCard = new YuGiOhCardDecorator(new CardImpl("test", "test", "test"), "test", "", "");
    }

    @Test
    public void testGetRace() {
        Assertions.assertEquals(yuGiOhRace, yuGiOhCard.getSpecialAttribute());
    }

    @Test
    public void testGetImageUrl() {
        Assertions.assertEquals(cardImageUrl, yuGiOhCard.getImageUrl());
    }

    @Test
    public void testGetSmallImageUrl() {
        Assertions.assertEquals(yuGiOhSmallImageUrl, yuGiOhCard.getSmallImageUrl());
    }

    @Test
    public void testEqualsForItself() {
        Assertions.assertTrue(yuGiOhCard.equals(yuGiOhCard));
    }

    @Test
    public void testEqualsForDifferentFieldsObject() {
        Assertions.assertFalse(yuGiOhCard.equals(new Object()));
    }

    @Test
    public void testEqualsForDifferentFieldsCard() {
        Assertions.assertFalse(yuGiOhCard.equals(differentYuGiOhCard));
    }

    @Test
    public void testEqualsForEqualFieldsCard() {
        Assertions.assertTrue(yuGiOhCard.equals(new YuGiOhCardDecorator(new CardImpl(cardName, cardDesc, cardType), yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl)));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(yuGiOhCard.hashCode() == differentYuGiOhCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(yuGiOhCard.hashCode() == new YuGiOhCardDecorator(new CardImpl(cardName, cardDesc, cardType), yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl).hashCode());
    }
}
