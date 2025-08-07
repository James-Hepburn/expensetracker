package com.example.expense.tracker.model;

import jakarta.persistence.*;

@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pin;

    @Column (unique = true)
    private String username;

    public User () {

    }

    public User (String username, String pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername () {
        return this.username;
    }
    public String getPin () {
        return this.pin;
    }

    public void setPin (String pin) {
        this.pin = pin;
    }
}