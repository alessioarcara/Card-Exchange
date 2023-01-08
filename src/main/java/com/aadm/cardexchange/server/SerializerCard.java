package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.Card;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.io.Serializable;

public class SerializerCard implements Serializer<Card>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void serialize(DataOutput2 out, Card value) throws IOException {
        out.writeUTF(value.getName());
        out.writeUTF(value.getDescription());
    }

    @Override
    public Card deserialize(DataInput2 in, int available) throws IOException {
        return new Card(in.readUTF(), in.readUTF());
    }
}