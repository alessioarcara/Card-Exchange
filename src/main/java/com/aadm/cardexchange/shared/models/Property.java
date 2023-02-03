package com.aadm.cardexchange.shared.models;

import java.io.Serializable;
import java.util.Objects;

public class Property implements Serializable {
    private static final long serialVersionUID = 6309433880398230595L;
    String name;
    PropertyType type;
    String value;

    public Property(String name, PropertyType type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Property() {
    }

    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property = (Property) o;
        return Objects.equals(name, property.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
