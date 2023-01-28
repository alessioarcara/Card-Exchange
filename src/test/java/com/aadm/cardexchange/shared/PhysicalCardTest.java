package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.server.GsonSerializer;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;

public class PhysicalCardTest {

    private CardDecorator card;
    private PhysicalCard pCard;

    @BeforeEach
    public void initialize() {
        card = new CardDecorator(new CardImpl("DUMMY_NAME", "DUMMY_TYPE", "DUMMY_DESCRIPTION"));
        pCard = new PhysicalCard(card.getId(), Status.Excellent, "well handled card during almost 10 years");
    }

    @Test
    public void testGetCardId_getId() {
        PhysicalCard pCard2 = new PhysicalCard(card.getId(), Status.Good, "this is a valid description");
        Assertions.assertEquals(card.getId(), pCard.getCardId());
        Assertions.assertEquals(card.getId(), pCard2.getCardId());
        Assertions.assertNotEquals(pCard.getId(), pCard2.getId());
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
