package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.*;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;


public class ImportJSONTest {

    @Test
    public void testYuGiOhImportJSON() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader();

        YuGiOhCardDecorator[] yuGiOh = jsonReader.importYugiCards();

        Assertions.assertEquals(yuGiOh[0].getName(), "Steel Ogre Grotto #2");
        Assertions.assertEquals(yuGiOh[0].getType(), "Normal Monster");
        Assertions.assertEquals(yuGiOh[0].getDescription(), "A mechanized iron doll with tremendous strength.");
        Assertions.assertEquals(yuGiOh[0].getRace(), "Machine");
        Assertions.assertEquals(yuGiOh[0].getImageUrl(), "https://images.ygoprodeck.com/images/cards/90908427.jpg");
        Assertions.assertEquals(yuGiOh[0].getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/90908427.jpg");

        Assertions.assertEquals(yuGiOh[1].getName(), "Clone Token");
        Assertions.assertEquals(yuGiOh[1].getType(), "Token");
        Assertions.assertEquals(yuGiOh[1].getDescription(), "Special Summoned with the effect of \"Cloning\". If the monster is destroyed and sent to the Graveyard, destroy this Token.");
        Assertions.assertEquals(yuGiOh[1].getRace(), "Warrior");
        Assertions.assertEquals(yuGiOh[1].getImageUrl(), "https://images.ygoprodeck.com/images/cards/86871615.jpg");
        Assertions.assertEquals(yuGiOh[1].getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/86871615.jpg");
    }

    @Test
    public void testMagicImportJSON() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader();

        MagicCardDecorator[] magic = jsonReader.importMagicCards();

        Assertions.assertEquals(magic[0].getArtist(), "Pete Venters");
        Assertions.assertEquals(magic[0].getName(), "Ancestor's Chosen");
        Assertions.assertEquals(magic[0].getDescription(), "First strike (This creature deals combat damage before creatures without first strike.)\nWhen Ancestor's Chosen enters the battlefield, you gain 1 life for each card in your graveyard.");
        Assertions.assertEquals(magic[0].getType(), "Creature");
        Assertions.assertEquals(magic[0].getRarity(), "uncommon");
        Assertions.assertFalse(magic[0].getHasFoil());
        Assertions.assertFalse(magic[0].getIsAlternative());
        Assertions.assertFalse(magic[0].getIsFullArt());
        Assertions.assertFalse(magic[0].getIsPromo());
        Assertions.assertTrue(magic[0].getIsReprint());

        Assertions.assertEquals(magic[2].getArtist(), "Volkan Ba\u01f5a");
        Assertions.assertEquals(magic[2].getName(), "Angel of Mercy");
        Assertions.assertEquals(magic[2].getDescription(), "Flying\nWhen Angel of Mercy enters the battlefield, you gain 3 life.");
        Assertions.assertEquals(magic[2].getType(), "Creature");
        Assertions.assertEquals(magic[2].getRarity(), "uncommon");
        Assertions.assertFalse(magic[2].getHasFoil());
        Assertions.assertFalse(magic[2].getIsAlternative());
        Assertions.assertFalse(magic[2].getIsFullArt());
        Assertions.assertFalse(magic[2].getIsPromo());
        Assertions.assertTrue(magic[2].getIsReprint());
    }

    @Test
    public void testPokemonImportJSON() throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader();

        PokemonCardDecorator[] pokemon = jsonReader.importPokemonCards();

        Assertions.assertEquals(pokemon[0].getArtist(), "Ken Sugimori"); // illustrator
        Assertions.assertEquals(pokemon[0].getImageUrl(), "https://assets.tcgdex.net/en/gym/gym1/98");
        Assertions.assertEquals(pokemon[0].getName(), "Brock");
        Assertions.assertEquals(pokemon[0].getRarity(), "Rare");
        Assertions.assertFalse(pokemon[0].getIsFirstEdition());
        Assertions.assertTrue(pokemon[0].getIsHolo());
        Assertions.assertTrue(pokemon[0].getIsNormal());
        Assertions.assertTrue(pokemon[0].getIsReverse());
        Assertions.assertFalse(pokemon[0].getIsPromo());
        Assertions.assertEquals(pokemon[0].getType(), "");
        Assertions.assertEquals(pokemon[0].getDescription(), "");

        Assertions.assertEquals(pokemon[11].getArtist(), "5ban Graphics"); // illustrator
        Assertions.assertEquals(pokemon[11].getImageUrl(), "https://assets.tcgdex.net/en/xy/xy4/70");
        Assertions.assertEquals(pokemon[11].getName(), "Dedenne");
        Assertions.assertEquals(pokemon[11].getRarity(), "Common");
        Assertions.assertFalse(pokemon[11].getIsFirstEdition());
        Assertions.assertTrue(pokemon[11].getIsHolo());
        Assertions.assertTrue(pokemon[11].getIsNormal());
        Assertions.assertTrue(pokemon[11].getIsReverse());
        Assertions.assertFalse(pokemon[11].getIsPromo());
        Assertions.assertEquals(pokemon[11].getType(), "Fairy");
        Assertions.assertEquals(pokemon[11].getDescription(), "");
    }

    @Test
    public void testYuGiOhMalformedJSON() {
        JsonReader jsonReader = new JsonReader();

        String yuGiOhCard = "[" +
                    "{" +
                        "\"name\": \"Steel Ogre Grotto #2\"," +
                        "\"type\": \"Normal Monster\"," +
                        "\"desc\": \"A mechanized iron doll with tremendous strength.\"," +
                        "\"race\": \"Machine\"," +
                        "\"image_url\": \"https://images.ygoprodeck.com/images/cards/90908427.jpg\"," +
                        "\"small_image_url\": \"https://images.ygoprodeck.com/images/cards_small/90908427.jpg\"" +
                    "}" +
                    "{" +
                        "\"name\": \"Clone Token\"," +
                        "\"type\": \"Token\"," +
                        "\"desc\": \"Special Summoned with the effect of \\\"Cloning\\\". If the monster is destroyed and sent to the Graveyard, destroy this Token.\"," +
                        "\"race\": \"Warrior\"," +
                        "\"image_url\": \"https://images.ygoprodeck.com/images/cards/86871615.jpg\"," +
                        "\"small_image_url\": \"https://images.ygoprodeck.com/images/cards_small/86871615.jpg\"" +
                    "}" +
                "]";

        Assertions.assertThrows(MalformedJsonException.class, () -> {
            YuGiOhCardDecorator[] cards = jsonReader.importYugiCards(yuGiOhCard);
            System.out.println(cards[0]);
        });
    }

    @Test
    public void testMagicMalformedJSON() {
        JsonReader jsonReader = new JsonReader();

        String magicCards = "[" +
                    "{" +
                        "\"artist\": \"Pete Venters\"," +
                        "\"name\": \"Ancestor's Chosen\"," +
                        "\"text\": \"First strike (This creature deals combat damage before creatures without first strike.)\\nWhen Ancestor's Chosen enters the battlefield, you gain 1 life for each card in your graveyard.\"," +
                        "\"types\": \"Creature\"," +
                        "\"rarity\": \"uncommon\"," +
                        "\"hasFoil\": \"0\"," +
                        "\"isAlternative\": \"0\"," +
                        "\"isFullArt\": \"0\"," +
                        "\"isPromo\": \"0\"," +
                        "\"isReprint\": \"1\"" +
                    "}" +
                    "{" +
                        "\"artist\": \"Volkan Ba\\u01f5a\"," +
                        "\"name\": \"Angel of Mercy\"," +
                        "\"text\": \"Flying\\nWhen Angel of Mercy enters the battlefield, you gain 3 life.\"," +
                        "\"types\": \"Creature\"," +
                        "\"rarity\": \"uncommon\"," +
                        "\"hasFoil\": \"0\"," +
                        "\"isAlternative\": \"0\"," +
                        "\"isFullArt\": \"0\"," +
                        "\"isPromo\": \"0\"," +
                        "\"isReprint\": \"1\"" +
                    "" +
                "]";

        Assertions.assertThrows(MalformedJsonException.class, () -> {
            MagicCardDecorator[] cards = jsonReader.importMagicCards(magicCards);
            System.out.println(cards[0]);
        });
    }

    @Test
    public void testPokemonMalformedJSON() {
        JsonReader jsonReader = new JsonReader();

        String pokemonCards = "" +
                    "" +
                        "\"illustrator\": \"Ryota Murayama\"," +
                        "\"image\": \"https://assets.tcgdex.net/en/swsh/swsh4.5/8\"," +
                        "\"name\": \"Decidueye\"," +
                        "\"rarity\": \"Rare\"," +
                        "\"variants\": {" +
                            "\"firstEdition\": false," +
                            "\"holo\": true," +
                            "\"normal\": false," +
                            "\"reverse\": true," +
                            "\"wPromo\": false" +
                        "}," +
                        "\"types\": [" +
                            "\"Grass\"" +
                        "]," +
                        "\"description\": \"As if wielding a bow, it launches the arrow quills hidden among the feathers of its wings. Decidueye's shots never miss.\"" +
                    "}," +
                    "{" +
                        "\"illustrator\": \"5ban Graphics\"," +
                        "\"image\": \"https://assets.tcgdex.net/en/xy/xy4/70\"," +
                        "\"name\": \"Dedenne\"," +
                        "\"rarity\": \"Common\"," +
                        "\"variants\": {" +
                            "\"firstEdition\": false," +
                            "\"holo\": true," +
                            "\"normal\": true," +
                            "\"reverse\": true," +
                            "\"wPromo\": false" +
                        "}," +
                        "\"types\": [" +
                            "\"Fairy\"" +
                        "]" +
                    "}" +
                "]";

        Assertions.assertThrows(MalformedJsonException.class, () -> {
            PokemonCardDecorator[] cards = jsonReader.importPokemonCards(pokemonCards);
            System.out.println(cards[0]);
        });
    }
}
