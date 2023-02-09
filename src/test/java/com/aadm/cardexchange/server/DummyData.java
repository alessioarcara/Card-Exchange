package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Create dummy cards with distinct fields for testing purposes
public class DummyData {
    public static PokemonCard createPokemonDummyCard() {
        return new PokemonCard("Charizard", "The flame Pokemon", "Flame",
                "Ken Sugimori", "http://www.charizard-image.jpg", "Rare",
                "holo");
    }

    public static YuGiOhCard createYuGiOhDummyCard() {
        return new YuGiOhCard("Exodia the Forbidden One", "One of the most powerful monsters in the game",
                "Creature", "Divine-Beast", "http://www.exodia-image.jpg", "http://www.exodia-thumbnail.jpg"
        );
    }

    public static Map<Integer, MagicCard> createMagicDummyMap() {
        return new HashMap<>() {{
            MagicCard mcd1 = new MagicCard("Lightning Bolt", "Deal 3 damage to any target", "Cast",
                    "Christopher Rush", "Rare", "hasFoil"
            );
            put(mcd1.getId(), mcd1);

            MagicCard mcd2 = new MagicCard("Counterspell", "Counter target spell", "Instant",
                    "Mark Poole", "Uncommon", "isAlternative"
            );
            put(mcd2.getId(), mcd2);

            MagicCard mcd3 = new MagicCard("Plague Wind", "Deal X damage to each creature and each player, where X is the number of creatures you control.", "Sorcery",
                    "Ron Spencer", "Mythic Rare", "isFullArt"
            );
            put(mcd3.getId(), mcd3);

            MagicCard mcd4 = new MagicCard("Angel of Mercy", "When Angel of Mercy enters the battlefield, you gain 3 life", "Creature",
                    "Volkan Baa", "Mega Rare", "isPromo"
            );
            put(mcd4.getId(), mcd4);

            MagicCard mcd5 = new MagicCard("Heart of Light", "Enchant creature (Target a creature as you cast this. This card enters the battlefield attached to that creature.)", "Counter",
                    "Luca Zontini", "Unique", "isReprint"
            );
            put(mcd5.getId(), mcd5);
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

    public static List<PhysicalCard> createPhysicalCardDummyList(int n) {
        List<PhysicalCard> list = new ArrayList<>();
        for (int i = 0; i < n; i++)
            list.add(new PhysicalCard(Game.randomGame(), (i + 3000), Status.randomStatus(), "This is a valid description."));
        return list;
    }

    public static List<PhysicalCardWithName> createPhysicalCardWithNameDummyList(int n) {
        return createPhysicalCardDummyList(n).stream().map(pCard -> new PhysicalCardWithName(pCard, "test")).collect(Collectors.toList());
    }
}
