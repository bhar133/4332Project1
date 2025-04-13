package delft;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.*;
//import net.jqwik.api.*;
//import net.jqwik.api.arbitraries.*;
//import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.list;
import static org.junit.jupiter.api.Assertions.*;


/*
 * Member Class Testing
 *
 */



public class MemberTest {

    private Member member;

    @BeforeEach
    public void setup(){
        member = new Member("First Last", "first@email.com", 1);
    }

    // ensures the borrowed book system is correctly tracking
    @Test
    void testGetBorrowedBookList(){
        member.addBorrowedBook(17);
        member.addBorrowedBook(14);
        member.addBorrowedBook(19);
        List<Integer> real = List.of(17,14,19);
        assertEquals(real, member.getBorrowedBookList());
    }

    // verifies that a borrowed book is added to the borrowed book list
    @Test
    void testAddBorrowedBook(){
        member.addBorrowedBook(15);
        member.addBorrowedBook(4);
        assertTrue(member.getBorrowedBookList().contains(15));
        assertTrue(member.getBorrowedBookList().contains(4));
        List<Integer> borrowedBooks = member.getBorrowedBookList();
        assertEquals(2, borrowedBooks.size());
        assertEquals(Integer.valueOf(15), borrowedBooks.get(0));
        assertEquals(Integer.valueOf(4), borrowedBooks.get(1));
    }

    // verifies that a borrowed book is removed from the borrowed book list
    @Test
    void testRemoveBorrowedBooks(){
        member.addBorrowedBook(20);
        member.removeBorrowedBook(20);
        assertFalse(member.getBorrowedBookList().contains(20));
    }

    // verifies that the removal of a non existent book is handled correctly
    @Test
    public void removeNonExistentBook() {
        member.removeBorrowedBook(999);
        assertTrue(member.getBorrowedBookList().isEmpty());
    }

    // verifies member information is correctly being updated
    @Test
    void testMemberInfo(){
        member.updateMemberInfo("John Stamos", "john@email.com");
        assertEquals("John Stamos", member.Name);
        assertEquals("john@email.com", member.Email);
    }

    // ensures member information isn't modified when updating with same values
    @Test
    void testDuplicateMemberInfoUpdate(){
        String dupeName = member.Name;
        String dupeEmail = member.Email;

        member.updateMemberInfo(dupeName, dupeEmail);
        assertEquals(dupeName, member.Name);
        assertEquals(dupeEmail, member.Email);
    }

    // tests the borrowing of large amounts of books with some removal
    @Test
    void testLargeNumberOfBorrowedBooks() {

        for (int i = 1; i <= 100; i++) {
            member.addBorrowedBook(i);
        }
        assertEquals(100, member.getBorrowedBookList().size());

        for (int i = 35; i <= 70; i++) {
            member.removeBorrowedBook(i);
        }
        assertEquals(64, member.getBorrowedBookList().size());
    }

    // adding a book then removing it, should always return an empty list
    @Property
    void borrowedBookListShouldBeEmptyAfterAddAndRemove(
            @ForAll @IntRange(min = 1, max = 2000) int bookID) {

        Member member = new Member("Real", "real@email.com", 6);
        member.addBorrowedBook(bookID);
        member.removeBorrowedBook(bookID);
        assertTrue(member.getBorrowedBookList().isEmpty());
    }

    //  removing a non existent book id should leave the list unmodified
    @Property
    void removingNonexistentBookShouldNotModifyList(
            @ForAll("borrowedBooks") List<@IntRange(min = 1, max = 1000) Integer> bookList,
            @ForAll @IntRange(min = 1001, max = 2000) int missingBookID) {

        Member member = new Member("Test", "test@email.com", 27);
        bookList.forEach(member::addBorrowedBook);

        List<Integer> oldList = new ArrayList<>(member.getBorrowedBookList());
        member.removeBorrowedBook(missingBookID);

        assertEquals(oldList, member.getBorrowedBookList());
    }

    // random borrowed book list
    @Provide
    Arbitrary<List<Integer>> borrowedBooks() {
        return Arbitraries.integers()
                .between(1, 1000)
                .list()
                .ofMinSize(0)
                .ofMaxSize(25);
    }

    // ensures borrowed books always maintain checkout order
    @Property
    void borrowedBookListShouldMaintainOrder(
            @ForAll("BookIDs") List<Integer> bookIDs) {
        Member member = new Member("First", "fest@email.com", 1);
        bookIDs.forEach(member::addBorrowedBook);
        assertEquals(bookIDs, member.getBorrowedBookList());
    }

    // generates book ids
    @Provide
    Arbitrary<List<Integer>> BookIDs() {
        return Arbitraries.integers()
                .between(1, 200)
                .list()
                .ofMinSize(1)
                .ofMaxSize(50);
    }
}
