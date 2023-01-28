package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.YuGiOhCardDecorator;
import com.google.gson.JsonObject;

public class YuGiOhCardParseStrategy implements CardParseStrategy {

    public YuGiOhCardDecorator execute(JsonObject json) {
        //fields
        String name = json.has("name") ? json.get("name").getAsString() : "unknown";
        String description = json.has("desc") ? json.get("desc").getAsString() : "unknown";
        String types = json.has("type") ? json.get("type").getAsString() : "unknown";
        String race = json.has("race") ? json.get("race").getAsString() : "unknown";
        String imageUrl = json.has("image_url") ? json.get("image_url").getAsString() : "";
        String smallImageUrl = json.has("small_image_url") ? json.get("small_image_url").getAsString() : "";

        return new YuGiOhCardDecorator(new CardImpl(name, description, types),
                                           race, imageUrl, smallImageUrl);
    }
}
