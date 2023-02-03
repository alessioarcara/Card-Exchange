package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.jsonparser.*;
import com.aadm.cardexchange.shared.models.Card;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class JSONParserTest {
    Gson gson;

    @BeforeEach
    public void initialize() {
        gson = new Gson();
    }

    @Test
    public void testYuGiOhJSONParse() throws FileNotFoundException {
        CardParseStrategy strategy = new YuGiOhCardParseStrategy();
        JSONParser parser = new JSONParser(strategy, gson);

        Card[] yuGiOhCards = parser.parseJSON("src/main/resources/json/yugioh_cards.json");

        Assertions.assertEquals(yuGiOhCards[0].getName(), "Steel Ogre Grotto #2");
        Assertions.assertEquals(yuGiOhCards[0].getType(), "Normal Monster");
        Assertions.assertEquals(yuGiOhCards[0].getDescription(), "A mechanized iron doll with tremendous strength.");
        Assertions.assertEquals(((YuGiOhCard) yuGiOhCards[0]).getRace(), "Machine");
        Assertions.assertEquals(((YuGiOhCard) yuGiOhCards[0]).getImageUrl(), "https://images.ygoprodeck.com/images/cards/90908427.jpg");
        Assertions.assertEquals(((YuGiOhCard) yuGiOhCards[0]).getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/90908427.jpg");

        Assertions.assertEquals(yuGiOhCards[1].getName(), "Clone Token");
        Assertions.assertEquals(yuGiOhCards[1].getType(), "Token");
        Assertions.assertEquals(yuGiOhCards[1].getDescription(), "Special Summoned with the effect of \"Cloning\". If the monster is destroyed and sent to the Graveyard, destroy this Token.");
        Assertions.assertEquals(((YuGiOhCard) yuGiOhCards[1]).getRace(), "Warrior");
        Assertions.assertEquals(((YuGiOhCard) yuGiOhCards[1]).getImageUrl(), "https://images.ygoprodeck.com/images/cards/86871615.jpg");
        Assertions.assertEquals(((YuGiOhCard) yuGiOhCards[1]).getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/86871615.jpg");

        Assertions.assertEquals(yuGiOhCards.length, 200);
    }

    @Test
    public void testMagicImportJSON() throws FileNotFoundException {
        CardParseStrategy strategy = new MagicCardParseStrategy();
        JSONParser parser = new JSONParser(strategy, gson);

        Card[] magicCards = parser.parseJSON("src/main/resources/json/magic_cards.json");

        Assertions.assertEquals(((MagicCard) magicCards[0]).getArtist(), "Pete Venters");
        Assertions.assertEquals(magicCards[0].getName(), "Ancestor's Chosen");
        Assertions.assertEquals(magicCards[0].getDescription(), "First strike (This creature deals combat damage before creatures without first strike.)\nWhen Ancestor's Chosen enters the battlefield, you gain 1 life for each card in your graveyard.");
        Assertions.assertEquals(magicCards[0].getType(), "Creature");
        Assertions.assertEquals(((MagicCard) magicCards[0]).getRarity(), "uncommon");
        Assertions.assertFalse(magicCards[0].getVariants().contains("hasFoil"));
        Assertions.assertFalse(magicCards[0].getVariants().contains("isAlternative"));
        Assertions.assertFalse(magicCards[0].getVariants().contains("isFullArt"));
        Assertions.assertFalse(magicCards[0].getVariants().contains("isPromo"));
        Assertions.assertTrue(magicCards[0].getVariants().contains("isReprint"));

        Assertions.assertEquals(((MagicCard) magicCards[2]).getArtist(), "Volkan Ba\u01f5a");
        Assertions.assertEquals(magicCards[2].getName(), "Angel of Mercy");
        Assertions.assertEquals(magicCards[2].getDescription(), "Flying\nWhen Angel of Mercy enters the battlefield, you gain 3 life.");
        Assertions.assertEquals(magicCards[2].getType(), "Creature");
        Assertions.assertEquals(((MagicCard) magicCards[2]).getRarity(), "uncommon");
        Assertions.assertFalse(magicCards[2].getVariants().contains("hasFoil"));
        Assertions.assertFalse(magicCards[2].getVariants().contains("isAlternative"));
        Assertions.assertFalse(magicCards[2].getVariants().contains("isFullArt"));
        Assertions.assertFalse(magicCards[2].getVariants().contains("isPromo"));
        Assertions.assertTrue(magicCards[2].getVariants().contains("isReprint"));

        Assertions.assertEquals(magicCards.length, 201);
    }

    @Test
    public void testPokemonImportJSON() throws FileNotFoundException {
        CardParseStrategy strategy = new PokemonCardParseStrategy();
        JSONParser parser = new JSONParser(strategy, gson);

        Card[] pokemonCards = parser.parseJSON("src/main/resources/json/pokemon_cards.json");

        Assertions.assertEquals(((PokemonCard) pokemonCards[0]).getArtist(), "Ken Sugimori"); // illustrator
        Assertions.assertEquals(((PokemonCard) pokemonCards[0]).getImageUrl(), "https://assets.tcgdex.net/en/gym/gym1/98");
        Assertions.assertEquals(pokemonCards[0].getName(), "Brock");
        Assertions.assertEquals(((PokemonCard) pokemonCards[0]).getRarity(), "Rare");
        Assertions.assertFalse((pokemonCards[0]).getVariants().contains("firstEdition"));
        Assertions.assertTrue((pokemonCards[0]).getVariants().contains("holo"));
        Assertions.assertTrue((pokemonCards[0]).getVariants().contains("normal"));
        Assertions.assertTrue((pokemonCards[0]).getVariants().contains("reverse"));
        Assertions.assertFalse((pokemonCards[0]).getVariants().contains("wPromo"));
        Assertions.assertEquals(pokemonCards[0].getType(), "unknown");
        Assertions.assertEquals(pokemonCards[0].getDescription(), "unknown");

        Assertions.assertEquals(((PokemonCard) pokemonCards[11]).getArtist(), "5ban Graphics"); // illustrator
        Assertions.assertEquals(((PokemonCard) pokemonCards[11]).getImageUrl(), "https://assets.tcgdex.net/en/xy/xy4/70");
        Assertions.assertEquals(pokemonCards[11].getName(), "Dedenne");
        Assertions.assertEquals(((PokemonCard) pokemonCards[11]).getRarity(), "Common");
        Assertions.assertFalse((pokemonCards[11]).getVariants().contains("firstEdition"));
        Assertions.assertTrue((pokemonCards[11]).getVariants().contains("holo"));
        Assertions.assertTrue((pokemonCards[11]).getVariants().contains("normal"));
        Assertions.assertTrue((pokemonCards[11]).getVariants().contains("reverse"));
        Assertions.assertFalse((pokemonCards[11]).getVariants().contains("wPromo"));
        Assertions.assertEquals(pokemonCards[11].getType(), "Fairy");
        Assertions.assertEquals(pokemonCards[11].getDescription(), "unknown");

        Assertions.assertEquals(pokemonCards.length, 200);
    }

    @Test
    public void testParserSetStrategy() throws FileNotFoundException {
        JSONParser jsonParser = new JSONParser(new YuGiOhCardParseStrategy(), gson);

        jsonParser.setParseStrategy(new PokemonCardParseStrategy());
        Card[] cards = jsonParser.parseJSON("src/main/resources/json/pokemon_cards.json");

        Assertions.assertTrue(cards[0] instanceof PokemonCard);
    }
}
