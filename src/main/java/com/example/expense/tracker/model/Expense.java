package com.example.expense.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double amount;
    private LocalDate date;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    public Expense () {

    }

    public Expense (String description, double amount, User user) {
        this.description = description;
        this.amount = amount;
        this.user = user;
        this.date = LocalDate.now ();
    }

    public String toString () {
        return "Expense: " + this.description + " Amount: $" + this.amount + " Date: " + date.toString ();
    }

    public void setDescription (String description) {
        this.description = description;
    }
    public void setAmount (double amount) {
        this.amount = amount;
    }
}