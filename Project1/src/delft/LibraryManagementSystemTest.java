package delft;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryManagementSystemTest {

    //Library Class Specification Based Testing
    Library library;
    Book book, book1, book2;
    Member member, member1;

    //This will create testing values for the library class to use
    @BeforeEach
    public void setUp() {
        library = new Library();
        book = new Book("Test Book", "Test Author", 2024, "123456789", 1, "Fiction");
        member = new Member("Test User", "test@example.com", 1);
    }

    //This test will test the class adding a book to the library and then checking if it is avaiable
    @Test
    public void testAddBookAndCheckAvailability() {
        library.addBook(book);
        assertTrue(library.bookAvailability(1));
    }

    //this class will add a member to the library then check if the member is in the library system
    //Then it will also test the revoke membership function
    @Test
    public void testAddAndRevokeMember() {
        library.addMember(member);
        assertEquals(2, library.getAllMembers().size());
        library.revokeMembership(1);
        assertEquals(1, library.getAllMembers().size());
    }

    //This test will test the checking out of books, the avaiability of a book when it is check out and the return book function
    @Test
    public void testCheckoutAndReturnBook1() {
        library.addBook(book);
        library.addMember(member);
        library.checkoutBook(1, 1);
        assertFalse(book.checkAvailability());
        assertFalse(library.bookAvailability(1));
        assertEquals(List.of(1), member.getBorrowedBookList());

        library.returnBook(1, 1);
        assertTrue(book.checkAvailability());
        assertTrue(library.bookAvailability(1));
        assertTrue(member.getBorrowedBookList().isEmpty());
    }

//this will test the searching of books ID by searching its name
    @Test
    public void testFindBookIdByName() {
        library.addBook(book);
        assertEquals(1, library.findBookIdByName("Test Book"));
        assertEquals(-1, library.findBookIdByName("Nonexistent"));
    }

    //This will make sure when you check who has a book checked out is under the correct person
    @Test
    public void testWhoHasBook() {
        library.addBook(book);
        library.addMember(member);
        library.checkoutBook(1, 1);
        assertEquals("Test User", library.whoHasBook(1));
    }

    //This will do more setup for my second phase of tests to reach higher coverage
    @BeforeEach
    void setup() {
        library = new Library();
        book1 = new Book("Title One", "Author A", 2001, "ISBN1", 1, "Fiction");
        book2 = new Book("Title Two", "Author B", 2002, "ISBN2", 2, "Nonfiction");
        member1 = new Member("Alice", "alice@example.com", 101);

        library.addBook(book1);
        library.addBook(book2);
        library.addMember(member1);
    }

    //this test checks for removing a nonexistant book
    @Test
    void testRemoveNonexistentBook() {
        library.removeBook(999); // Should not throw
    }


    //this will check for removing a non existant member
    @Test
    void testRevokeNonexistentMember() {
        library.revokeMembership(999); // Should not throw
    }

    //this test will try and add a duplicate book and member to the library system
    @Test
    void testDuplicateAddBookAndMember() {
        Book duplicateBook = new Book("Duplicate", "Someone", 2020, "ISBN3", 1, "Drama");
        library.addBook(duplicateBook);
        library.addBook(duplicateBook);
        assertEquals("Duplicate", library.AllBooksInLibrary.get(1).Name);
        Member duplicateMember = new Member("Duplicate", "someone@example.com", 102);
        library.addMember(duplicateMember);
        library.addMember(duplicateMember);
        assertEquals("Duplicate", library.MemberIDs.get(102).Name);
    }
    //Library Class Property based testing
    //this will create a bunch of test books that I can use to test the functions handling checking in and out books
    //and the availability of different books within the library system
    @Provide
    Arbitrary<Book> books() {
        return Combinators.combine(
                Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20),
                Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20),
                Arbitraries.integers().between(1800, 2025),
                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(10),
                Arbitraries.integers().between(1, 10000),
                Arbitraries.of("Fiction", "Nonfiction", "Sci-fi", "Mystery")
        ).as(Book::new);
    }

    //This test will check the availability of all the books when they are added to the library.
    //Will function correct if they are all set to true
    @Property
    void checkAddingBooksMakesThemAvailable(@ForAll("books") Book book) {
        Library lib = new Library();
        lib.addBook(book);
        assertTrue(lib.bookAvailability(book.BookID));
    }

    //Testing when multiple books are checked out and then return the avaiability switches from, not available
    //to available after each action/return
    @Property
    void checkoutThenReturnRestoresAvailability(@ForAll("books") Book book, @ForAll("memberIDs") int id) {
        Library lib = new Library();
        lib.addBook(book);
        Member m = new Member("X", "x@test.com", id);
        lib.addMember(m);

        lib.checkoutBook(book.BookID, id);
        assertFalse(book.checkAvailability());

        lib.returnBook(book.BookID, id);
        assertTrue(book.checkAvailability());
    }

    //Provide an jqwik interface to generate member ID's for property tests
    @Provide
    Arbitrary<Integer> memberIDs() {
        return Arbitraries.integers().between(1000, 9999);
    }

}
