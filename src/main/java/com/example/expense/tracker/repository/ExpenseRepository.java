package com.example.expense.tracker.repository;

import com.example.expense.tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List <Expense> findByDescription (String description);

    List <Expense> findByDateAfter (LocalDate start);                      // for "last X days"
    List <Expense> findByDateBetween (LocalDate start, LocalDate end);

    List <Expense> findByAmountAfter (double start);
    List <Expense> findByAmountBetween (double start, double end);
}