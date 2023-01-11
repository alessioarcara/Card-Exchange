package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImportJSONTest {

    @Test
    public void testPokemonImportJSON() {
        ListenerImpl listener = new ListenerImpl();

        String pokemonCard = "[" +
                    "{" +
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
                    "{\n" +
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

        PokemonCardDecorator[] pokemon = (PokemonCardDecorator[]) (listener.importJSON(pokemonCard, 'p'));

        Assertions.assertEquals(pokemon[0].getArtist(), "Ryota Murayama"); // illustrator
        Assertions.assertEquals(pokemon[0].getImageUrl(), "https://assets.tcgdex.net/en/swsh/swsh4.5/8");
        Assertions.assertEquals(pokemon[0].getName(), "Decidueye");
        Assertions.assertEquals(pokemon[0].getRarity(), "Rare");
        Assertions.assertFalse(pokemon[0].getIsFirstEdition());
        Assertions.assertTrue(pokemon[0].getIsHolo());
        Assertions.assertFalse(pokemon[0].getIsNormal());
        Assertions.assertTrue(pokemon[0].getIsReverse());
        Assertions.assertFalse(pokemon[0].getIsPromo());
        Assertions.assertEquals(pokemon[0].getType(), "Grass");
        Assertions.assertEquals(pokemon[0].getDescription(), "As if wielding a bow, it launches the arrow quills hidden among the feathers of its wings. Decidueye's shots never miss.");

        Assertions.assertEquals(pokemon[1].getArtist(), "5ban Graphics"); // illustrator
        Assertions.assertEquals(pokemon[1].getImageUrl(), "https://assets.tcgdex.net/en/xy/xy4/70");
        Assertions.assertEquals(pokemon[1].getName(), "Dedenne");
        Assertions.assertEquals(pokemon[1].getRarity(), "Common");
        Assertions.assertFalse(pokemon[1].getIsFirstEdition());
        Assertions.assertTrue(pokemon[1].getIsHolo());
        Assertions.assertTrue(pokemon[1].getIsNormal());
        Assertions.assertTrue(pokemon[1].getIsReverse());
        Assertions.assertFalse(pokemon[1].getIsPromo());
        Assertions.assertEquals(pokemon[1].getType(), "Fairy");
        Assertions.assertEquals(pokemon[1].getDescription(), "");
    }

    @Test
    public void testMagicImportJSON() {
        ListenerImpl listener = new ListenerImpl();

        String magicCard = "[" +
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
                    "}," +
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
                    "}" +
                "]";

        MagicCardDecorator[] magic = (MagicCardDecorator[]) (listener.importJSON(magicCard, 'm'));

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

        Assertions.assertEquals(magic[1].getArtist(), "Volkan Ba\u01f5a");
        Assertions.assertEquals(magic[1].getName(), "Angel of Mercy");
        Assertions.assertEquals(magic[1].getDescription(), "Flying\nWhen Angel of Mercy enters the battlefield, you gain 3 life.");
        Assertions.assertEquals(magic[1].getType(), "Creature");
        Assertions.assertEquals(magic[1].getRarity(), "uncommon");
        Assertions.assertFalse(magic[1].getHasFoil());
        Assertions.assertFalse(magic[1].getIsAlternative());
        Assertions.assertFalse(magic[1].getIsFullArt());
        Assertions.assertFalse(magic[1].getIsPromo());
        Assertions.assertTrue(magic[1].getIsReprint());
    }

    @Test
    public void testYuGiOhImportJSON() {
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
                        "\"desc\": \"Special Summoned with the effect of \\\"Cloning\\\". If the monster is destroyed and sent to the Graveyard, destroy this Token.\"," +
                        "\"race\": \"Warrior\"," +
                        "\"image_url\": \"https://images.ygoprodeck.com/images/cards/86871615.jpg\"," +
                        "\"small_image_url\": \"https://images.ygoprodeck.com/images/cards_small/86871615.jpg\"" +
                    "}" +
                "]";

        YuGiOhCardDecorator[] yuGiOh = (YuGiOhCardDecorator[]) (listener.importJSON(yuGiOhCard, 'y'));

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
}
