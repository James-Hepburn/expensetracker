package com.example.expense.tracker.cli;

import com.example.expense.tracker.model.Expense;
import com.example.expense.tracker.model.User;
import com.example.expense.tracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class CommandRunner implements CommandLineRunner {
    private Scanner input = new Scanner (System.in);
    private UserRepository usersRepo;

    public CommandRunner (UserRepository usersRepo) {
        this.usersRepo = usersRepo;
    }

    public User login () {
        User user;

        while (true) {
            System.out.print ("\nPlease enter your username: ");
            String username = input.nextLine ();

            Optional <User> userOptional = usersRepo.findByUsername (username);

            if (userOptional.isPresent ()) {
                user = userOptional.get ();

                System.out.print ("Please enter your pin: ");
                String pin = input.nextLine ();

                if (user.getPin ().equals (pin)) {
                    System.out.println ("Successful login.");
                    break;
                } else {
                    System.out.println ("Invalid pin.");
                }
            } else {
                System.out.println ("Invalid username.");
            }
        }

        return user;
    }

    public User create () {
        User user;

        while (true) {
            System.out.print ("\nPlease enter your new username: ");
            String username = input.nextLine ();

            Optional <User> userOptional = usersRepo.findByUsername (username);

            if (userOptional.isPresent ()) {
                System.out.println ("User already exists.");
            } else {
                System.out.print ("Please enter your new (4-digit) pin: ");
                String pin = input.nextLine ();

                if (pin.length () != 4) {
                    System.out.println ("Invalid pin length.");
                }

                boolean isNumeric = true;

                for (int i = 0; i < 4; i++) {
                    if (!Character.isDigit (pin.charAt (i))) {
                        isNumeric = false;
                        break;
                    }
                }

                if (!isNumeric) {
                    System.out.println ("Invalid pin content.");
                } else {
                    user = new User (username, pin);
                    System.out.println ("Successful registration.");
                    break;
                }
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

            System.out.print ("Please select an option: ");
            int option = input.nextInt ();

            input.nextLine ();

            if (option == 1) {
                user = login ();
                break;
            } else if (option == 2) {
                user = create ();
                break;
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
        System.out.print ("\nEnter the expense: ");
        String description = input.nextLine ();
        System.out.print ("Enter the amount: ");
        double amount = input.nextDouble ();

        Expense expense = new Expense (description, amount);
        user.getExpenseRepo ().save (expense);
    }

    public void deleteExpense (User user) {

    }

    public void updateExpense (User user) {

    }

    public void viewAllExpenses (User user) {
        System.out.println ("\nHere are all your expenses:");

        List <Expense> expenses = user.getExpenseRepo ().findAll ();

        for (int i = 0; i < expenses.size (); i++) {
            System.out.println (expenses.get (i));
        }
    }

    public void viewExpensesByAmount (User user) {

    }

    public void viewExpensesByDate (User user) {

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

            System.out.print ("Please select an option: ");
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