package com.example.expense.tracker.repository;

import com.example.expense.tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByDescription (String description);
}