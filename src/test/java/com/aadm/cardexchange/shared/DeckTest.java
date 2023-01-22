package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.aadm.cardexchange.shared.UserTestConstants.password;
import static com.aadm.cardexchange.shared.UserTestConstants.username;

public class DeckTest {

    private Deck deck;
    private Deck defaultDeck;
    private PhysicalCard pCard;
    private PhysicalCard pCard2;

    @BeforeEach
    public void initialize() {
        User user = new User(username, password);
        deck = new Deck(user.getUsername(), "Deck_name");
        defaultDeck = new Deck(user.getUsername(), "Deck_default", true);
        CardDecorator card = new CardDecorator(new CardImpl("DUMMY_NAME", "DUMMY_TYPE", "DUMMY_DESCRIPTION"));
        pCard = new PhysicalCard(card.getId(), "1 (Very Good)", "test description card");
        pCard2 = new PhysicalCard(card.getId(), "2 (Good)", "test card 2");
    }

    @Test
    public void testGetUserEmail() {
        Assertions.assertEquals(username, deck.getUserEmail());
        Assertions.assertEquals(username, defaultDeck.getUserEmail());
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
    }

    @Test
    public void testIsDefault() {
        Assertions.assertFalse(deck.isDefault());
        Assertions.assertTrue(defaultDeck.isDefault());
    }

    @Test
    public void testAddPhysicalCard() {
        Assertions.assertTrue(deck.addPhysicalCard(pCard.getId()));
        Assertions.assertFalse(deck.addPhysicalCard(pCard.getId()));
    }

    @Test
    public void testContainsPhysicalCard() {
        deck.addPhysicalCard(pCard.getId());
        Assertions.assertTrue(deck.containsPhysicalCard(pCard.getId()));
        Assertions.assertFalse(deck.containsPhysicalCard(pCard2.getId()));
    }

    @Test
    public void testGetPhysicalCards() {
        deck.addPhysicalCard(pCard.getId());
        deck.addPhysicalCard(pCard2.getId());
        ArrayList<Integer> cards = deck.getPhysicalCards();

        Assertions.assertEquals(2, cards.size());
        Assertions.assertEquals(pCard.getId(), cards.get(0));
        Assertions.assertEquals(pCard2.getId(), cards.get(1));
    }

    @Test
    public void testRemovePhysicalCard() {
        deck.addPhysicalCard(pCard.getId());
        deck.addPhysicalCard(pCard2.getId());
        Assertions.assertTrue(deck.removePhysicalCard(pCard.getId()));
        Assertions.assertEquals(1, deck.getPhysicalCards().size());
        Assertions.assertFalse(deck.removePhysicalCard(pCard.getId()));
    }
}
