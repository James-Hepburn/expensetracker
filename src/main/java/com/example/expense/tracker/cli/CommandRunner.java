package com.example.expense.tracker.cli;

import com.example.expense.tracker.model.User;
import com.example.expense.tracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

            System.out.print ("Please select an option: ");
            int option = input.nextInt ();

            input.nextLine ();

            if (option == 1) {
                user = login ();
                break;
            } else if (option == 2) {
                user = create ();
                break;
            } else {
                System.out.println ("Invalid option.");
            }
        }

        return user;
    }

    @Override
    public void run (String... args) {
        System.out.println ("Expense Tracker");

        User user = loginOrCreate ();

        while (true) {

        }
    }
}