package com.aadm.cardexchange.server;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson;
    private final Class<T> type;

    public GsonSerializer(Gson gson, Class<T> type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public void serialize(DataOutput2 out, @NotNull T value) throws IOException {
        out.writeUTF(gson.toJson(value));
    }

    @Override
    public T deserialize(DataInput2 in, int available) throws IOException {
        return gson.fromJson(in.readUTF(), type);
    }
}