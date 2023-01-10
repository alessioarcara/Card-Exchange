package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.CardImpl;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.io.Serializable;

public class SerializerCard implements Serializer<CardImpl>, Serializable {
    private static final long serialVersionUID = 2L;

    @Override
    public void serialize(DataOutput2 out, CardImpl value) throws IOException {
        out.writeUTF(value.getName());
        out.writeUTF(value.getDescription());
        out.writeUTF(value.getType());
    }

    @Override
    public CardImpl deserialize(DataInput2 in, int available) throws IOException {
        return new CardImpl(in.readUTF(), in.readUTF(), in.readUTF());
    }
}