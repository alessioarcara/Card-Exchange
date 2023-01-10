package com.aadm.cardexchange.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MagicCardDecoratorTest implements CardTestConstants {

    private MagicCardDecorator magicCard;

    @BeforeEach
    public void initialize() {
        magicCard = new MagicCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist,
                genericRarity, true, true, true, true, true);
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
}
