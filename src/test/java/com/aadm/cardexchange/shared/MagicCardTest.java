package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.MagicCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MagicCardTest implements CardTestConstants {

    private MagicCard magicCard;
    private MagicCard differentMagicCard;

    @BeforeEach
    public void initialize() {
        magicCard = new MagicCard(cardName, cardDesc, cardType, genericArtist,
                genericRarity, "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint");
        differentMagicCard = new MagicCard("test", "test", "test", "test",
                "test");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, magicCard.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, magicCard.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, magicCard.getType());
    }

    @Test
    public void testGetVariants() {
        Assertions.assertAll(() -> {
            Assertions.assertTrue(magicCard.getVariants().contains("hasFoil"));
            Assertions.assertTrue(magicCard.getVariants().contains("isAlternative"));
            Assertions.assertTrue(magicCard.getVariants().contains("isFullArt"));
            Assertions.assertTrue(magicCard.getVariants().contains("isPromo"));
            Assertions.assertTrue(magicCard.getVariants().contains("isReprint"));
            Assertions.assertFalse(magicCard.getVariants().contains("test"));
        });
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
        Assertions.assertTrue(magicCard.equals(new MagicCard(cardName, cardDesc, cardType, genericArtist,
                genericRarity, "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint")));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(magicCard.hashCode() == differentMagicCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(magicCard.hashCode() == new MagicCard(cardName, cardDesc, cardType, genericArtist,
                genericRarity, "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint").hashCode());
    }
}
