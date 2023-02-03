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

public class YuGiOhCardParseStrategy implements CardParseStrategy {
    public Card execute(JsonObject json) {
        // properties
        List<Property> textProperties = getPropertiesFromJson(json, PropertyType.TEXT, "name", "desc");
        List<Property> catProperties = getPropertiesFromJson(json, PropertyType.CATEGORICAL, "type", "race");
        List<Property> otherProperties = getPropertiesFromJson(json, PropertyType.OTHER, "image_url", "small_image_url");

        return new Card(Game.YUGIOH, Stream.of(textProperties, catProperties, otherProperties).flatMap(Collection::stream).toArray(Property[]::new));
    }
}
