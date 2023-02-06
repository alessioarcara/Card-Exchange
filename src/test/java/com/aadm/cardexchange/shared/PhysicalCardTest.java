package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.server.MockCardData;
import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;

public class PhysicalCardTest {

    private Card card;
    private PhysicalCard pCard;

    @BeforeEach
    public void initialize() {
        card = MockCardData.createPokemonDummyCard();
        pCard = new PhysicalCard(Game.POKEMON, card.getId(), Status.Excellent, "well handled card during almost 10 years");
    }

    @ParameterizedTest
    @EnumSource(Game.class)
    public void testGetId(Game game) {
        PhysicalCard pCard2 = new PhysicalCard(game, card.getId(), Status.Good, "this is a valid description");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(game.name().toLowerCase().charAt(0), pCard2.getId().charAt(0));
            Assertions.assertDoesNotThrow(() -> Integer.parseInt(pCard.getId().substring(1)));
            Assertions.assertNotEquals(pCard.getId(), pCard2.getId());
        });
    }

    @Test
    public void testGetCardId() {
        PhysicalCard pCard2 = new PhysicalCard(Game.MAGIC, card.getId(), Status.Good, "this is a valid description");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(card.getId(), pCard.getCardId());
            Assertions.assertEquals(card.getId(), pCard2.getCardId());
        });
    }

    @ParameterizedTest
    @EnumSource(Game.class)
    public void testGetGameType(Game game) {
        PhysicalCard pCard2 = new PhysicalCard(game, card.getId(), Status.VeryDamaged, "this is a valid description");
        Assertions.assertEquals(game, pCard2.getGameType());
    }

    @Test
    public void testGetStatus() {
        Assertions.assertEquals(Status.Excellent, pCard.getStatus());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals("well handled card during almost 10 years", pCard.getDescription());
    }

    @Test
    public void testEqualsAfterSerializeAndDeserialize() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<PhysicalCard> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        serializer.serialize(out, pCard);

        byte[] data = out.copyBytes();
        GsonSerializer<PhysicalCard> deserializer = new GsonSerializer<>(gson);
        PhysicalCard deserializedPCard = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertEquals(pCard, deserializedPCard);
    }
}
