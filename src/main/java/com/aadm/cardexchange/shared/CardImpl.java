package com.aadm.cardexchange.shared;

public class CardImpl implements Card {
    private final String name;
    private final String description;
    private final String type;

    public CardImpl(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
