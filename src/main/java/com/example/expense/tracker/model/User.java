package com.example.expense.tracker.model;

import com.example.expense.tracker.repository.ExpenseRepository;
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ExpenseRepository expenseRepo;
    private String pin;

    @Column (unique = true)
    private String username;

    public User (ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
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
    public ExpenseRepository getExpenseRepo () { return this.expenseRepo; }

    public void setPin (String pin) {
        this.pin = pin;
    }
}