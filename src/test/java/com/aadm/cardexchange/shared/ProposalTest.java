package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PhysicalCardImpl;
import com.aadm.cardexchange.shared.models.Proposal;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProposalTest {

    private String senderEmail;
    private String receiverEmail;
    private List<PhysicalCardImpl> senderCards;
    private List<PhysicalCardImpl> receiversCards;
    private PhysicalCardImpl senderCard1;
    private PhysicalCardImpl senderCard2;
    private PhysicalCardImpl receiverCard1;
    private PhysicalCardImpl receiverCard2;

    Proposal prop;

    @BeforeEach
    public void initialize() {
        final String validDesc = "this is a valid description!!!";

        senderCard1 = new PhysicalCardImpl(Game.YUGIOH, 111, Status.Good, validDesc);
        senderCard2 = new PhysicalCardImpl(Game.POKEMON, 222, Status.Excellent, validDesc);
        receiverCard1 = new PhysicalCardImpl(Game.YUGIOH, 333, Status.Excellent, validDesc);
        receiverCard2 = new PhysicalCardImpl(Game.POKEMON, 444, Status.Fair, validDesc);

        senderCards = new ArrayList<>() {{
            add(senderCard1);
            add(senderCard2);
        }};

        receiversCards = new ArrayList<>() {{
            add(receiverCard1);
            add(receiverCard2);
        }};

        senderEmail = "sender@test.it";
        receiverEmail = "receiver@test.it";

        prop = new Proposal(senderEmail, receiverEmail, senderCards, receiversCards);
    }

    @Test
    public void testIfGetIdReturnsUniqueIds() {
        Proposal prop2 = new Proposal(senderEmail, receiverEmail, senderCards, receiversCards);
        Assertions.assertNotEquals(prop.getId(), prop2.getId());
    }

    @Test
    public void testGetSenderEmail() {
        Assertions.assertEquals(senderEmail, prop.getSenderUserEmail());
    }

    @Test
    public void testGetReceiverEmail() {
        Assertions.assertEquals(receiverEmail, prop.getReceiverUserEmail());
    }

    @Test
    public void testGetSenderPhysicalCards() {
        Assertions.assertEquals(senderCards, prop.getSenderPhysicalCards());
    }

    @Test
    public void testGetReceiverPhysicalCards() {
        Assertions.assertEquals(receiversCards, prop.getReceiverPhysicalCards());
    }

    @Test
    public void testEqualsAfterSerializeAndDeserialize() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<Proposal> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        serializer.serialize(out, prop);

        byte[] data = out.copyBytes();
        GsonSerializer<Proposal> deserializer = new GsonSerializer<>(gson);
        Proposal deserializedProposal = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertEquals(prop, deserializedProposal);
    }
}
