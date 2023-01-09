package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.CardService;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.MagicCardDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

public class CardServiceTest {
    CardService cardService;

    @BeforeEach
    public void initialize() {
        cardService = new CardServiceImpl();
    }

    @ParameterizedTest()
    @EnumSource(Game.class)
    public void testGetGameCardsForNotNull(Game game) {
        Assertions.assertNotNull(cardService.getGameCards(game));
    }

    @Test
    public void testGetGameCardsForMagic() {
        List<MagicCardDecorator> magicCards = cardService.getGameCards(Game.Magic);
        Assertions.assertEquals(MagicCardDecorator.class, magicCards.get(0).getClass());
    }
}
