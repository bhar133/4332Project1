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
    private boolean fullTime; // NEW: true if full-time, false if part-time
    private List<Book> purchasedBooks;

    public Librarian(String name, int id, String password, boolean fullTime) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.fullTime = fullTime;
        this.purchasedBooks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public boolean authenticate(String inputPassword) {
        return password.equals(inputPassword);
    }

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

    public void recordPurchase(Book book) {
        purchasedBooks.add(book);
    }

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

// CLI for the library system
class Interface {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        Librarian currentLibrarian = null;
        while (currentLibrarian == null) {
            System.out.println("\n--- Login as Librarian ---");

            System.out.print("Librarian ID: ");
            int id;
            try {
                id = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a 6-digit number.");
                continue;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();

            currentLibrarian = library.authenticateLibrarian(id, password);
            if (currentLibrarian == null) {
                System.out.println("Authentication failed. Try again.");


                System.out.println("\nLogin successful. Welcome, " + currentLibrarian.getName() + "!");
            }
        }

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Library");
            System.out.println("2. Book");
            System.out.println("3. Member");
            System.out.println("4. Librarian Options");
            System.out.println("5. Exit");
            System.out.print("Select a category: ");

            int mainChoice = getIntInput(scanner);

            switch (mainChoice) {
                case 1:
                    handleLibraryOptions(scanner, library);
                    break;
                case 2:
                    handleBookOptions(scanner, library);
                    break;
                case 3:
                    handleMemberOptions(scanner, library);
                    break;
                case 4:
                    handleLibrarianOptions(scanner, library, currentLibrarian);
                    break;
                case 5:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid menu option.");
            }
        }
    }

    private static void handleLibrarianOptions(Scanner scanner, Library library, Librarian librarian) {
            while (true) {
                System.out.println("\n--- Library Options ---");
                System.out.println("1. Remove Book");
                System.out.println("2. Add Member");
                System.out.println("3. Revoke Membership");
                System.out.println("4. Checkout Book");
                System.out.println("5. Return Book");
                System.out.println("6. Check Book Availability");
                System.out.println("7. Who Has Book");
                System.out.println("8. Get All Members");
                System.out.println("9. Find Book ID by Name");
                System.out.println("0. Return to Main Menu");
                System.out.print("Select an option: ");

                int libChoice = getIntInput(scanner);

                switch (libChoice) {
                    case 0:
                        return;
                    case 1:
                        if (!librarian.isFullTime()) {
                            System.out.println("Permission denied.");
                            break;
                        }
                        System.out.print("Enter Book ID to remove: ");
                        library.removeBook(Integer.parseInt(scanner.nextLine()));
                        break;
                    case 2:
                        System.out.print("Member name: ");
                        String memberName = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Member ID: ");
                        int memberID = Integer.parseInt(scanner.nextLine());
                        library.addMember(new Member(memberName, email, memberID));
                        break;
                    case 3:
                        if (!librarian.isFullTime()) {
                            System.out.println("Only full-time librarians can revoke memberships.");
                            break;
                        }
                        System.out.print("Enter Member ID to revoke: ");
                        library.revokeMembership(Integer.parseInt(scanner.nextLine()));
                        break;
                    case 4:
                        System.out.print("Book ID to checkout: ");
                        int checkoutBookID = Integer.parseInt(scanner.nextLine());
                        System.out.print("Member ID: ");
                        int checkoutMemberID = Integer.parseInt(scanner.nextLine());
                        Book book = library.getBookById(checkoutBookID);
                        if (book == null) {
                            if (librarian.isFullTime()) {
                                System.out.println("Book not found in library. Would you like to purchase and proceed with checkout? (yes/no)");
                                if (scanner.nextLine().equalsIgnoreCase("yes")) {
                                    System.out.print("Book name: ");
                                    String name = scanner.nextLine();
                                    System.out.print("Author: ");
                                    String author = scanner.nextLine();
                                    System.out.print("Year: ");
                                    int year = Integer.parseInt(scanner.nextLine());
                                    System.out.print("ISBN: ");
                                    String isbn = scanner.nextLine();
                                    System.out.print("Genre: ");
                                    String genre = scanner.nextLine();
                                    book = new Book(name, author, year, isbn, checkoutBookID, genre);
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
                                System.out.println("Please request a full-time librarian to approve the purchase.");
                                break;
                            }
                        }
                        library.checkoutBook(checkoutBookID, checkoutMemberID);
                        break;
                    case 5:
                        System.out.print("Book ID to return: ");
                        int returnBookID = Integer.parseInt(scanner.nextLine());
                        System.out.print("Member ID: ");
                        int checkinMemberID = Integer.parseInt(scanner.nextLine());
                        library.returnBook(returnBookID, checkinMemberID);
                        break;
                    case 6:
                        System.out.print("Enter Book ID to check availability: ");
                        System.out.println("Available: " + library.bookAvailability(Integer.parseInt(scanner.nextLine())));
                        break;
                    case 7:
                        System.out.print("Enter Book ID to check who has it: ");
                        System.out.println("Checked out by: " + library.whoHasBook(Integer.parseInt(scanner.nextLine())));
                        break;
                    case 8:
                        for (Member m : library.getAllMembers()) {
                            m.printMemberInfo();
                        }
                        break;
                    case 9:
                        System.out.print("Enter Book Name: ");
                        System.out.println("Book ID: " + library.findBookIdByName(scanner.nextLine()));
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }
    }

    // --- helper submenus (same as in second interface) ---
    private static void handleLibraryOptions(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- Library Options ---");
            System.out.println("1. Remove Book");
            System.out.println("2. Add Member");
            System.out.println("3. Revoke Membership");
            System.out.println("4. Checkout Book");
            System.out.println("5. Return Book");
            System.out.println("6. Check Book Availability");
            System.out.println("7. Who Has Book");
            System.out.println("8. Get All Members");
            System.out.println("9. Find Book ID by Name");
            System.out.println("0. Return to Main Menu");
            System.out.print("Select an option: ");

            int libChoice = scanner.nextInt();
            scanner.nextLine();

            if (libChoice == 0) {
                return;
            }

            switch (libChoice) {
                case 1: // remove book
                    System.out.print("Enter Book ID to remove: ");
                    int removeID = Integer.parseInt(scanner.nextLine());
                    library.removeBook(removeID);
                    break;
                case 2: // add member
                    System.out.print("Member name: ");
                    String memberName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    int memberID;
                    while (true) {
                        System.out.print("Member ID: ");
                        try {
                            memberID = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Member ID. Please enter a valid integer.");
                        }
                    }
                    Member newMember = new Member(memberName, email, memberID);
                    library.addMember(newMember);
                    break;
                case 3: // revoke membership
                    System.out.print("Enter Member ID to revoke: ");
                    int revokeID = Integer.parseInt(scanner.nextLine());
                    library.revokeMembership(revokeID);
                    break;
                case 4: // checkout book
                    System.out.print("Book ID to checkout: ");
                    int checkoutBookID = Integer.parseInt(scanner.nextLine());
                    System.out.print("Member ID: ");
                    int checkoutMemberID = Integer.parseInt(scanner.nextLine());
                    library.checkoutBook(checkoutBookID, checkoutMemberID);
                    break;
                case 5: // return book
                    System.out.print("Book ID to return: ");
                    int returnBookID = Integer.parseInt(scanner.nextLine());
                    System.out.print("Member ID: ");
                    int checkinMemberID = Integer.parseInt(scanner.nextLine());
                    library.returnBook(returnBookID, checkinMemberID);
                    break;
                case 6: // check book availability
                    System.out.print("Enter Book ID to check availability: ");
                    int availID = Integer.parseInt(scanner.nextLine());
                    System.out.println("Available: " + library.bookAvailability(availID));
                    break;
                case 7: // who has book
                    System.out.print("Enter Book ID to check who has it: ");
                    int whoID = Integer.parseInt(scanner.nextLine());
                    System.out.println("Checked out by: " + library.whoHasBook(whoID));
                    break;
                case 8: // get all members
                    System.out.println("All Members: ");
                    for (Member m : library.getAllMembers()) {
                        m.printMemberInfo();
                    }
                    break;
                case 9: // find book ID by name
                    System.out.print("Enter Book Name: ");
                    String bookName = scanner.nextLine();
                    System.out.println("Book ID: " + library.findBookIdByName(bookName));
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleBookOptions(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- Book Options ---");
            System.out.println("1. Check Availability");
            System.out.println("2. Update Book Info");
            System.out.println("3. Get Book Info");
            System.out.println("0. Return to Main Menu");
            System.out.print("Select an option: ");

            int bookChoice = scanner.nextInt();
            scanner.nextLine();

            if (bookChoice == 0) {
                return;
            }

            switch (bookChoice) {
                case 1: // check availability
                    System.out.print("Enter Book ID: ");
                    int bID = Integer.parseInt(scanner.nextLine());
                    Book b1 = library.AllBooksInLibrary.get(bID);
                    if (b1 != null) {
                        System.out.println("Available: " + b1.checkAvailability());
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 2: // update book info
                    System.out.print("Enter Book ID: ");
                    int ubID = Integer.parseInt(scanner.nextLine());
                    Book b2 = library.AllBooksInLibrary.get(ubID);
                    if (b2 != null) {
                        System.out.print("New Name: ");
                        b2.Name = scanner.nextLine();
                        System.out.print("New Author: ");
                        b2.Author = scanner.nextLine();
                        System.out.print("New Year: ");
                        b2.Year = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("New ISBN: ");
                        b2.ISBN = scanner.nextLine();
                        System.out.print("New Genre: ");
                        b2.Genre = scanner.nextLine();
                        b2.updateBookInfo(b2.Name, b2.Author, b2.Year, b2.ISBN, b2.Genre);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 3: // get book info
                    System.out.print("Enter Book ID: ");
                    int gID = Integer.parseInt(scanner.nextLine());
                    Book b3 = library.AllBooksInLibrary.get(gID);
                    if (b3 != null) {
                        System.out.println(b3.getBookInfo());
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

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

            int memChoice = scanner.nextInt();
            scanner.nextLine();

            if (memChoice == 0) {
                return;
            }

            System.out.print("Enter Member ID: ");
            int memberId = Integer.parseInt(scanner.nextLine());
            Member member = library.MemberIDs.get(memberId);

            if (member == null) {
                System.out.println("Member not found.");
                continue;
            }

            switch (memChoice) {
                case 1: // print member info
                    member.printMemberInfo();
                    break;
                case 2: // get borrowed book list
                    System.out.println("Borrowed Books: " + member.getBorrowedBookList());
                    break;
                case 3: // add borrowed book
                    System.out.print("Enter Book ID to add: ");
                    int addBookID = Integer.parseInt(scanner.nextLine());
                    member.addBorrowedBook(addBookID);
                    break;
                case 4: // remove borrowed book
                    System.out.print("Enter Book ID to remove: ");
                    int removeBookID = Integer.parseInt(scanner.nextLine());
                    member.removeBorrowedBook(removeBookID);
                    break;
                case 5: // update member info
                    System.out.print("New Name: ");
                    member.Name = scanner.nextLine();
                    System.out.print("New Email: ");
                    member.Email = scanner.nextLine();
                    member.updateMemberInfo(member.Name, member.Email);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // --- helper input methods ---
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

