package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.MagicCardDecorator;
import com.google.gson.JsonObject;

public class MagicCardParseStrategy implements CardParseStrategy{

    public MagicCardDecorator execute(JsonObject json) {
        // fields
        String name = json.has("name") ? json.get("name").getAsString() : "";
        String description = json.has("text") ? json.get("text").getAsString() : "";
        String types = json.has("types") ? json.get("types").getAsString() : "";
        String artist = json.has("artist") ? json.get("artist").getAsString() : "";
        String rarity = json.has("rarity") ? json.get("rarity").getAsString() : "";
        boolean hasFoil = (json.has("hasFoil")) && (json.get("hasFoil").getAsInt() != 0);
        boolean isAlternative = (json.has("isAlternative")) && (json.get("isAlternative").getAsInt() != 0);
        boolean isFullArt = (json.has("isFullArt")) && (json.get("isFullArt").getAsInt() != 0);
        boolean isPromo = (json.has("isPromo")) && (json.get("isPromo").getAsInt() != 0);
        boolean isReprint = (json.has("isReprint")) && (json.get("isReprint").getAsInt() != 0);

        return new MagicCardDecorator(new CardImpl(name, description, types),
                artist, rarity,
                hasFoil, isAlternative, isFullArt, isPromo, isReprint);
    }
}
