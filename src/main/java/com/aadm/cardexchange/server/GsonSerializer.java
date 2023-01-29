package com.aadm.cardexchange.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson;
    private Type type;


    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }

    public GsonSerializer(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public void serialize(DataOutput2 out, @NotNull T value) throws IOException {
        if (type != null) {
            String json = gson.toJson(value, type);
            System.out.println(json);
            out.writeUTF(json);
        } else {
            JsonObject jsonObj = gson.toJsonTree(value).getAsJsonObject();
            jsonObj.addProperty("classType", value.getClass().getName());
            out.writeUTF(jsonObj.toString());
        }
    }

    @Override
    public T deserialize(DataInput2 in, int available) throws IOException {
        JsonObject jsonObj = gson.fromJson(in.readUTF(), JsonObject.class);
        String classType = null;
        if (jsonObj.get("classType") == null) {
            return gson.fromJson(jsonObj, type);
        }
        classType = jsonObj.get("classType").getAsString();
        try {
            System.out.println(jsonObj);
            Class<?> clazz = Class.forName(classType);
            return gson.fromJson(jsonObj, (Type) clazz);
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to deserialize class of type " + classType);
        }
    }
}