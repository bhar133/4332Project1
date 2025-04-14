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
     */

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testStart_DisplaysMainMenu() {
        // Simulate user choosing to exit immediately (option 4)
        String input = "4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();

        String output = outContent.toString();
        assertTrue(output.contains("--- Main Menu ---"));
        assertTrue(output.contains("1. Library"));
        assertTrue(output.contains("2. Book"));
        assertTrue(output.contains("3. Member"));
        assertTrue(output.contains("4. Exit"));
        assertTrue(output.contains("Select a category:"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void testStart_SelectLibraryOption() {
        // Simulate user choosing Library (1) then exiting
        String input = "1\n0\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();

        String output = outContent.toString();
        assertTrue(output.contains("--- Library Options ---"));
        assertTrue(output.contains("1. Add Book"));
        assertTrue(output.contains("2. Remove Book"));
        assertTrue(output.contains("3. Add Member"));
        assertTrue(output.contains("4. Revoke Membership"));
        assertTrue(output.contains("5. Checkout Book"));
        assertTrue(output.contains("6. Return Book"));
        assertTrue(output.contains("7. Check Book Availability"));
        assertTrue(output.contains("8. Who Has Book"));
        assertTrue(output.contains("9. Get All Members"));
        assertTrue(output.contains("10. Find Book ID by Name"));
        assertTrue(output.contains("0. Return to Main Menu"));
        assertTrue(output.contains("Select an option: "));


    }

    @Test
    public void testStart_SelectBookOption() {
        // Simulate user choosing Book (2) then exiting
        String input = "2\n0\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();

        String output = outContent.toString();
        assertTrue(output.contains("--- Book Options ---"));
        assertTrue(output.contains("1. Check Availability"));
        assertTrue(output.contains("2. Update Book Info"));
        assertTrue(output.contains("3. Get Book Info"));
        assertTrue(output.contains("0. Return to Main Menu"));
        assertTrue(output.contains("Select an option: "));


    }

    @Test
    public void testStart_SelectMemberOption() {
        // Simulate user choosing Member (3) then exiting
        String input = "3\n0\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();

        String output = outContent.toString();
        assertTrue(output.contains("--- Member Options ---"));
        assertTrue(output.contains("1. Print Member Info"));
        assertTrue(output.contains("2. Get Borrowed Book List"));
        assertTrue(output.contains("3. Add Borrowed Book"));
        assertTrue(output.contains("4. Remove Borrowed Book"));
        assertTrue(output.contains("5. Update Member Info"));
        assertTrue(output.contains("0. Return to Main Menu"));
        assertTrue(output.contains("Select an option: "));


    }

    @Test
    public void testStart_InvalidInput() {
        // Simulate invalid input (non-number) then exit
        String input = "invalid\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();

        String output = outContent.toString();
        assertTrue(output.contains("Invalid input. Please enter a valid number."));

    }

    @Test
    public void testStart_InvalidOptionNumber() {
        // Simulate invalid option number (5) then exit
        String input = "5\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Interface.start();

        String output = outContent.toString();
        assertTrue(output.contains("Invalid main menu option."));

    }

    // testing cli user input library options, ensuring the program is functioning as intended
    // testing user input for availability and updating a book
    /*
     * simulated user flow
     * 1 library options
     * 1 add book
     * book name
     * author
     * year
     * isbn
     * book id
     * genre
     * returns back to menu, select option 3 add member
     * name
     * email
     * id
     * goes back, checkout book option 5
     * book id
     * member id
     * back to menu, 7 for book availability
     * book id
     * 0 main menu
     * 4 exit
     */

    @Test
    public void testLibraryOptionsCheckoutBook() {

        String input = """
                1
                1
                Real Name
                Real Author
                2025
                1234
                1
                Non Fiction
                3
                Real Guy
                realguy@email.com
                10163
                5
                1
                10163
                7
                1
                0
                4
                """;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Interface.start();
        String output = outContent.toString();
        assertTrue(output.contains("Book checked out successfully."));
        assertTrue(output.contains("Available: false"));
        assertTrue(output.contains("Goodbye!"));


    }

    // testing book updating functionality,ensuring proper program flow and
    // that availability should not change when updating a book
    // should return new book info
    @Test
    public void testBookOptionsUpdate() {
        String input = """
                1
                3
                test user
                test@email.com
                10
                0
                1
                1
                the giver
                some guy
                2025
                12345
                1
                sci fi
                0
                2
                2
                1
                the taker
                the guy
                2027
                123456
                non fiction
                3
                1
                0
                4
                """;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Interface.start();
        String output = outContent.toString();
        assertTrue(output.contains("New Name:"));
        assertTrue(output.contains("New Author:"));
        assertTrue(output.contains("New Year:"));
        assertTrue(output.contains("[1] the taker by the guy, 2027, Genre: non fiction (Available)"));
        assertTrue(output.contains("Goodbye!"));

    }


    // testing user input for seeing a members borrowed book list
    // starts by adding a member, adding a book, adding a book to a members borrowed book list
    // then grabbing borrowed book list of selected member
    // should return book id of 1

    @Test
    public void testMemberOptionsGetBorrowedBookList() {
        String input = """
                1
                3
                test user
                test@email.com
                10
                0
                1
                1
                the giver
                some guy
                2025
                12345
                1
                sci fi
                0
                3
                3
                10
                1
                2
                10
                0
                4
                """;

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Interface.start();
        String output = outContent.toString();
        assertTrue(output.contains("Borrowed Books: [1]"));
    }
}




