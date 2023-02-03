package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.Property;
import com.aadm.cardexchange.shared.models.PropertyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Create mocks cards with distinct fields for testing purposes
public class MockCardData {
    public static Card createPokemonDummyCard() {
        return new Card(Game.POKEMON, new Property[]{
                new Property("name", PropertyType.TEXT, "Charizard"),
                new Property("description", PropertyType.TEXT, "The flame Pokemon"),
                new Property("illustrator", PropertyType.TEXT, "Ken Sugimori"),
                new Property("types", PropertyType.CATEGORICAL, "Flame"),
                new Property("rarity", PropertyType.CATEGORICAL, "Rare"),
                new Property("image", PropertyType.OTHER, "http://www.charizard-image.jpg"),
        }, "holo");
    }

    public static Card createYuGiOhDummyCard() {
        return new Card(Game.POKEMON, new Property[]{
                new Property("name", PropertyType.TEXT, "Exodia the Forbidden One"),
                new Property("desc", PropertyType.TEXT, "One of the most powerful monsters in the game"),
                new Property("type", PropertyType.CATEGORICAL, "Creature"),
                new Property("race", PropertyType.CATEGORICAL, "Divine-Beast"),
                new Property("image_url", PropertyType.OTHER, "http://www.exodia-image.jpg"),
                new Property("small_image_url", PropertyType.OTHER, "http://www.exodia-thumbnail.jpg"),
        });
    }

    public static Map<Integer, Card> createCardDummyMap() {
        return new HashMap<>() {{
            put(0, new Card(
                    "Lightning Bolt", "Deal 3 damage to any target", "Cast",
                    "Christopher Rush", "Rare",
                    "hasFoil"
            ));
            put(1, new MagicCard("Counterspell", "Counter target spell", "Instant",
                    "Mark Poole", "Uncommon",
                    "isAlternative"
            ));
            put(2, new MagicCard("Plague Wind", "Deal X damage to each creature and each player, where X is the number of creatures you control.", "Sorcery",
                    "Ron Spencer", "Mythic Rare",
                    "isFullArt"
            ));
            put(3, new MagicCard("Angel of Mercy", "When Angel of Mercy enters the battlefield, you gain 3 life", "Creature",
                    "Volkan Baa", "Mega Rare",
                    "isPromo"
            ));
            put(4, new MagicCard("Heart of Light", "Enchant creature (Target a creature as you cast this. This card enters the battlefield attached to that creature.)", "Counter",
                    "Luca Zontini", "Unique",
                    "isReprint"
            ));
        }};
    }


    public static Map<Integer, PokemonCard> createPokemonDummyMap() {
        return new HashMap<>() {{
            put(0, new PokemonCard("Pikachu", "The electric mouse Pokemon", "Electric",
                    "Atsuko Nishida", "http://www.pikachu-image.jpg", "Common",
                    "firstEdition"
            ));
            put(1, createPokemonDummyCard());
            put(2, new PokemonCard("Blastoise", "The shellfish Pokemon", "Water",
                    "Mitsuhiro Arita", "http://www.blastoise-image.jpg", "Uncommon",
                    "normal"
            ));
            put(3, new PokemonCard("Mewtwo", "The Genetic Pokemon", "Psychic",
                    "Akira Egawa", "http://www.mewtwo-image.jpg", "Ultra Rare",
                    "reverse"
            ));
            put(4, new PokemonCard("Hitmonlee", "The legs freely contract and stretch", "Fighting",
                    "Shigenori Negishi", "https://assets.tcgdex.net/en/swsh/swsh1/94", "Unique",
                    "wPromo"
            ));
        }};
    }

    public static Map<Integer, YuGiOhCard> createYuGiOhDummyMap() {
        return new HashMap<>() {
            {
                put(0, new YuGiOhCard("Dark Magician", "A powerful sorcerer", "Monster",
                        "Spellcaster", "http://www.darkmagician-image.jpg", "http://www.darkmagician-thumbnail.jpg"
                ));
                put(1, createYuGiOhDummyCard());
                put(2, new YuGiOhCard("Monster Reborn", "Special Summon a monster from either player's graveyard", "Spell",
                        "Monster", "http://www.monsterreborn-image.jpg", "http://www.monsterreborn-thumbnail.jpg"
                ));
                put(3, new YuGiOhCard("Pot of Greed", "Draw 2 cards", "Draw",
                        "Pot", "http://www.potofgreed-image.jpg", "http://www.potofgreed-thumbnail.jpg"
                ));
            }
        };
    }

    public static List<Card> createMagicDummyList() {
        return new ArrayList<>(createMagicDummyMap().values());
    }

    public static List<Card> createPokemonDummyList() {
        return new ArrayList<>(createPokemonDummyMap().values());
    }

    public static List<Card> createYuGiOhDummyList() {
        return new ArrayList<>(createYuGiOhDummyMap().values());
    }
}
