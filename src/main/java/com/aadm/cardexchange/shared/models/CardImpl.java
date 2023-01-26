package com.aadm.cardexchange.shared.models;


public class CardImpl implements Card {
    private static final long serialVersionUID = -1573268135415808142L;
    private String name;
    private String description;
    private String type;

    public CardImpl(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public CardImpl() {
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
