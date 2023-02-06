package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.YuGiOhCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YuGiOhCardTest implements CardTestConstants {
    private YuGiOhCard yuGiOhCard;
    private YuGiOhCard differentYuGiOhCard;

    @BeforeEach
    public void initialize() {
        yuGiOhCard = new YuGiOhCard(cardName, cardDesc, cardType, yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl);
        differentYuGiOhCard = new YuGiOhCard("test", "test", "test", "test", "", "");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, yuGiOhCard.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, yuGiOhCard.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, yuGiOhCard.getType());
    }

    @Test
    public void testGetVariants() {
        Assertions.assertAll(() -> {
            Assertions.assertFalse(yuGiOhCard.getVariants().contains("test"));
            Assertions.assertFalse(yuGiOhCard.getVariants().contains(null));
        });
    }

    @Test
    public void testGetRace() {
        Assertions.assertEquals(yuGiOhRace, yuGiOhCard.getRace());
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
        Assertions.assertTrue(yuGiOhCard.equals(new YuGiOhCard(cardName, cardDesc, cardType, yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl)));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(yuGiOhCard.hashCode() == differentYuGiOhCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(yuGiOhCard.hashCode() == new YuGiOhCard(cardName, cardDesc, cardType, yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl).hashCode());
    }
}
