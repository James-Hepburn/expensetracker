package com.example.expense.tracker.repository;

import com.example.expense.tracker.model.Expense;
import com.example.expense.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List <Expense> findByUser (User user);

    List <Expense> findByDescription (String description, User user);

    List <Expense> findByDateAfter (LocalDate start, User user);                      // for "last X days"
    List <Expense> findByDateBetween (LocalDate start, LocalDate end, User user);

    List <Expense> findByAmountAfter (double start, User user);
    List <Expense> findByAmountBetween (double start, double end, User user);
}