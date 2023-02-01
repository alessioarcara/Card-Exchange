package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.places.NewExchangePlace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NewExchangePlaceTest {

    private String card;
    private String userEmail;

    NewExchangePlace place;

    @BeforeEach
    public void initialize() {
        card = "cardId";
        userEmail = "test@test.it";
        place = new NewExchangePlace(card, userEmail);
    }

    @Test
    public void testGetReceiverUserEmail() {
        Assertions.assertEquals(userEmail, place.getReceiverUserEmail());
    }

    @Test
    public void testGetSelectedCard() {
        Assertions.assertEquals(card, place.getSelectedCardId());
    }
}
