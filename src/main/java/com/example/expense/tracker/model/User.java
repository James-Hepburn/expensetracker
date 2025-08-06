package com.example.expense.tracker.model;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pin;

    @Column (unique = true)
    private String username;

    public User (String username, int pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername () {
        return this.username;
    }
    public int getPin () {
        return this.pin;
    }

    public void setPin (int pin) {
        this.pin = pin;
    }
}