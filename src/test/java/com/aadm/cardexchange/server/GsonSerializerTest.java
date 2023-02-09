package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.gsonserializer.GsonSerializer;
import com.aadm.cardexchange.shared.CardTestConstants;
import com.aadm.cardexchange.shared.models.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GsonSerializerTest implements CardTestConstants {

    private static Stream<Arguments> provideSubClasses() {
        return Stream.of(
                Arguments.of(new MagicCard(cardName, cardDesc, cardType, genericArtist, genericRarity,
                        "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint")),
                Arguments.of(new PokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl, genericRarity,
                        "firstEdition", "holo", "normal", "reverse", "wPromo")),
                Arguments.of(new YuGiOhCard(cardName, cardDesc, cardType, yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl))
        );
    }

    @ParameterizedTest
    @MethodSource("provideSubClasses")
    public void testSerializerForCards(Card card) throws IOException {
        Gson gson = new Gson();
        GsonSerializer<Card> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        serializer.serialize(out, card);

        byte[] data = out.copyBytes();
        GsonSerializer<Card> deserializer = new GsonSerializer<>(gson);
        Card deserializedCard = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertEquals(card, deserializedCard);
    }

    @Test
    public void testSerializerForUnknownClass() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<Card> serializer = new GsonSerializer<>(gson);

        byte[] data;
        try (DataOutput2 out = new DataOutput2()) {
            out.writeUTF("{\"classType\":\"com.aadm.NonExistentClass\"}");
            data = out.copyBytes();
        }
        Assertions.assertThrows(JsonParseException.class, () -> serializer.deserialize(new DataInput2.ByteArray(data), 0));
    }

    @Test
    public void testSerializerForMapOfStrings() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        GsonSerializer<Map<String, String>> serializer = new GsonSerializer<>(gson, type);
        DataOutput2 out = new DataOutput2();

        Map<String, String> stringMap = new HashMap<>() {{
            put("test1", "test1");
            put("test2", "test2");
            put("test3", "test3");
        }};
        serializer.serialize(out, stringMap);

        byte[] data = out.copyBytes();
        GsonSerializer<Map<String, String>> deserializer = new GsonSerializer<>(gson, type);
        Map<String, String> deserializedDeckMap = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertAll(() -> {
            Assertions.assertEquals("test1", deserializedDeckMap.get("test1"));
            Assertions.assertEquals("test2", deserializedDeckMap.get("test2"));
            Assertions.assertEquals("test3", deserializedDeckMap.get("test3"));
        });
    }

    @Test
    public void testSerializerForDeck() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<Deck> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        Deck ownedDeck = new Deck("Owned");
        PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 111, Status.Excellent, "This is valid description");
        PhysicalCard mockPCard2 = new PhysicalCard(Game.POKEMON, 222, Status.VeryDamaged, "This is valid description");
        ownedDeck.addPhysicalCard(mockPCard1);
        ownedDeck.addPhysicalCard(mockPCard2);
        serializer.serialize(out, ownedDeck);

        byte[] data = out.copyBytes();
        GsonSerializer<Deck> deserializer = new GsonSerializer<>(gson);
        Deck deserializedDeck = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(ownedDeck, deserializedDeck);
            Assertions.assertEquals(2, ownedDeck.getPhysicalCards().size());
            Assertions.assertTrue(ownedDeck.getPhysicalCards().contains(mockPCard1));
            Assertions.assertTrue(ownedDeck.getPhysicalCards().contains(mockPCard2));
        });
    }

    @Test
    public void testSerializerForMapOfMapOfDecks() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, Deck>>>() {
        }.getType();

        GsonSerializer<Map<String, Map<String, Deck>>> serializer = new GsonSerializer<>(gson, type);
        DataOutput2 out = new DataOutput2();

        Map<String, Map<String, Deck>> deckMap = new HashMap<>();
        Map<String, Deck> mockDecks = new HashMap<>() {{
            put("Owned", new Deck("Owned"));
            put("Wished", new Deck("Wished"));
        }};
        PhysicalCard mockPCard1 = new PhysicalCard(Game.MAGIC, 111, Status.Excellent, "This is valid description");
        PhysicalCard mockPCard2 = new PhysicalCard(Game.POKEMON, 222, Status.VeryDamaged, "This is valid description");
        mockDecks.get("Owned").addPhysicalCard(mockPCard1);
        mockDecks.get("Wished").addPhysicalCard(mockPCard2);
        deckMap.put("test@test.it", mockDecks);
        serializer.serialize(out, deckMap);

        byte[] data = out.copyBytes();
        GsonSerializer<Map<String, Map<String, Deck>>> deserializer = new GsonSerializer<>(gson, type);
        Map<String, Map<String, Deck>> deserializedDeckMap = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertAll(() -> {
            Assertions.assertNotNull(deserializedDeckMap.get("test@test.it"));
            Assertions.assertTrue(deserializedDeckMap.get("test@test.it").get("Owned").containsPhysicalCard(mockPCard1));
            Assertions.assertTrue(deserializedDeckMap.get("test@test.it").get("Wished").containsPhysicalCard(mockPCard2));
        });
    }
}
