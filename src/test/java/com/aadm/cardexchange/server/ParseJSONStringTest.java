package com.aadm.cardexchange.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseJSONStringTest {
    @Test
    public void testYuGiOhJSONParse() {
        ListenerImpl listener = new ListenerImpl();

        String yuGiOhCard = "[" +
                    "{" +
                        "\"name\": \"Steel Ogre Grotto #2\"," +
                        "\"type\": \"Normal Monster\"," +
                        "\"desc\": \"A mechanized iron doll with tremendous strength.\"," +
                        "\"race\": \"Machine\"," +
                        "\"image_url\": \"https://images.ygoprodeck.com/images/cards/90908427.jpg\"," +
                        "\"small_image_url\": \"https://images.ygoprodeck.com/images/cards_small/90908427.jpg\"" +
                    "}," +
                    "{" +
                        "\"name\": \"Clone Token\"," +
                        "\"type\": \"Token\"," +
                        "\"desc\": \"Special Summoned with the effect of \"Cloning\". If the monster is destroyed and sent to the Graveyard, destroy this Token.\"," +
                        "\"race\": \"Warrior\"," +
                        "\"image_url\": \"https://images.ygoprodeck.com/images/cards/86871615.jpg\"," +
                        "\"small_image_url\": \"https://images.ygoprodeck.com/images/cards_small/86871615.jpg\"" +
                    "}" +
                "]";

        String[] parsedCards = listener.parseJSONString(yuGiOhCard);

        Assertions.assertEquals(parsedCards[0], "{" +
                        "\"name\": \"Steel Ogre Grotto #2\"," +
                        "\"type\": \"Normal Monster\"," +
                        "\"desc\": \"A mechanized iron doll with tremendous strength.\"," +
                        "\"race\": \"Machine\"," +
                        "\"image_url\": \"https://images.ygoprodeck.com/images/cards/90908427.jpg\"," +
                        "\"small_image_url\": \"https://images.ygoprodeck.com/images/cards_small/90908427.jpg\"" +
                        "}"
        );

        Assertions.assertEquals(parsedCards[1], "{" +
                "\"name\": \"Clone Token\"," +
                "\"type\": \"Token\"," +
                "\"desc\": \"Special Summoned with the effect of \"Cloning\". If the monster is destroyed and sent to the Graveyard, destroy this Token.\"," +
                "\"race\": \"Warrior\"," +
                "\"image_url\": \"https://images.ygoprodeck.com/images/cards/86871615.jpg\"," +
                "\"small_image_url\": \"https://images.ygoprodeck.com/images/cards_small/86871615.jpg\"" +
                "}"
        );
    }
}
