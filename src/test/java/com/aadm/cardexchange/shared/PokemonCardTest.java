package com.aadm.cardexchange.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PokemonCardTest implements CardTestConstants {
    private PokemonCard pokemonCard;
    private PokemonCard differentPokemonCard;

    @BeforeEach
    public void initialize() {
        pokemonCard = new PokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl,
                genericRarity, "firstEdition", "holo", "normal", "reverse", "wPromo");
        differentPokemonCard = new PokemonCard("test", "test", "test", "test", "test",
                "test");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, pokemonCard.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, pokemonCard.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, pokemonCard.getType());
    }

    @Test
    public void testGetVariants() {
        Assertions.assertAll(() -> {
            Assertions.assertTrue(pokemonCard.getVariants().contains("firstEdition"));
            Assertions.assertTrue(pokemonCard.getVariants().contains("holo"));
            Assertions.assertTrue(pokemonCard.getVariants().contains("normal"));
            Assertions.assertTrue(pokemonCard.getVariants().contains("reverse"));
            Assertions.assertTrue(pokemonCard.getVariants().contains("wPromo"));
            Assertions.assertFalse(pokemonCard.getVariants().contains("test"));
        });
    }

    @Test
    public void testGetArtist() {
        Assertions.assertEquals(genericArtist, pokemonCard.getArtist());
    }

    @Test
    public void testGetImageUrl() {
        Assertions.assertEquals(cardImageUrl, pokemonCard.getImageUrl());
    }

    @Test
    public void testGetRarity() {
        Assertions.assertEquals(genericRarity, pokemonCard.getRarity());
    }

    @Test
    public void testEqualsForItself() {
        Assertions.assertTrue(pokemonCard.equals(pokemonCard));
    }

    @Test
    public void testEqualsForDifferentFieldsObject() {
        Assertions.assertFalse(pokemonCard.equals(new Object()));
    }

    @Test
    public void testEqualsForDifferentFieldsCard() {
        Assertions.assertFalse(pokemonCard.equals(differentPokemonCard));
    }

    @Test
    public void testEqualsForEqualFieldsCard() {
        Assertions.assertTrue(pokemonCard.equals(new PokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl,
                genericRarity, "firstEdition", "holo", "normal", "reverse", "wPromo")));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(pokemonCard.hashCode() == differentPokemonCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(pokemonCard.hashCode() == new PokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl,
                genericRarity, "firstEdition", "holo", "normal", "reverse", "wPromo").hashCode());
    }
}
