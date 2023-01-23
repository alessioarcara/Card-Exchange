package com.aadm.cardexchange.client.AuthSubject;

import java.util.ArrayList;
import java.util.List;

public class AuthSubject {
    List<Observer> observers = new ArrayList<>();
    String token;

    public AuthSubject(String token) {
        this.token = token;
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        notifyObservers();
    }

    public boolean isLoggedIn() {
        return token != null;
    }
}
