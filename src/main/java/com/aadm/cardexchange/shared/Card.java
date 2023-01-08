package com.aadm.cardexchange.shared;

import java.io.Serializable;

/*
 * Le classi che possono essere serializzabili devono implementare l'interfaccia Serializable.
 * Java di default serializza e deserializza gli oggetti. È possibile fare override, nel caso non
 * fosse possibile serializzare qualche particolare attributo non serializzabile.
 * Attenzione: tutte le referenze di altri oggetti alla classe che implementa Serializable devono
 * implementare Serializable.
 */
public class Card implements Serializable {
    /*
     * Ogni classe serializable viene associata a un serialVersionUID, che
     * viene utilizzato durante la deserializzazione per verificare che la
     * classe deserializzabile sia la stessa della classe serializzata.
     * Di default Java specifica un serialVersionUID, ma è fortemente
     * raccomandato che ogni oggetto serializzabile speicifichi il suo
     * serialVersionUID per garantire che sia sempre diverso tra tutte le
     * classi serializzate.
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;

    public Card() {
    }

    public Card(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "<" + name + ", " + description + ">";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
