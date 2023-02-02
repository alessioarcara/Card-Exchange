package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class DeckTest {

    private Deck deck;
    private Deck defaultDeck;
    private PhysicalCardImpl pCard;
    private PhysicalCardImpl pCard2;

    @BeforeEach
    public void initialize() {
        deck = new Deck("Deck_name");
        defaultDeck = new Deck("Deck_default", true);
        CardDecorator card = new CardDecorator(new CardImpl("DUMMY_NAME", "DUMMY_TYPE", "DUMMY_DESCRIPTION"));
        pCard = new PhysicalCardImpl(Game.MAGIC, card.getId(), Status.Excellent, "test description card");
        pCard2 = new PhysicalCardImpl(Game.MAGIC, card.getId(), Status.Good, "test card 2");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("Deck_name", deck.getName());
        Assertions.assertEquals("Deck_default", defaultDeck.getName());
    }

    @Test
    public void testSetName() {
        deck.setName("deletable_deck");
        Assertions.assertEquals("deletable_deck", deck.getName());

        defaultDeck.setName("new_name");
        Assertions.assertEquals("Deck_default", defaultDeck.getName());
    }

    @Test
    public void testIsDefault() {
        Assertions.assertFalse(deck.isDefault());
        Assertions.assertTrue(defaultDeck.isDefault());
    }

    @Test
    public void testAddPhysicalCard() {
        Assertions.assertTrue(deck.addPhysicalCard(pCard));
        Assertions.assertFalse(deck.addPhysicalCard(pCard));
    }

    @Test
    public void testContainsPhysicalCard() {
        deck.addPhysicalCard(pCard);
        Assertions.assertTrue(deck.containsPhysicalCard(pCard));
        Assertions.assertFalse(deck.containsPhysicalCard(pCard2));
    }

    @Test
    public void testGetPhysicalCards() {
        deck.addPhysicalCard(pCard);
        deck.addPhysicalCard(pCard2);
        Set<PhysicalCardImpl> cards = deck.getPhysicalCards();

        Assertions.assertEquals(2, cards.size());
        Assertions.assertTrue(cards.contains(pCard));
        Assertions.assertTrue(cards.contains(pCard2));
    }

    @Test
    public void testRemovePhysicalCard() {
        deck.addPhysicalCard(pCard);
        deck.addPhysicalCard(pCard2);
        Assertions.assertTrue(deck.removePhysicalCard(pCard));
        Assertions.assertEquals(1, deck.getPhysicalCards().size());
        Assertions.assertFalse(deck.removePhysicalCard(pCard));
    }
}
