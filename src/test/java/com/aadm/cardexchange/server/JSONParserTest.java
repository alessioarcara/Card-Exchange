package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.jsonparser.*;
import com.aadm.cardexchange.shared.models.CardDecorator;
import com.aadm.cardexchange.shared.models.MagicCardDecorator;
import com.aadm.cardexchange.shared.models.PokemonCardDecorator;
import com.aadm.cardexchange.shared.models.YuGiOhCardDecorator;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class JSONParserTest {

    @Test
    public void testYuGiOhJSONParse() throws FileNotFoundException {
        Gson gson = new Gson();
        CardParseStrategy strategy = new YuGiOhCardParseStrategy();
        JSONParser parser = new JSONParser(strategy, gson);

        CardDecorator[] yuGiOhCards = parser.parseJSON("src/main/resources/json/yugioh_cards.json");

        Assertions.assertEquals(yuGiOhCards[0].getName(), "Steel Ogre Grotto #2");
        Assertions.assertEquals(yuGiOhCards[0].getType(), "Normal Monster");
        Assertions.assertEquals(yuGiOhCards[0].getDescription(), "A mechanized iron doll with tremendous strength.");
        Assertions.assertEquals(((YuGiOhCardDecorator) yuGiOhCards[0]).getRace(), "Machine");
        Assertions.assertEquals(((YuGiOhCardDecorator) yuGiOhCards[0]).getImageUrl(), "https://images.ygoprodeck.com/images/cards/90908427.jpg");
        Assertions.assertEquals(((YuGiOhCardDecorator) yuGiOhCards[0]).getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/90908427.jpg");

        Assertions.assertEquals(yuGiOhCards[1].getName(), "Clone Token");
        Assertions.assertEquals(yuGiOhCards[1].getType(), "Token");
        Assertions.assertEquals(yuGiOhCards[1].getDescription(), "Special Summoned with the effect of \"Cloning\". If the monster is destroyed and sent to the Graveyard, destroy this Token.");
        Assertions.assertEquals(((YuGiOhCardDecorator) yuGiOhCards[1]).getRace(), "Warrior");
        Assertions.assertEquals(((YuGiOhCardDecorator) yuGiOhCards[1]).getImageUrl(), "https://images.ygoprodeck.com/images/cards/86871615.jpg");
        Assertions.assertEquals(((YuGiOhCardDecorator) yuGiOhCards[1]).getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/86871615.jpg");

        Assertions.assertEquals(yuGiOhCards.length, 200);
    }

    @Test
    public void testMagicImportJSON() throws FileNotFoundException {
        Gson gson = new Gson();
        CardParseStrategy strategy = new MagicCardParseStrategy();
        JSONParser parser = new JSONParser(strategy, gson);

        CardDecorator[] magicCards = parser.parseJSON("src/main/resources/json/magic_cards.json");

        Assertions.assertEquals(((MagicCardDecorator) magicCards[0]).getArtist(), "Pete Venters");
        Assertions.assertEquals(magicCards[0].getName(), "Ancestor's Chosen");
        Assertions.assertEquals(magicCards[0].getDescription(), "First strike (This creature deals combat damage before creatures without first strike.)\nWhen Ancestor's Chosen enters the battlefield, you gain 1 life for each card in your graveyard.");
        Assertions.assertEquals(magicCards[0].getType(), "Creature");
        Assertions.assertEquals(((MagicCardDecorator) magicCards[0]).getRarity(), "uncommon");
        Assertions.assertFalse(((MagicCardDecorator) magicCards[0]).getHasFoil());
        Assertions.assertFalse(((MagicCardDecorator) magicCards[0]).getIsAlternative());
        Assertions.assertFalse(((MagicCardDecorator) magicCards[0]).getIsFullArt());
        Assertions.assertFalse(((MagicCardDecorator) magicCards[0]).getIsPromo());
        Assertions.assertTrue(((MagicCardDecorator) magicCards[0]).getIsReprint());

        Assertions.assertEquals(((MagicCardDecorator) magicCards[2]).getArtist(), "Volkan Ba\u01f5a");
        Assertions.assertEquals(magicCards[2].getName(), "Angel of Mercy");
        Assertions.assertEquals(magicCards[2].getDescription(), "Flying\nWhen Angel of Mercy enters the battlefield, you gain 3 life.");
        Assertions.assertEquals(magicCards[2].getType(), "Creature");
        Assertions.assertEquals(((MagicCardDecorator) magicCards[2]).getRarity(), "uncommon");
        Assertions.assertFalse(((MagicCardDecorator) magicCards[2]).getHasFoil());
        Assertions.assertFalse(((MagicCardDecorator) magicCards[2]).getIsAlternative());
        Assertions.assertFalse(((MagicCardDecorator) magicCards[2]).getIsFullArt());
        Assertions.assertFalse(((MagicCardDecorator) magicCards[2]).getIsPromo());
        Assertions.assertTrue(((MagicCardDecorator) magicCards[2]).getIsReprint());

        Assertions.assertEquals(magicCards.length, 201);
    }

    @Test
    public void testPokemonImportJSON() throws FileNotFoundException {
        Gson gson = new Gson();
        CardParseStrategy strategy = new PokemonCardParseStrategy();
        JSONParser parser = new JSONParser(strategy, gson);

        CardDecorator[] pokemonCards = parser.parseJSON("src/main/resources/json/pokemon_cards.json");

        Assertions.assertEquals(((PokemonCardDecorator) pokemonCards[0]).getArtist(), "Ken Sugimori"); // illustrator
        Assertions.assertEquals(((PokemonCardDecorator) pokemonCards[0]).getImageUrl(), "https://assets.tcgdex.net/en/gym/gym1/98");
        Assertions.assertEquals(pokemonCards[0].getName(), "Brock");
        Assertions.assertEquals(((PokemonCardDecorator) pokemonCards[0]).getRarity(), "Rare");
        Assertions.assertFalse(((PokemonCardDecorator) pokemonCards[0]).getIsFirstEdition());
        Assertions.assertTrue(((PokemonCardDecorator) pokemonCards[0]).getIsHolo());
        Assertions.assertTrue(((PokemonCardDecorator) pokemonCards[0]).getIsNormal());
        Assertions.assertTrue(((PokemonCardDecorator) pokemonCards[0]).getIsReverse());
        Assertions.assertFalse(((PokemonCardDecorator) pokemonCards[0]).getIsPromo());
        Assertions.assertEquals(pokemonCards[0].getType(), "");
        Assertions.assertEquals(pokemonCards[0].getDescription(), "");

        Assertions.assertEquals(((PokemonCardDecorator) pokemonCards[11]).getArtist(), "5ban Graphics"); // illustrator
        Assertions.assertEquals(((PokemonCardDecorator) pokemonCards[11]).getImageUrl(), "https://assets.tcgdex.net/en/xy/xy4/70");
        Assertions.assertEquals(pokemonCards[11].getName(), "Dedenne");
        Assertions.assertEquals(((PokemonCardDecorator) pokemonCards[11]).getRarity(), "Common");
        Assertions.assertFalse(((PokemonCardDecorator) pokemonCards[11]).getIsFirstEdition());
        Assertions.assertTrue(((PokemonCardDecorator) pokemonCards[11]).getIsHolo());
        Assertions.assertTrue(((PokemonCardDecorator) pokemonCards[11]).getIsNormal());
        Assertions.assertTrue(((PokemonCardDecorator) pokemonCards[11]).getIsReverse());
        Assertions.assertFalse(((PokemonCardDecorator) pokemonCards[11]).getIsPromo());
        Assertions.assertEquals(pokemonCards[11].getType(), "Fairy");
        Assertions.assertEquals(pokemonCards[11].getDescription(), "");

        Assertions.assertEquals(pokemonCards.length, 200);
    }

    @Test
    public void testParserSetStrategy() throws FileNotFoundException {
        Gson gson = new Gson();
        JSONParser jsonParser = new JSONParser(new YuGiOhCardParseStrategy(), gson);

        jsonParser.setParseStrategy(new PokemonCardParseStrategy());
        CardDecorator[] cards = jsonParser.parseJSON("src/main/resources/json/pokemon_cards.json");

        Assertions.assertTrue(cards[0] instanceof PokemonCardDecorator);
    }
}
