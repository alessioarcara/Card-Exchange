package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.Property;
import com.aadm.cardexchange.shared.models.PropertyType;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.aadm.cardexchange.server.jsonparser.JSONParser.getPropertiesFromJson;
import static com.aadm.cardexchange.server.jsonparser.JSONParser.getVariantsFromJson;

public class PokemonCardParseStrategy implements CardParseStrategy {
    public Card execute(JsonObject json) {
        // properties
        List<Property> textProperties = getPropertiesFromJson(json, PropertyType.TEXT, "name", "description", "illustrator");
        List<Property> catProperties = getPropertiesFromJson(json, PropertyType.CATEGORICAL, "types", "rarity");
        List<Property> otherProperties = getPropertiesFromJson(json, PropertyType.OTHER, "image");
        // variants
        JsonObject variantsObject = json.getAsJsonObject("variants");
        List<String> variants = getVariantsFromJson(variantsObject, "firstEdition", "holo", "normal", "reverse", "wPromo");

        return new Card(Game.POKEMON, Stream.of(textProperties, catProperties, otherProperties).flatMap(Collection::stream).toArray(Property[]::new), variants.toArray(new String[0]));
    }
}
