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
            unavailableBook.setAvailability(false);
        }

        // Tests for Core Functionality
        @Test
        public void checkAvailability_returnsTrue_whenBookIsAvailable() {
            assertTrue(availableBook.checkAvailability());
        }

        @Test
        public void checkAvailability_returnsFalse_whenBookIsNotAvailable() {
            assertFalse(unavailableBook.checkAvailability());
        }

        @Test
        public void getBookInfo_showsCorrectFormatForAvailableBook() {
            assertEquals(availableBook.getBookInfo(),"[65] Hamlin by Amy Charish, 2016, Genre: Fantasy (Available)");
        }

        @Test
        public void getBookInfo_showsCorrectFormatForUnavailableBook() {
            assertEquals(unavailableBook.getBookInfo(),"[65] Hamlin by Amy Charish, 2016, Genre: Fantasy (Checked Out)");
        }

        @Test
        public void updateBookInfo_updatesAllFieldsCorrectly() {
            Book book = new Book("OldName", "OldAuthor", 2000, "OldISBN", 1, "OldGenre");
            book.updateBookInfo("NewName", "NewAuthor", 2023, "NewISBN", "NewGenre");

            assertEquals("NewName", book.getName());
            assertEquals("NewAuthor", book.getAuthor());
            assertEquals(2023, book.getYear());
            assertEquals("NewISBN", book.getIsbn());
            assertEquals("NewGenre", book.getGenre());
        }
        Book book = new Book("Default", "Author", 2000, "ISBN", 1, "Genre");

        // Tests for Getters/Setters
        @Property
        void allStringPropertiesRoundtrip(

                @ForAll @StringLength(min = 1, max = 100) String name,
                @ForAll @StringLength(min = 1, max = 100) String author,
                @ForAll @StringLength(min = 1, max = 20) String isbn,
                @ForAll @StringLength(min = 1, max = 50) String genre) {

            book.setName(name);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setGenre(genre);

            assertAll(
                    () -> assertEquals(name, book.getName()),
                    () -> assertEquals(author, book.getAuthor()),
                    () -> assertEquals(isbn, book.getIsbn()),
                    () -> assertEquals(genre, book.getGenre())
            );
        }

        @Property
        void numericPropertiesRoundtrip(
                @ForAll @IntRange(min = 0, max = 3000) int year,
                @ForAll @Positive int bookId) {

            book.setYear(year);
            book.setBookId(bookId);

            assertAll(
                    () -> assertEquals(year, book.getYear()),
                    () -> assertEquals(bookId, book.getBookId())
            );
        }

        @Test
        public void isAvailable_returnsCorrectStatus() {
            assertTrue(availableBook.isAvailable());
            assertFalse(unavailableBook.isAvailable());
        }

        @Test
        public void setAvailable_updatesStatusCorrectly() {
            availableBook.setAvailability(false);
            assertFalse(availableBook.isAvailable());

            unavailableBook.setAvailability(true);
            assertTrue(unavailableBook.isAvailable());
        }
    }

