package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.models.*;

import java.util.HashMap;
import java.util.Map;

public class MockCardData {
    static Map<Integer, MagicCardDecorator> createMagicDummyData() {
        return new HashMap<>() {{
            put(0, new MagicCardDecorator(new CardImpl("Lightning Bolt", "Deal 3 damage to any target", "Instant"),
                    "Christopher Rush", "Rare", true, true, true, true, true
            ));
            put(1, new MagicCardDecorator(new CardImpl("Counterspell", "Counter target spell", "Instant"),
                    "Mark Poole", "Uncommon", false, true, false, true, false
            ));
            put(2, new MagicCardDecorator(new CardImpl("Plague Wind", "Deal X damage to each creature and each player, where X is the number of creatures you control.", "Sorcery"),
                    "Ron Spencer", "Mythic Rare", true, false, true, true, false
            ));
            put(3, new MagicCardDecorator(new CardImpl("Ancestral Recall", "Take an extra turn after this one. At the beginning of that turn's end step, you lose the game.", "Instant"),
                    "Mark Tedin", "Rare", true, true, true, false, true
            ));
        }};
    }

    static Map<Integer, PokemonCardDecorator> createPokemonDummyData() {
        return new HashMap<Integer, PokemonCardDecorator>() {{
            put(0, new PokemonCardDecorator(new CardImpl("Pikachu", "The electric mouse Pokemon", "Monster"),
                    "Atsuko Nishida", "http://www.pikachu-image.jpg", "Common", true, true, true, true, true
            ));
            put(1, new PokemonCardDecorator(new CardImpl("Charizard", "The flame Pokemon", "Monster"),
                    "Ken Sugimori", "http://www.charizard-image.jpg", "Rare", true, true, true, false, false
            ));
            put(2, new PokemonCardDecorator(new CardImpl("Blastoise", "The shellfish Pokemon", "Monster"),
                    "Mitsuhiro Arita", "http://www.blastoise-image.jpg", "Rare", true, false, true, true, false
            ));
            put(3, new PokemonCardDecorator(new CardImpl("Mewtwo", "The Genetic Pokemon", "Monster"),
                    "Ken Sugimori", "http://www.mewtwo-image.jpg", "Ultra Rare", true, false, true, true, false
            ));
        }};
    }

    static Map<Integer, CardDecorator> createYuGiOhDummyData() {
        return new HashMap<>() {
            {
                put(0, new YuGiOhCardDecorator(new CardImpl("Dark Magician", "A powerful sorcerer", "Monster"),
                        "Spellcaster", "http://www.darkmagician-image.jpg", "http://www.darkmagician-thumbnail.jpg"
                ));
                put(1, new YuGiOhCardDecorator(new CardImpl("Exodia the Forbidden One", "One of the most powerful monsters in the game", "Monster"),
                        "Divine-Beast", "http://www.exodia-image.jpg", "http://www.exodia-thumbnail.jpg"
                ));
                put(2, new YuGiOhCardDecorator(new CardImpl("Monster Reborn", "Special Summon a monster from either player's graveyard", "Spell"),
                        "", "http://www.monsterreborn-image.jpg", "http://www.monsterreborn-thumbnail.jpg"
                ));
                put(3, new YuGiOhCardDecorator(new CardImpl("Pot of Greed", "Draw 2 cards", "Spell"),
                        "", "http://www.potofgreed-image.jpg", "http://www.potofgreed-thumbnail.jpg"
                ));
            }
        };
    }
}
