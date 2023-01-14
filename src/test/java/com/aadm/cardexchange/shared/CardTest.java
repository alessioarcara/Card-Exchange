package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.CardImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardTest implements CardTestConstants {
    private Card card;

    @BeforeEach
    public void initialize() {
        card = new CardImpl(cardName, cardDesc, cardType);
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, card.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, card.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, card.getType());
    }
}