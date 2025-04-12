package delft;
import java.util.*;
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

        // Tests for Getters/Setters
        @Test
        public void getName_returnsCorrectName() {
            assertEquals("Hamlin", availableBook.getName());
        }

        @Test
        public void setName_updatesNameCorrectly() {
            availableBook.setName("New Title");
            assertEquals("New Title", availableBook.getName());
        }

        @Test
        public void getAuthor_returnsCorrectAuthor() {
            assertEquals("Amy Charish", availableBook.getAuthor());
        }

        @Test
        public void setAuthor_updatesAuthorCorrectly() {
            availableBook.setAuthor("New Author");
            assertEquals("New Author", availableBook.getAuthor());
        }

        @Test
        public void getIsbn_returnsCorrectIsbn() {
            assertEquals("12345", availableBook.getIsbn());
        }

        @Test
        public void setIsbn_updatesIsbnCorrectly() {
            availableBook.setIsbn("00000");
            assertEquals("00000", availableBook.getIsbn());
        }

        @Test
        public void getGenre_returnsCorrectGenre() {
            assertEquals("Fantasy", availableBook.getGenre());
        }

        @Test
        public void setGenre_updatesGenreCorrectly() {
            availableBook.setGenre("NonFiction");
            assertEquals("NonFiction", availableBook.getGenre());
        }

        @Test
        public void getID_returnsCorrectID() {
            assertEquals(65,availableBook.getBookId());
        }

        @Test
        public void setID_updatesIDCorrectly() {
            availableBook.setBookId(0);
            assertEquals(0, availableBook.getBookId());
        }


        @Test
        public void getYear_returnsCorrectYear() {
            assertEquals(2016, availableBook.getYear());
        }

        @Test
        public void setYear_updatesYearCorrectly() {
            availableBook.setYear(2023);
            assertEquals(2023, availableBook.getYear());
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

