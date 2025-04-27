package delft;

public class LibraryAccount {
    private double balance;
    private Purchasing purchasing;

    //setting the balance and constructing the purchasing
    public LibraryAccount(Purchasing purchasing) {
        this.balance = 39000.0;
        this.purchasing = purchasing;
    }

    //returns balance of library
    public double getBalance() {
        return balance;
    }

    //adds money to library balance
    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    //the actual process withdrawing from library system when called
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    //will purchase the book or return nothing because the library is out of funds
    public boolean orderBook(Library library, Librarian librarian, Book book) {
        double cost = purchasing.purchaseBook();
        if (withdraw(cost)) {
            library.addBook(book);
            librarian.recordPurchase(book);
            System.out.println("Book purchased for $" + cost + " and added to library.");
            return true;
        } else {
            System.out.println("Insufficient funds to purchase book.");
            return false;
        }
    }
}
