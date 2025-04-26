package delft;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class LibrarianTest {

    private Librarian librarian;
    private LibraryAccount account;
    private Book book;



    @BeforeEach
    void setup(){
        librarian = new Librarian("Alice", 123456, "alice123", true);
        account = mock(LibraryAccount.class);
        book = new Book("The Giver", "Lois Lowry", 1993, "119922", 1, "Fiction");


    }

    // testing whether the librarian is full time, returns true if full time
    @Test
    void testFullTimeLibrarian(){
        assertTrue(librarian.isFullTime());
    }

    // tests the ability of the librarian to authenticate, should return false upon
    // invalid password and vice versa
   @Test
    void testAuthInvalidOrValidPassword(){
        assertFalse(librarian.authenticate("alice321"));
        assertTrue(librarian.authenticate("alice123"));
   }

    // testing functionality of a full time librarian to withdraw a salary, should return true
    // as long as librarian is full time and account has sufficient funds
   @Test
    void testWithdrawFullTime(){
        when(account.withdraw(1000)).thenReturn(true);
        librarian.withdrawSalary(1000, account);
        verify(account).withdraw(1000);
   }

    // testing functionality of a part time librarian to withdraw a salary,
    // should not allow withdrawal as part time librarians cannot withdraw salary
   @Test
   void testWithdrawPartTime(){
        Librarian partTimeLibrarian = new Librarian("jobe", 125634, "jobe125", false);
        when(account.withdraw(6900)).thenReturn(true);
        partTimeLibrarian.withdrawSalary(6900, account);
        verify(account, never()).withdraw(6900);
   }

    // verifying the ability of a librarian to record a book purchase
    // ensuring the book is added to the purchased books list
   @Test
    void testRecordPurchase(){
       librarian.recordPurchase(book);
       assertEquals(1, librarian.getPurchasedBooks().size());
       assertEquals(book, librarian.getPurchasedBooks().get(0));

   }

    // testing the ability of a full time librarian to withdraw a salary,
    // should return false when account has insufficient funds (bva)
   @Test
    void testWithdrawFullTimeInsufficientFunds(){
        when(account.withdraw(39000.1)).thenReturn(false);
        librarian.withdrawSalary(39000.1, account);
        verify(account).withdraw(39000.1);
   }


}
