package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.MagicCardDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MagicCardDecoratorTest implements CardTestConstants {

    private MagicCardDecorator magicCard;
    private MagicCardDecorator differentMagicCard;

    @BeforeEach
    public void initialize() {
        magicCard = new MagicCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist,
                genericRarity, true, true, true, true, true);
        differentMagicCard = new MagicCardDecorator(new CardImpl("test", "test", "test"), "test",
                "test", false, false, false, false, false);
    }

    @Test
    public void testGetArtist() {
        Assertions.assertEquals(genericArtist, magicCard.getArtist());
    }

    @Test
    public void testGetRarity() {
        Assertions.assertEquals(genericRarity, magicCard.getRarity());
    }

    @Test
    public void testGetHasFoil() {
        Assertions.assertTrue(magicCard.getHasFoil());
    }

    @Test
    public void testGetIsAlternative() {
        Assertions.assertTrue(magicCard.getIsAlternative());
    }

    @Test
    public void testGetIsFullArt() {
        Assertions.assertTrue(magicCard.getIsFullArt());
    }

    @Test
    public void testGetIsPromo() {
        Assertions.assertTrue(magicCard.getIsPromo());
    }

    @Test
    public void testGetIsReprint() {
        Assertions.assertTrue(magicCard.getIsReprint());
    }

    @Test
    public void testEqualsForItself() {
        Assertions.assertTrue(magicCard.equals(magicCard));
    }

    @Test
    public void testEqualsForDifferentFieldsObject() {
        Assertions.assertFalse(magicCard.equals(new Object()));
    }

    @Test
    public void testEqualsForDifferentFieldsCard() {
        Assertions.assertFalse(magicCard.equals(differentMagicCard));
    }

    @Test
    public void testEqualsForEqualFieldsCard() {
        Assertions.assertTrue(magicCard.equals(new MagicCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist,
                genericRarity, true, true, true, true, true)));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(magicCard.hashCode() == differentMagicCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(magicCard.hashCode() == new MagicCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist,
                genericRarity, true, true, true, true, true).hashCode());
    }
}
