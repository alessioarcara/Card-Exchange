package com.aadm.cardexchange.server.jsonparser;

import com.aadm.cardexchange.shared.CardDecorator;
import com.google.gson.JsonObject;

public interface CardParseStrategy {
    CardDecorator execute(JsonObject json);
}
