package com.aadm.cardexchange.client.auth;

import java.util.ArrayList;
import java.util.List;

public class AuthSubject {
    List<Observer> observers = new ArrayList<>();
    String token;
    String email;

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

    public String getEmail() {
        return email;
    }

    public void setCredentials(String token, String email) {
        this.token = token;
        this.email = email;
        notifyObservers();
    }

    public boolean isLoggedIn() {
        return token != null;
    }
}
