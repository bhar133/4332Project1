package delft;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LibraryAccountTest {

    private LibraryAccount account;
    private Library mockLibrary;
    private Librarian mockLibrarian;
    private Book mockBook;
    private Purchasing mockPurchasing;

    @BeforeEach
    void setUp() {
        mockPurchasing = mock(Purchasing.class);
        account = new LibraryAccount(mockPurchasing);
        mockLibrary = mock(Library.class);
        mockLibrarian = mock(Librarian.class);
        mockBook = mock(Book.class);
    }

    // Testing that a new account starts with the correct initial balance
    @Test
    void initialBalanceIsCorrect() {
        assertEquals(39000.0, account.getBalance());
    }

    // Testing that depositing a positive amount increases the balance correctly
    @Test
    void depositPositiveAmount() {
        account.deposit(1000.0);
        assertEquals(40000.0, account.getBalance());
    }

    // Testing that depositing zero leaves the balance unchanged
    @Test
    void depositZeroDoesNothing() {
        account.deposit(0);
        assertEquals(39000.0, account.getBalance());
    }

    // Testing that depositing a negative amount leaves the balance unchanged
    @Test
    void depositNegativeDoesNothing() {
        account.deposit(-1000.0);
        assertEquals(39000.0, account.getBalance());
    }

    // Testing that withdrawing with sufficient funds succeeds and updates balance
    @Test
    void withdrawWithEnoughMoney() {
        assertTrue(account.withdraw(10000.0));
        assertEquals(29000.0, account.getBalance());
    }

    // Testing that withdrawing with insufficient funds fails and leaves balance unchanged
    @Test
    void withdrawWithNotEnoughMoney() {
        assertFalse(account.withdraw(40000.0));
        assertEquals(39000.0, account.getBalance());
    }

    // Testing that withdrawing zero fails and leaves balance unchanged
    @Test
    void withdrawZeroFails() {
        assertFalse(account.withdraw(0));
        assertEquals(39000.0, account.getBalance());
    }

    // Testing that withdrawing a negative amount fails and leaves balance unchanged
    @Test
    void withdrawNegativeFails() {
        assertFalse(account.withdraw(-1000.0));
        assertEquals(39000.0, account.getBalance());
    }

    // Testing that ordering with sufficient funds succeeds, updates balance, adds book, and records purchase
    @Test
    void orderBookWithFunds() {
        when(mockPurchasing.purchaseBook()).thenReturn(100.0);

        assertTrue(account.orderBook(mockLibrary, mockLibrarian, mockBook));
        assertEquals(38900.0, account.getBalance());

        verify(mockLibrary).addBook(mockBook);
        verify(mockLibrarian).recordPurchase(mockBook);
    }

    // Testing that ordering without sufficient funds fails, leaves balance unchanged, and doesn't add book, doesn't record purchase
    @Test
    void orderBookWithoutFunds() {
        when(mockPurchasing.purchaseBook()).thenReturn(40000.0);

        assertFalse(account.orderBook(mockLibrary, mockLibrarian, mockBook));
        assertEquals(39000.0, account.getBalance());

        verify(mockLibrary, never()).addBook(any());
        verify(mockLibrarian, never()).recordPurchase(any());
    }

    // Testing that ordering with a negative cost fails, leaves balance unchanged, and doesn't add book, doesn't record purchase
    @Test
    void orderBookWithNegativeCost() {
        when(mockPurchasing.purchaseBook()).thenReturn(-1000.0); // Invalid negative cost

        assertFalse(account.orderBook(mockLibrary, mockLibrarian, mockBook));
        assertEquals(39000.0, account.getBalance());

        verify(mockLibrary, never()).addBook(any());
        verify(mockLibrarian, never()).recordPurchase(any());
    }
}