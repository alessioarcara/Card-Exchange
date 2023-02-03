package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.models.Card;
import com.google.gson.JsonObject;

public interface CardParseStrategy {
    Card execute(JsonObject json);
}
