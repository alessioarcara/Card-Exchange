package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.CardTestConstants;
import com.aadm.cardexchange.shared.models.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;
import java.util.stream.Stream;

public class CardSerializerTest implements CardTestConstants {

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

        DataOutput2 out = new DataOutput2();
        out.writeUTF("{\"classType\":\"com.aadm.NonExistentClass\"}");
        byte[] data = out.copyBytes();
        Assertions.assertThrows(IOException.class, () -> serializer.deserialize(new DataInput2.ByteArray(data), 0));
    }
}
