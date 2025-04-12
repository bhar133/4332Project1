package delft;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.*;
//import net.jqwik.api.*;
//import net.jqwik.api.arbitraries.*;
//import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryManagementSystemTest {

    //Library Class Specification Based Testing
    Library library;
    Book book;
    Member member;

    @BeforeEach
    public void setUp() {
        library = new Library();
        book = new Book("Test Book", "Test Author", 2024, "123456789", 1, "Fiction");
        member = new Member("Test User", "test@example.com", 1);
    }

    @Test
    public void testAddBookAndCheckAvailability() {
        library.addBook(book);
        assertTrue(library.bookAvailability(1));
    }

    @Test
    public void testAddAndRevokeMember() {
        library.addMember(member);
        assertEquals(1, library.getAllMembers().size());
        library.revokeMembership(1);
        assertEquals(0, library.getAllMembers().size());
    }

    @Test
    public void testCheckoutAndReturnBook() {
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

    @Test
    public void testFindBookIdByName() {
        library.addBook(book);
        assertEquals(1, library.findBookIdByName("Test Book"));
        assertEquals(-1, library.findBookIdByName("Nonexistent"));
    }

    @Test
    public void testWhoHasBook() {
        library.addBook(book);
        library.addMember(member);
        library.checkoutBook(1, 1);
        assertEquals("Test User", library.whoHasBook(1));
    }

    //Library Class Property based testing
//    @Provide
//    Arbitrary<Book> books() {
//        return Combinators.combine(
//                Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20),
//                Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20),
//                Arbitraries.integers().between(1800, 2025),
//                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(10),
//                Arbitraries.integers().between(1, 10000),
//                Arbitraries.of("Fiction", "Nonfiction", "Sci-fi", "Mystery")
//        ).as(Book::new);
//    }
//
//    @Property
//    void checkAddingBooksMakesThemAvailable(@ForAll("books") Book book) {
//        Library lib = new Library();
//        lib.addBook(book);
//        assertTrue(lib.bookAvailability(book.BookID));
//    }
//
//    @Property
//    void checkoutThenReturnRestoresAvailability(@ForAll("books") Book book, @ForAll("memberIDs") int id) {
//        Library lib = new Library();
//        lib.addBook(book);
//        Member m = new Member("X", "x@test.com", id);
//        lib.addMember(m);
//
//        lib.checkoutBook(book.BookID, id);
//        assertFalse(book.checkAvailability());
//
//        lib.returnBook(book.BookID, id);
//        assertTrue(book.checkAvailability());
//    }
//
//    @Provide
//    Arbitrary<Integer> memberIDs() {
//        return Arbitraries.integers().between(1000, 9999);
//    }

}
