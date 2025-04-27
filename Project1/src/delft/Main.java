package delft;
import java.util.*;

/*
 * Library Management System
 * Simulates a basic library system with functionality to manage books and members.
 *
 *
 *
 *
 */

public class Main {
    public static void main(String[] args) {
        Interface.start();
    }
}
class Book {
    String Name, Author, ISBN, Genre;
    int Year, BookID;
    boolean IsAvailable = true;

    public Book(String name, String author, int year, String isbn, int id, String genre) {
        Name = name;
        Author = author;
        Year = year;
        ISBN = isbn;
        BookID = id;
        Genre = genre;
    }

    // checks if the book is available
    // returns true if available, false if not
    public boolean checkAvailability() {
        return IsAvailable;
    }

    // updates the book information
    public void updateBookInfo(String name, String author, int year, String isbn, String genre) {
        Name = name;
        Author = author;
        Year = year;
        ISBN = isbn;
        Genre = genre;
    }

    // returns  book information
    public String getBookInfo() {
        return "[" + BookID + "] " + Name + " by " + Author + ", " + Year + ", Genre: " + Genre +
                (IsAvailable ? " (Available)" : " (Checked Out)");
    }
}

// Member class where you can access and see member info
class Member {
    String Name, Email;
    int MemberID;
    List<Integer> BorrowedBookList = new ArrayList<>();

    // member constructor, initializes member info with name, email, and ID
    public Member(String name, String email, int id) {
        Name = name;
        Email = email;
        MemberID = id;
    }

    // displays member information
    public void printMemberInfo() {
        System.out.println("[" + MemberID + "] " + Name + " (" + Email + ")");
    }

    // returns the list of borrowed books
    public List<Integer> getBorrowedBookList() {
        return BorrowedBookList;
    }

    // adds a book to a member's borrowed list
    public void addBorrowedBook(int bookID) {
        BorrowedBookList.add(bookID);
    }

    // removes a book from a member's borrowed list
    public void removeBorrowedBook(int bookID) {
        BorrowedBookList.remove(Integer.valueOf(bookID));
    }

    // updates member's information, name and email
    public void updateMemberInfo(String name, String email) {
        Name = name;
        Email = email;
    }
}

// LibraryAccount class
class LibraryAccount {
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

// Purchasing class
class Purchasing {
    public double purchaseBook() {
        Random rand = new Random();
        return 10 + rand.nextInt(91); // $10 to $100
    }
}

// Librarian class
class Librarian {
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

// Library class where you can find the books and member access is
class Library {
    Map<Integer, Book> AllBooksInLibrary = new HashMap<>();
    Map<Integer, Book> LoanedBooks = new HashMap<>();
    Map<Integer, Member> MemberIDs = new HashMap<>();
    Set<Integer> AvailableBookIds = new HashSet<>();
    Purchasing purchasing = new Purchasing();
    LibraryAccount account = new LibraryAccount(purchasing);
    List<Librarian> librarians = new ArrayList<>();

    //Pre-defined librarians
    public Library() {
        librarians.add(new Librarian("Alice", 123456, "alice123", true));
        librarians.add(new Librarian("Bob", 234567, "bob234", true));
        librarians.add(new Librarian("Cathy", 345678, "cathy345", true));
        librarians.add(new Librarian("Seth", 456789, "seth456", false));
    }

    public LibraryAccount getAccount() {
        return account;
    }

    public Librarian authenticateLibrarian(int id, String password) {
        for (Librarian lib : librarians) {
            if (lib.getId() == id && lib.authenticate(password)) {
                return lib;
            }
        }
        return null;
    }

    // adds a book to the library
    public void addBook(Book b) {
        AllBooksInLibrary.put(b.BookID, b);
        AvailableBookIds.add(b.BookID);
    }

    // removes a book from the library
    public void removeBook(int bookID) {
        if (!AllBooksInLibrary.containsKey(bookID)) {
            System.out.println("Book ID not found.");
            return;
        }

        AllBooksInLibrary.remove(bookID);
        AvailableBookIds.remove(bookID);
        LoanedBooks.remove(bookID);
    }

    // checks out a book to a member
    // updates the book's availability status and adds it to the member's borrowed list
    public void checkoutBook(int bookID, int memberID) {
        if (!AllBooksInLibrary.containsKey(bookID)) {
            System.out.println("Book ID not found in library.");
            return;
        }
        if (!MemberIDs.containsKey(memberID)) {
            System.out.println("Member ID not found.");
            return;
        }
        Book book = AllBooksInLibrary.get(bookID);
        Member member = MemberIDs.get(memberID);
        if (!book.IsAvailable) {
            System.out.println("Book is already checked out.");
            return;
        }
        book.IsAvailable = false;
        AvailableBookIds.remove(bookID);
        LoanedBooks.put(bookID, book);
        member.addBorrowedBook(bookID);
        System.out.println("Book checked out successfully.");
    }

    // returns a book from a member
    // updates the book's availability status and removes it from the member's borrowed list
    public void returnBook(int bookID, int memberID) {
        if (!AllBooksInLibrary.containsKey(bookID)) {
            System.out.println("Book ID not found in library.");
            return;
        }
        if (!MemberIDs.containsKey(memberID)) {
            System.out.println("Member ID not found.");
            return;
        }
        Book book = AllBooksInLibrary.get(bookID);
        Member member = MemberIDs.get(memberID);
        if (book.IsAvailable) {
            System.out.println("Book is already returned.");
            return;
        }
        book.IsAvailable = true;
        AvailableBookIds.add(bookID);
        LoanedBooks.remove(bookID);
        member.removeBorrowedBook(bookID);
        System.out.println("Book returned successfully.");
    }

    // adds a member to the library
    public void addMember(Member m) {
        MemberIDs.put(m.MemberID, m);
    }

    // revokes a member's membership
    public void revokeMembership(int memberID) {
        MemberIDs.remove(memberID);
    }

    // checks if a book is available
    public boolean bookAvailability(int bookID) {
        return AvailableBookIds.contains(bookID);
    }

    // check who has a book and returns the member's name
    public String whoHasBook(int bookID) {
        for (Member m : MemberIDs.values()) {
            if (m.BorrowedBookList.contains(bookID)) {
                return m.Name;
            }
        }
        return "None";
    }

    // returns a list of all members in the library
    public List<Member> getAllMembers() {
        return new ArrayList<>(MemberIDs.values());
    }

    // finds a book ID by its name
    public int findBookIdByName(String name) {
        for (Book b : AllBooksInLibrary.values()) {
            if (b.Name.equalsIgnoreCase(name)) return b.BookID;
        }
        return -1;
    }

    public Book getBookById(int bookID) {
        return AllBooksInLibrary.get(bookID);
    }
}


class Interface {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        Librarian currentLibrarian = null;
        while (currentLibrarian == null) {
            System.out.println("\n--- Login as Librarian ---");
            System.out.print("Librarian ID: ");
            int id = getIntInput(scanner);

            System.out.print("Password: ");
            String password = scanner.nextLine();

            currentLibrarian = library.authenticateLibrarian(id, password);
            if (currentLibrarian == null) {
                System.out.println("Authentication failed. Try again.");
            } else {
                System.out.println("\nLogin successful. Welcome, " + currentLibrarian.getName() + "!");
            }
        }

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Book Options");
            System.out.println("2. Member Options");
            System.out.println("3. Librarian Options");
            System.out.println("4. Account Options");
            System.out.println("5. Exit");
            System.out.print("Select a category: ");

            int mainChoice = getIntInput(scanner);

            switch (mainChoice) {
                case 1 -> handleBookOptions(scanner, library);
                case 2 -> handleMemberOptions(scanner, library);
                case 3 -> handleLibrarianOptions(scanner, library, currentLibrarian);
                case 4 -> {
                    if (!currentLibrarian.isFullTime()) {
                        System.out.println("Permission denied. Only full-time librarians can access account options.");
                    } else {
                        handleAccountOptions(scanner, library, currentLibrarian);
                    }
                }

                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid menu option.");
            }
        }
    }

    // --- Handle Book Options ---
    private static void handleBookOptions(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- Book Options ---");
            System.out.println("1. Check Availability");
            System.out.println("2. Update Book Info");
            System.out.println("3. Get Book Info");
            System.out.println("0. Return to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput(scanner);

            if (choice == 0) return;

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Book ID: ");
                    Book b = library.getBookById(getIntInput(scanner));
                    if (b != null) {
                        System.out.println("Available: " + b.checkAvailability());
                    } else {
                        System.out.println("Book not found.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Book ID: ");
                    Book b = library.getBookById(getIntInput(scanner));
                    if (b != null) {
                        System.out.print("New Name: ");
                        b.Name = scanner.nextLine();
                        System.out.print("New Author: ");
                        b.Author = scanner.nextLine();
                        System.out.print("New Year: ");
                        b.Year = getIntInput(scanner);
                        System.out.print("New ISBN: ");
                        b.ISBN = scanner.nextLine();
                        System.out.print("New Genre: ");
                        b.Genre = scanner.nextLine();
                        b.updateBookInfo(b.Name, b.Author, b.Year, b.ISBN, b.Genre);
                    } else {
                        System.out.println("Book not found.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Book ID: ");
                    Book b = library.getBookById(getIntInput(scanner));
                    if (b != null) {
                        System.out.println(b.getBookInfo());
                    } else {
                        System.out.println("Book not found.");
                    }
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- Handle Member Options ---
    private static void handleMemberOptions(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- Member Options ---");
            System.out.println("1. Print Member Info");
            System.out.println("2. Get Borrowed Book List");
            System.out.println("3. Add Borrowed Book");
            System.out.println("4. Remove Borrowed Book");
            System.out.println("5. Update Member Info");
            System.out.println("0. Return to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput(scanner);

            if (choice == 0) return;

            System.out.print("Enter Member ID: ");
            Member member = library.MemberIDs.get(getIntInput(scanner));
            if (member == null) {
                System.out.println("Member not found.");
                continue;
            }

            switch (choice) {
                case 1 -> member.printMemberInfo();
                case 2 -> System.out.println("Borrowed Books: " + member.getBorrowedBookList());
                case 3 -> {
                    System.out.print("Enter Book ID to add: ");
                    member.addBorrowedBook(getIntInput(scanner));
                }
                case 4 -> {
                    System.out.print("Enter Book ID to remove: ");
                    member.removeBorrowedBook(getIntInput(scanner));
                }
                case 5 -> {
                    System.out.print("New Name: ");
                    member.Name = scanner.nextLine();
                    System.out.print("New Email: ");
                    member.Email = scanner.nextLine();
                    member.updateMemberInfo(member.Name, member.Email);
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- Handle Librarian Options (Library management like add/remove books, checkout, etc.) ---
    private static void handleLibrarianOptions(Scanner scanner, Library library, Librarian librarian) {
        while (true) {
            System.out.println("\n--- Librarian Options ---");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Add Member");
            System.out.println("4. Revoke Membership");
            System.out.println("5. Checkout Book");
            System.out.println("6. Return Book");
            System.out.println("7. Check Book Availability");
            System.out.println("8. Who Has Book");
            System.out.println("9. Get All Members");
            System.out.println("10. Find Book ID by Name");
            System.out.println("0. Return to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput(scanner);

            if (choice == 0) return;

            switch (choice) {
                case 1 -> {

                    System.out.print("Enter Book ID: ");
                    int id = getIntInput(scanner);
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Year: ");
                    int year = getIntInput(scanner);
                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();
                    Book b = new Book(name, author, year, isbn, id, genre);
                    library.addBook(b);
                    System.out.println("Book added successfully.");
                }
                case 2 -> {
                    if (!librarian.isFullTime()) {
                        System.out.println("Permission denied. Only full-time librarians can remove books.");
                        break;
                    }
                    System.out.print("Enter Book ID to remove: ");
                    library.removeBook(getIntInput(scanner));
                    System.out.println("Book removed successfully.");
                }
                case 3 -> {
                    System.out.print("Member Name: ");
                    String memberName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Member ID: ");
                    int memberID = getIntInput(scanner);
                    library.addMember(new Member(memberName, email, memberID));
                    System.out.println("Member added successfully.");
                }
                case 4 -> {
                    if (!librarian.isFullTime()) {
                        System.out.println("Only full-time librarians can revoke memberships.");
                        break;
                    }
                    System.out.print("Enter Member ID to revoke: ");
                    library.revokeMembership(getIntInput(scanner));
                }
                case 5 -> {
                    System.out.print("Enter Book ID to checkout: ");
                    int bookID = getIntInput(scanner);
                    System.out.print("Enter Member ID: ");
                    int memberID = getIntInput(scanner);
                    Book book = library.getBookById(bookID);
                    if (book == null) {
                        if (librarian.isFullTime()) {
                            System.out.println("Book not found. Would you like to purchase it? (yes/no)");
                            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                                System.out.print("Name: ");
                                String name = scanner.nextLine();
                                System.out.print("Author: ");
                                String author = scanner.nextLine();
                                System.out.print("Year: ");
                                int year = getIntInput(scanner);
                                System.out.print("ISBN: ");
                                String isbn = scanner.nextLine();
                                System.out.print("Genre: ");
                                String genre = scanner.nextLine();
                                book = new Book(name, author, year, isbn, bookID, genre);
                                if (library.getAccount().orderBook(library, librarian, book)) {
                                    library.addBook(book);
                                    System.out.println("Book purchased and added.");
                                } else {
                                    System.out.println("Purchase failed.");
                                    break;
                                }
                            } else {
                                System.out.println("Checkout cancelled.");
                                break;
                            }
                        } else {
                            System.out.println("Please request a full-time librarian.");
                            break;
                        }
                    }
                    library.checkoutBook(bookID, memberID);
                }
                case 6 -> {
                    System.out.print("Enter Book ID to return: ");
                    int returnBookID = getIntInput(scanner);
                    System.out.print("Enter Member ID: ");
                    int returnMemberID = getIntInput(scanner);
                    library.returnBook(returnBookID, returnMemberID);

                }
                case 7 -> {
                    System.out.print("Enter Book ID to check availability: ");
                    System.out.println("Available: " + library.bookAvailability(getIntInput(scanner)));
                }
                case 8 -> {
                    System.out.print("Enter Book ID to check who has it: ");
                    System.out.println("Checked out by: " + library.whoHasBook(getIntInput(scanner)));
                }
                case 9 -> library.getAllMembers().forEach(Member::printMemberInfo);
                case 10 -> {
                    System.out.print("Enter Book Name: ");
                    System.out.println("Book ID: " + library.findBookIdByName(scanner.nextLine()));
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- Handle Account Options ---
    private static void handleAccountOptions(Scanner scanner, Library library, Librarian librarian) {
        while (true) {
            System.out.println("\n--- Account Options ---");
            System.out.println("1. Add Cash (Deposit)");
            System.out.println("2. Withdraw Salary");
            System.out.println("3. View Total Withdrawn Salaries");
            System.out.println("4. View Books Purchased by Librarian");
            System.out.println("0. Return to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 0 -> { return; }
                case 1 -> {
                    System.out.print("Enter donation amount: ");
                    double depositAmount = getDoubleInput(scanner);
                    library.getAccount().deposit(depositAmount);
                    System.out.println("Deposited $" + depositAmount);
                    System.out.println("Current Library Balance: $" + library.getAccount().getBalance());
                }
                case 2 -> {
                    System.out.print("Enter salary amount to withdraw: ");
                    double salaryAmount = getDoubleInput(scanner);
                    if (library.getAccount().withdraw(salaryAmount)) {
                        librarian.addSalaryWithdrawn(salaryAmount);
                        System.out.println("Withdrew $" + salaryAmount + " for salary.");
                        System.out.println("Current Library Balance: $" + library.getAccount().getBalance());
                    } else {
                        System.out.println("Insufficient funds for withdrawal.");
                    }
                }
                case 3 -> System.out.println("Total Salary Withdrawn: $" + librarian.getTotalWithdrawnSalary());
                case 4 -> {
                    System.out.println("Books Purchased:");
                    for (Book book : librarian.getPurchasedBooks()) {
                        System.out.println(book.getBookInfo());
                    }
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- Helper input methods ---
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    private static double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid amount. Try again: ");
            }
        }
    }
}