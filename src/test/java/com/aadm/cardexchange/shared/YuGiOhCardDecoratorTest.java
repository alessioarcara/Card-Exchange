package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.YuGiOhCardDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YuGiOhCardDecoratorTest implements CardTestConstants {
    private YuGiOhCardDecorator yugiOhCard;

    @BeforeEach
    public void initialize() {
        yugiOhCard = new YuGiOhCardDecorator(new CardImpl(cardName, cardDesc, cardType), yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl);
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, yugiOhCard.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, yugiOhCard.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, yugiOhCard.getType());
    }

    @Test
    public void testGetRace() {
        Assertions.assertEquals(yuGiOhRace, yugiOhCard.getRace());
    }

    @Test
    public void testGetImageUrl() {
        Assertions.assertEquals(cardImageUrl, yugiOhCard.getImageUrl());
    }

    @Test
    public void testGetSmallImageUrl() {
        Assertions.assertEquals(yuGiOhSmallImageUrl, yugiOhCard.getSmallImageUrl());
    }
}
