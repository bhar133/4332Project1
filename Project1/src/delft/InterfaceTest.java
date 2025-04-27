package delft;
import java.util.*;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.Scanner;

public class InterfaceTest {


    /*
     * test suite for user facing functionalities
     *
     *
     */

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private Library library;
    private Scanner scanner;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        library = new Library();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testStart_DisplaysMainMenu() {
        // Test Main Menu appears as intended
        String input = "123456\nalice123\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("--- Login as Librarian ---"));
        assertTrue(output.contains("--- Main Menu ---"));
        assertTrue(output.contains("1. Book Options"));
        assertTrue(output.contains("2. Member Options"));
        assertTrue(output.contains("3. Librarian Options"));
        assertTrue(output.contains("4. Account Options"));
        assertTrue(output.contains("5. Exit"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void testStart_SelectBookOption() {
        // Test all Book options appear when selected
        String input = "123456\nalice123\n1\n0\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("--- Book Options ---"));
        assertTrue(output.contains("1. Check Availability"));
        assertTrue(output.contains("2. Update Book Info"));
        assertTrue(output.contains("3. Get Book Info"));
        assertTrue(output.contains("0. Return to Main Menu"));
    }

    @Test
    public void testStart_SelectMemberOption() {
        // Test all Member options appear when selected
        String input = "123456\nalice123\n2\n0\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("--- Member Options ---"));
        assertTrue(output.contains("1. Print Member Info"));
        assertTrue(output.contains("2. Get Borrowed Book List"));
        assertTrue(output.contains("3. Add Borrowed Book"));
        assertTrue(output.contains("4. Remove Borrowed Book"));
        assertTrue(output.contains("5. Update Member Info"));
    }

    @Test
    public void testStart_SelectLibrarianOption() {
        // Test all Librarian options appear when selected
        String input = "123456\nalice123\n3\n0\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("--- Librarian Options ---"));
        assertTrue(output.contains("1. Add Book"));
        assertTrue(output.contains("2. Remove Book"));
        assertTrue(output.contains("3. Add Member"));
        assertTrue(output.contains("4. Revoke Membership"));
    }

    @Test
    public void testStart_InvalidInput() {
        // Test invalid non-integer inputs are rejected
        String input = "123456\nalice123\ninvalid\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("Invalid number. Try again:"));
    }

    /*
    * tests are mostly specification based, with a few structural based tests
     */

    // ensuring correct program flow for book checkout. we login as full time librarian,
    // then select librarian options, add member, checkout book we dont have. must be purchased
    @Test
    public void testLibraryOptionsCheckoutBook() {
        String input = String.join("\n",
                "123456", "alice123", // login
                "3", // librarian options
                "3", "Real Guy", "realguy@email.com", "10163", // add member
                "5", // checkout book
                "1", "10163", // book id
                "yes", // yes or no to purchase
                "Real Name", "Real Author", "2025", "1234", "Non Fiction", // add the book info
                "0", // back to main menu
                "5"  // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Interface.start();
        String output = outContent.toString();
        assertTrue(output.contains("Book not found. Would you like to purchase it? (yes/no)"));
        assertTrue(output.contains("Book purchased and added."));
        assertTrue(output.contains("Book checked out successfully."));
        assertTrue(output.contains("Goodbye!"));
    }

    // testing book updating functionality,ensuring proper program flow and
    // that availability should not change when updating a book
    // should return new book info
    @Test
    public void testBookOptionsUpdate() {
        String input = String.join("\n",
                "123456", "alice123",
                "3", // librarian options
                "1", "1", "The Giver", "Some Guy", "2025", "119966", "Fiction", // add book
                "0", // main menu
                "1", // book options
                "2", // update book
                "1", "The Taker", "No One", "2026", "991166", "Non Fiction", // new information
                "3", // get book info
                "1", // book id
                "0", "5" // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Interface.start();
        String output = outContent.toString();
        assertTrue(output.contains("[1] The Taker by No One, 2026, Genre: Non Fiction (Available)"));
        assertTrue(output.contains("Goodbye!"));
    }

    // testing user input for seeing a members borrowed book list
    // starts by adding a member, adding a book, adding a book to a members borrowed book list
    // then grabbing borrowed book list of selected member
    // should return book id of 1

    @Test
    public void testMemberOptionsGetBorrowedBookList() {
        String input = String.join("\n",
                "123456", "alice123",
                "3", // librarian options
                "1", "99", "Terrible Book", "Bad Author", "2025", "2121", "Adventure", // add book
                "3", "Sample Member", "sample@email.com", "6969", // add a new member
                "5", "99", "6969", // checkout the book
                "0", // go back
                "2", // member options
                "2", "6969", // get bbl (borrowed book list) input member id
                "0", "5" // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Interface.start();
        String output = outContent.toString();
        assertTrue(output.contains("Borrowed Books: [99]"));
        assertTrue(output.contains("Goodbye!"));
    }


    // testing librarian login, only full time librarians should have access to
    // account options
    @Test
    public void testFullTimeLibrarianHasAccessToAccountOptions() {
        String input = String.join("\n",
                "123456", "alice123",
                "4", // account options
                "0", // main menu
                "5" // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("--- Account Options ---"));
        assertTrue(output.contains("1. Add Cash (Deposit)"));
    }

    // testing part time librarian login, only full time librarians should have access to
    // account options
    @Test
    public void testPartTimeLibrarianNoAccessToAccountOptions() {
        String input = String.join("\n",
                "456789", "seth456", // login as part time librarian
                "4", // attempt access at account options
                "5" // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("Permission denied. Only full-time librarians can access account options."));
    }

    // ensuring that when trying to remove a book that hasnt been added, should return
    // book id not found
    @Test
    public void testRemoveNonexistentBook() {
        String input = String.join("\n",
                "123456", "alice123",
                "3", // Lib options
                "2", // remove book
                "420", // book id that doesnt exist
                "0", "5" // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("Book ID not found."));
    }

    // ensuring proper functionality of withdrawing a salary with sufficient funds
    // hitting boundary
    @Test
    public void testWithdrawSalarySufficientFunds() {
        String input = String.join("\n",
                "123456", "alice123",
                "4", // accnt options
                "2", // withdraw
                "38999.99", // sufficient amount
                "0", "5" // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("Current Library Balance: $0.01"));
    }

    // when attempting to withdraw a salary even slightly above our balance, should never
    // allow it
    @Test
    public void testWithdrawSalaryInsufficientFunds() {
        String input = String.join("\n",
                "123456", "alice123",
                "4", // accnt options
                "2", // withdraw
                "39000.1", // slightly above our balance
                "0", "5" // exit
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();
        String output = outContent.toString();

        assertTrue(output.contains("Insufficient funds for withdrawal."));
    }




}






