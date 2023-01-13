package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.PokemonCardDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PokemonCardDecoratorTest implements CardTestConstants {
    private PokemonCardDecorator pokemonCard;
    private PokemonCardDecorator differentPokemonCard;

    @BeforeEach
    public void initialize() {
        pokemonCard = new PokemonCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist, cardImageUrl,
                genericRarity, true, true, true, true, true);
        differentPokemonCard = new PokemonCardDecorator(new CardImpl("test", "test", "test"), "test", "test",
                "test", false, false, false, false, false);
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
        Assertions.assertEquals(genericRarity, pokemonCard.getSpecialAttribute());
    }

    @Test
    public void testGetIsFirstEdition() {
        Assertions.assertTrue(pokemonCard.getIsFirstEdition());
    }

    @Test
    public void testGetIsHolo() {
        Assertions.assertTrue(pokemonCard.getIsHolo());
    }

    @Test
    public void testGetIsNormal() {
        Assertions.assertTrue(pokemonCard.getIsNormal());
    }

    @Test
    public void testGetIsReverse() {
        Assertions.assertTrue(pokemonCard.getIsReverse());
    }

    @Test
    public void testGetIsPromo() {
        Assertions.assertTrue(pokemonCard.getIsPromo());
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
        Assertions.assertTrue(pokemonCard.equals(new PokemonCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist, cardImageUrl,
                genericRarity, true, true, true, true, true)));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(pokemonCard.hashCode() == differentPokemonCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(pokemonCard.hashCode() == new PokemonCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist, cardImageUrl,
                genericRarity, true, true, true, true, true).hashCode());
    }
}
