package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.models.CardImpl;
import com.aadm.cardexchange.shared.models.PokemonCardDecorator;
import com.google.gson.JsonObject;

public class PokemonCardParseStrategy implements CardParseStrategy {

    public PokemonCardDecorator execute(JsonObject json) {
        // fields
        String name = json.has("name") ? json.get("name").getAsString() : "";
        String description = json.has("description") ? json.get("description").getAsString() : "";
        String types = json.has("types") ? json.get("types").getAsString() : "";
        String illustrator = json.has("illustrator") ? json.get("illustrator").getAsString() : "";
        String image = json.has("image") ? json.get("image").getAsString() : "";
        String rarity = json.has("rarity") ? json.get("rarity").getAsString() : "";

        JsonObject variants = json.has("variants") ? json.getAsJsonObject("variants") : null;
        boolean isFirstEdition = false;
        boolean isHolo = false;
        boolean isNormal = false;
        boolean isReverse = false;
        boolean isPromo = false;

        if (json.has("variants")) {
            assert variants != null;
            isFirstEdition = variants.has("firstEdition") && variants.get("firstEdition").getAsBoolean();
            isHolo = variants.has("holo") && variants.get("holo").getAsBoolean();
            isNormal = variants.has("normal") && variants.get("normal").getAsBoolean();
            isReverse = variants.has("reverse") && variants.get("reverse").getAsBoolean();
            isPromo = variants.has("wPromo") && variants.get("wPromo").getAsBoolean();
        }

        return new PokemonCardDecorator(new CardImpl(name, description, types),
                illustrator, image, rarity,
                isFirstEdition, isHolo, isNormal, isReverse, isPromo);
    }
}
