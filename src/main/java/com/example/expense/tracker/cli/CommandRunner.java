package com.example.expense.tracker.cli;

import com.example.expense.tracker.model.Expense;
import com.example.expense.tracker.model.User;
import com.example.expense.tracker.repository.ExpenseRepository;
import com.example.expense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class CommandRunner implements CommandLineRunner {
    private Scanner input = new Scanner (System.in);

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    public User login () {
        User user = null;

        System.out.print ("\nEnter your username: ");
        String username = input.nextLine ();

        Optional <User> userOptional = usersRepo.findByUsername (username);

        if (userOptional.isPresent ()) {
            user = userOptional.get ();

            System.out.print ("Enter your pin: ");
            String pin = input.nextLine ();

            if (user.getPin ().equals (pin)) {
                System.out.println ("Successful login.");
            } else {
                System.out.println ("Invalid pin.");
                user = null;
            }
        } else {
            System.out.println ("Invalid username.");
        }

        return user;
    }

    public User create () {
        User user = null;

        System.out.print ("\nEnter your new username: ");
        String username = input.nextLine ();

        Optional <User> userOptional = usersRepo.findByUsername (username);

        if (userOptional.isPresent ()) {
            System.out.println ("User already exists.");
        } else {
            System.out.print ("Enter your new (4-digit) pin: ");
            String pin = input.nextLine ();

            if (pin.length () != 4) {
                System.out.println ("Invalid pin length.");
                return user;
            }

            boolean isNumeric = true;

            for (int i = 0; i < 4; i++) {
                if (!Character.isDigit (pin.charAt (i))) {
                    isNumeric = false;
                }
            }

            if (!isNumeric) {
                System.out.println ("Invalid pin content.");
            } else {
                user = new User (username, pin);
                usersRepo.save (user);
                System.out.println ("Successful registration.");
            }
        }

        return user;
    }

    public User loginOrCreate () {
        User user;

        while (true) {
            System.out.println ("\n1. Login as existing user");
            System.out.println ("2. Create a new user");
            System.out.println ("3. Quit program");

            System.out.print ("Select an option: ");
            int option = input.nextInt ();

            input.nextLine ();

            if (option == 1) {
                user = login ();

                if (user != null) {
                    break;
                }
            } else if (option == 2) {
                user = create ();

                if (user != null) {
                    break;
                }
            } else if (option == 3) {
              user = null;
              break;
            } else {
                System.out.println ("Invalid option.");
            }
        }

        return user;
    }

    public void addExpense (User user) {
        input.nextLine ();

        System.out.print ("\nEnter the expense: ");
        String description = input.nextLine ();
        System.out.print ("Enter the amount: ");
        double amount = input.nextDouble ();

        Expense expense = new Expense (description, amount, user);
        expenseRepo.save (expense);
    }

    public void deleteExpense (User user) {
        input.nextLine ();

        System.out.print ("\nEnter the expense: ");
        String description = input.nextLine ();

        List <Expense> expenseList = expenseRepo.findByDescriptionAndUser (description, user);

        if (expenseList.isEmpty ()) {
            System.out.println ("Expense not found.");
        } else if (expenseList.size () == 1) {
            expenseRepo.delete (expenseList.get (0));
            System.out.println ("Expense successfully deleted.");
        } else {
            System.out.println ("\nHere are all the matching expenses:");

            for (int i = 0; i < expenseList.size (); i++) {
                System.out.println (i + 1 + ". " + expenseList.get (i));
            }

            System.out.print ("Enter an expense to delete: ");
            int option = input.nextInt ();

            if (option >= 1 && option <= expenseList.size ()) {
                expenseRepo.delete (expenseList.get (option - 1));
                System.out.println ("Expense successfully deleted.");
            } else {
                System.out.println ("Invalid option.");
            }
        }
    }

    public void updateExpenseHelper (User user, Expense expense) {
        System.out.println ("\n1. Update description");
        System.out.println ("2. Update amount");

        System.out.print ("Enter an option: ");
        int option = input.nextInt ();

        if (option == 1) {
            input.nextLine ();

            System.out.print ("Enter the new description: ");
            String description = input.nextLine ();

            expense.setDescription (description);
            expenseRepo.save (expense);
        } else if (option == 2) {
            System.out.print ("Enter the new amount: ");
            double amount = input.nextDouble ();

            expense.setAmount (amount);
            expenseRepo.save (expense);
        } else {
            System.out.println ("Invalid option.");
        }
    }

    public void updateExpense (User user) {
        input.nextLine ();

        System.out.print ("\nEnter the expense: ");
        String description = input.nextLine ();

        List <Expense> expenseList = expenseRepo.findByDescriptionAndUser (description, user);

        if (expenseList.isEmpty ()) {
            System.out.println ("Expense not found.");
        } else if (expenseList.size () == 1) {
            updateExpenseHelper (user, expenseList.get (0));
        } else {
            System.out.println ("\nHere are all the matching expenses:");

            for (int i = 0; i < expenseList.size (); i++) {
                System.out.println (i + 1 + ". " + expenseList.get (i));
            }

            System.out.print ("Enter an expense to update: ");
            int option = input.nextInt ();

            if (option >= 1 && option <= expenseList.size ()) {
                updateExpenseHelper (user, expenseList.get (option - 1));
            } else {
                System.out.println ("Invalid option.");
            }
        }
    }

    public void viewAllExpenses (User user) {
        System.out.println ("\nHere are all your expenses:");

        List <Expense> expenses = expenseRepo.findByUser (user);

        for (int i = 0; i < expenses.size (); i++) {
            System.out.println (expenses.get (i));
        }
    }

    public void viewExpensesByAmount (User user) {
        System.out.println ("\n1. Under $50");
        System.out.println ("2. $51 to $199");
        System.out.println ("3. $200 and above");

        System.out.print ("Enter an option: ");
        int option = input.nextInt ();

        if (option == 1) {
            System.out.println ("\nHere are all the expenses under $50:");

            List <Expense> expenses = expenseRepo.findByAmountBetweenAndUser (0, 50.99, user);

            for (int i = 0; i < expenses.size (); i++) {
                System.out.println (expenses.get (i));
            }
        } else if (option == 2) {
            System.out.println ("\nHere are all the expenses from $51 to $199:");

            List <Expense> expenses = expenseRepo.findByAmountBetweenAndUser (51, 199.99, user);

            for (int i = 0; i < expenses.size (); i++) {
                System.out.println (expenses.get (i));
            }
        } else if (option == 3) {
            System.out.println ("\nHere are all the expenses $200 and above:");

            List <Expense> expenses = expenseRepo.findByAmountAfterAndUser (200, user);

            for (int i = 0; i < expenses.size (); i++) {
                System.out.println (expenses.get (i));
            }
        } else {
            System.out.println ("Invalid option.");
        }
    }

    public void viewExpensesByDate (User user) {
        System.out.println ("\n1. Last 7 days");
        System.out.println ("2. Last 30 days");
        System.out.println ("3. All time");

        System.out.print ("Enter an option: ");
        int option = input.nextInt ();

        if (option == 1) {
            System.out.println ("\nHere are all the expenses within the last 7 days:");

            List <Expense> expenses = expenseRepo.findByDateBetweenAndUser (LocalDate.now ().minusDays (7), LocalDate.now (), user);

            for (int i = 0; i < expenses.size (); i++) {
                System.out.println (expenses.get (i));
            }
        } else if (option == 2) {
            System.out.println ("\nHere are all the expenses within the last 30 days:");

            List <Expense> expenses = expenseRepo.findByDateBetweenAndUser (LocalDate.now ().minusDays (30), LocalDate.now (), user);

            for (int i = 0; i < expenses.size (); i++) {
                System.out.println (expenses.get (i));
            }
        } else if (option == 3) {
            System.out.println ("\nHere are all the expenses within all time:");

            List <Expense> expenses = expenseRepo.findByUser (user);

            for (int i = 0; i < expenses.size (); i++) {
                System.out.println (expenses.get (i));
            }
        } else {
            System.out.println ("Invalid option.");
        }
    }

    public void trackExpenses (User user) {
        while (true) {
            System.out.println ("\n1. Add expense");
            System.out.println ("2. Delete expense");
            System.out.println ("3. Update expense");
            System.out.println ("4. View all expenses");
            System.out.println ("5. View expenses by amount");
            System.out.println ("6. View expenses by date");
            System.out.println ("7. Logout");

            System.out.print ("Select an option: ");
            int option = input.nextInt ();

            if (option == 1) {
                addExpense (user);
            } else if (option == 2) {
                deleteExpense (user);
            } else if (option == 3) {
                updateExpense (user);
            } else if (option == 4) {
                viewAllExpenses (user);
            } else if (option == 5) {
                viewExpensesByAmount (user);
            } else if (option == 6) {
                viewExpensesByDate (user);
            } else if (option == 7) {
                break;
            } else {
                System.out.println ("Invalid option");
            }
        }
    }

    @Override
    public void run (String... args) {
        System.out.println ("Expense Tracker");

        while (true) {
            User user = loginOrCreate ();

            if (user == null) {
                System.out.println ("Thank you for using the program.");
                break;
            } else {
                trackExpenses (user);
            }
        }

        System.exit (0);
    }
}