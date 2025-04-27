package delft;

import java.util.ArrayList;
import java.util.List;

public class Librarian {
    private String name;
    private int id;
    private String password;
    private boolean fullTime;
    private double totalWithdrawnSalary = 0.0;
    private List<Book> purchasedBooks;

    public Librarian(String name, int id, String password, boolean fullTime) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.fullTime = fullTime;
        this.purchasedBooks = new ArrayList<>();
    }

    // adds the amount to the total withdrawn salary
    public void addSalaryWithdrawn(double amount) {
        totalWithdrawnSalary += amount;
    }

    // returns the total salary withdrawn by the librarian
    public double getTotalWithdrawnSalary() {
        return totalWithdrawnSalary;
    }

    // returns the librarians name
    public String getName() {
        return name;
    }

    // returns the librarians id
    public int getId() {
        return id;
    }

    // returns whether the librarian is full or part time
    public boolean isFullTime() {
        return fullTime;
    }

    // method to check if the librarian is full-time
    public boolean authenticate(String inputPassword) {
        return password.equals(inputPassword);
    }

    // method to add salary withdrawn
    // checks if the librarian is full-time and withdraws the salary
    public void withdrawSalary(double amount, LibraryAccount account) {
        if (fullTime) {
            if (account.withdraw(amount)) {
                System.out.println("Withdrew $" + amount + " from library account.");
            } else {
                System.out.println("Failed to withdraw. Insufficient funds.");
            }
        } else {
            System.out.println("Part-time librarians cannot withdraw salary.");
        }
    }

    // records the book purchased by the librarian
    // adds it to the purchasedBooks list
    public void recordPurchase(Book book) {
        purchasedBooks.add(book);
    }

    // returns the list of books purchased by the librarian
    public List<Book> getPurchasedBooks() {
        return purchasedBooks;
    }
}
