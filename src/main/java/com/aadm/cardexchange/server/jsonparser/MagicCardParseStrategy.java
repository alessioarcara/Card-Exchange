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

public class MagicCardParseStrategy implements CardParseStrategy {
    public Card execute(JsonObject json) {
        // properties
        List<Property> textProperties = getPropertiesFromJson(json, PropertyType.TEXT, "name", "text", "artist");
        List<Property> catProperties = getPropertiesFromJson(json, PropertyType.CATEGORICAL, "types", "rarity");
        // variants
        List<String> variants = getVariantsFromJson(json, "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint");
        return new Card(Game.MAGIC, Stream.of(textProperties, catProperties).flatMap(Collection::stream).toArray(Property[]::new), variants.toArray(new String[0]));
    }
}
