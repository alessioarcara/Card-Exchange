package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.PokemonCardDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PokemonCardDecoratorTest implements CardTestConstants {
    private PokemonCardDecorator pokemonCard;

    @BeforeEach
    public void initialize() {
        pokemonCard = new PokemonCardDecorator(new CardImpl(cardName, cardDesc, cardType), genericArtist, cardImageUrl,
                genericRarity, true, true, true, true, true);
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
}
