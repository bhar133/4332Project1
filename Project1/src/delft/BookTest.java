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

import static org.junit.jupiter.api.Assertions.*;

    public class BookTest {
        private Book availableBook;
        private Book unavailableBook;


        @BeforeEach
        public void setUp() {
            availableBook = new Book("Hamlin", "Amy Charish", 2016, "12345", 65, "Fantasy");
            unavailableBook = new Book("Hamlin", "Amy Charish", 2016, "12345", 65, "Fantasy");
            unavailableBook.IsAvailable = false;
        }

        // returns true when book is availible
        @Test
        public void checkAvailabilityIsTrue() {
            assertTrue(availableBook.checkAvailability());
        }
        // returns false when book is checked out
        @Test
        public void checkAvailabilityIsFalse() {
            assertFalse(unavailableBook.checkAvailability());
        }

        //adds books, uses getBookInfo , and verfies correct info is retrived
        @Property
        void getBookInfoTest(
                @ForAll @StringLength(min = 1, max = 50) String title,
                @ForAll @StringLength(min = 1, max = 50) String author,
                @ForAll @IntRange(min = 0, max = 3000) int year,
                @ForAll @StringLength(min = 1, max = 20) String isbn,
                @ForAll @StringLength(min = 1, max = 30) String genre,
                @ForAll int bookId,
                @ForAll boolean available) {

            Book book = new Book(title, author, year, isbn, bookId, genre);
            book.IsAvailable = available;

            String info = book.getBookInfo();

            // Verify the pattern matches
            assertEquals(info,String.format("[%d] %s by %s, %d, Genre: %s (%s)",
                    bookId,
                    title,
                    author,
                    year,
                    genre,
                    available ? "Available" : "Checked Out"));


        }

        //updates book info with new info and verifies new info
        @Property
        void updateBookInfoTest(
                @ForAll @StringLength(min = 1) String newName,
                @ForAll @StringLength(min = 1) String newAuthor,
                @ForAll @IntRange(min = -3000, max = 3000) int newYear,
                @ForAll @StringLength(min = 1) String newIsbn,
                @ForAll @StringLength(min = 1) String newGenre) {

            Book book = new Book("Old", "Old", 0, "Old", 1, "Old");
            book.updateBookInfo(newName, newAuthor, newYear, newIsbn, newGenre);

            assertAll(
                    () -> assertEquals(newName, book.Name),
                    () -> assertEquals(newAuthor, book.Author),
                    () -> assertEquals(newYear, book.Year),
                    () -> assertEquals(newIsbn, book.ISBN),
                    () -> assertEquals(newGenre, book.Genre)
            );
        }

    }

