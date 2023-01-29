package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.CardTestConstants;
import com.aadm.cardexchange.shared.models.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GsonSerializerTest implements CardTestConstants {

    private static Stream<Arguments> provideSubClasses() {
        return Stream.of(
                Arguments.of(new CardDecorator(new CardImpl(cardName, cardDesc, cardType))),
                Arguments.of(new MagicCardDecorator(new CardImpl(cardName, cardDesc, cardType),
                        genericArtist, genericRarity, true, true, true, true, true)),
                Arguments.of(new PokemonCardDecorator(new CardImpl(cardName, cardDesc, cardType),
                        genericArtist, cardImageUrl, genericRarity, true, true, true, true, true)),
                Arguments.of(new YuGiOhCardDecorator(new CardImpl(cardName, cardDesc, cardType),
                        yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl))
        );
    }

    @ParameterizedTest
    @MethodSource("provideSubClasses")
    public void testSerializerForCardDecorators(CardDecorator card) throws IOException {
        Gson gson = new Gson();
        GsonSerializer<CardDecorator> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        serializer.serialize(out, card);

        byte[] data = out.copyBytes();
        GsonSerializer<CardDecorator> deserializer = new GsonSerializer<>(gson);
        Object deserializedCard = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertEquals(card, deserializedCard);
    }

    @Test
    public void testSerializerForUnknownClass() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<CardDecorator> serializer = new GsonSerializer<>(gson);

        byte[] data;
        try (DataOutput2 out = new DataOutput2()) {
            out.writeUTF("{\"classType\":\"com.aadm.NonExistentClass\"}");
            data = out.copyBytes();
        }
        Assertions.assertThrows(IOException.class, () -> serializer.deserialize(new DataInput2.ByteArray(data), 0));
    }

    @Test
    public void testSerializerForMapOfMapOfStrings() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<Map<String, String>> serializer = new GsonSerializer<>(gson, new TypeToken<Map<String, String>>() {
        }.getType());
        DataOutput2 out = new DataOutput2();

        Map<String, String> stringMap = new HashMap<>() {{
            put("test1", "test1");
            put("test2", "test2");
            put("test3", "test3");
        }};
        serializer.serialize(out, stringMap);

        byte[] data = out.copyBytes();
        GsonSerializer<Map<String, String>> deserializer = new GsonSerializer<>(gson);
        Map<String, String> deserializedDeckMap = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertAll(() -> {
            Assertions.assertNotNull(deserializedDeckMap.get("test1"));
            Assertions.assertNotNull(deserializedDeckMap.get("test2"));
            Assertions.assertNotNull(deserializedDeckMap.get("test3"));
        });

    }

//    @Test
//    public void testSerializerForMapOfMapOfDecks() throws IOException {
//        String userEmail = "test@test.it";
//        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
//        GsonSerializer<Map<String, Map<String, Deck>>> serializer = new GsonSerializer<>(gson);
//        DataOutput2 out = new DataOutput2();
//
//        Map<String, Map<String, Deck>> deckMap = new HashMap<>();
//        Map<String, Deck> mockDecks = new HashMap<>() {{
//            put("Owned", new Deck(userEmail, "Owned"));
//            put("Wished", new Deck(userEmail, "Wished"));
//        }};
//        deckMap.put(userEmail, mockDecks);
//        serializer.serialize(out, deckMap);
//
//        byte[] data = out.copyBytes();
//        GsonSerializer<Map<String, Map<String, Deck>>> deserializer = new GsonSerializer<>(gson);
//        Map<String, Map<String, Deck>> deserializedDeckMap = deserializer.deserialize(new DataInput2.ByteArray(data), 0);
//
//        System.out.println(deserializedDeckMap.get(userEmail));
//        Assertions.assertAll(() -> {
//            Assertions.assertNotNull(deserializedDeckMap.get("test@test.it"));
//        });
//    }
}
