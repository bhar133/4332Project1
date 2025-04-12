package delft;

import java.util.*;

/*
 * delft.Library Management System
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


// delft.Book class where you can see and access the book info
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

    // Getters
    public String getName() { return Name; }
    public String getAuthor() { return Author; }
    public int getYear() { return Year; }
    public String getIsbn() { return ISBN; }
    public String getGenre() { return Genre; }
    public int getBookId() { return BookID; }
    public boolean isAvailable() { return IsAvailable; }

    // Setters
    public void setName(String name) { this.Name = name; }
    public void setAuthor(String author) { this.Author = author; }
    public void setYear(int year) { this.Year = year; }
    public void setIsbn(String isbn) { this.ISBN = isbn; }
    public void setGenre(String genre) { this.Genre = genre; }
    public void setBookId(int bookId) { this.BookID = bookId; }
    public void setAvailability(boolean available) { IsAvailable = available; }


    // returns  book information
    public String getBookInfo() {
        return "[" + BookID + "] " + Name + " by " + Author + ", " + Year + ", Genre: " + Genre +
                (IsAvailable ? " (Available)" : " (Checked Out)");
    }
}

// delft.Member class where you can access and see member info
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

// delft.Library class where you can find the books and member access is
class Library {
    Map<Integer, Book> AllBooksInLibrary = new HashMap<>();
    Map<Integer, Book> LoanedBooks = new HashMap<>();
    Map<Integer, Member> MemberIDs = new HashMap<>();
    Set<Integer> AvailableBookIds = new HashSet<>();

    // adds a book to the library
    public void addBook(Book b) {
        AllBooksInLibrary.put(b.BookID, b);
        AvailableBookIds.add(b.BookID);
    }

    // removes a book from the library
    public void removeBook(int bookID) {
        if (!AllBooksInLibrary.containsKey(bookID)) {
            System.out.println("delft.Book ID not found.");
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
            System.out.println("delft.Book ID not found in library.");
            return;
        }
        if (!MemberIDs.containsKey(memberID)) {
            System.out.println("delft.Member ID not found.");
            return;
        }
        Book book = AllBooksInLibrary.get(bookID);
        Member member = MemberIDs.get(memberID);
        if (!book.IsAvailable) {
            System.out.println("delft.Book is already checked out.");
            return;
        }
        book.IsAvailable = false;
        AvailableBookIds.remove(bookID);
        LoanedBooks.put(bookID, book);
        member.addBorrowedBook(bookID);
        System.out.println("delft.Book checked out successfully.");
    }

    // returns a book from a member
    // updates the book's availability status and removes it from the member's borrowed list
    public void returnBook(int bookID, int memberID) {
        if (!AllBooksInLibrary.containsKey(bookID)) {
            System.out.println("delft.Book ID not found in library.");
            return;
        }
        if (!MemberIDs.containsKey(memberID)) {
            System.out.println("delft.Member ID not found.");
            return;
        }
        Book book = AllBooksInLibrary.get(bookID);
        Member member = MemberIDs.get(memberID);
        if (book.IsAvailable) {
            System.out.println("delft.Book is already returned.");
            return;
        }
        book.IsAvailable = true;
        AvailableBookIds.add(bookID);
        LoanedBooks.remove(bookID);
        member.removeBorrowedBook(bookID);
        System.out.println("delft.Book returned successfully.");
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
}

// CLI for the library system
class Interface {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            try {
                System.out.println("\n--- delft.Main Menu ---");
                System.out.println("1. delft.Library");
                System.out.println("2. delft.Book");
                System.out.println("3. delft.Member");
                System.out.println("4. Exit");
                System.out.print("Select a category: ");

                int mainChoice = scanner.nextInt();
                scanner.nextLine();

                switch (mainChoice) {
                    case 1: // library options
                        handleLibraryOptions(scanner, library);
                        break;
                    case 2: // book options
                        handleBookOptions(scanner, library);
                        break;
                    case 3: // member options
                        handleMemberOptions(scanner, library);
                        break;
                    case 4: // exit
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid main menu option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void handleLibraryOptions(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- delft.Library Options ---");
            System.out.println("1. Add delft.Book");
            System.out.println("2. Remove delft.Book");
            System.out.println("3. Add delft.Member");
            System.out.println("4. Revoke Membership");
            System.out.println("5. Checkout delft.Book");
            System.out.println("6. Return delft.Book");
            System.out.println("7. Check delft.Book Availability");
            System.out.println("8. Who Has delft.Book");
            System.out.println("9. Get All Members");
            System.out.println("10. Find delft.Book ID by Name");
            System.out.println("0. Return to delft.Main Menu");
            System.out.print("Select an option: ");

            int libChoice = scanner.nextInt();
            scanner.nextLine();

            if (libChoice == 0) {
                return;
            }

            switch (libChoice) {
                case 1: // add book
                    System.out.print("delft.Book name: ");
                    String name = scanner.nextLine();
                    System.out.print("Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("delft.Book ID: ");
                    int bookID = Integer.parseInt(scanner.nextLine());
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();
                    Book book = new Book(name, author, year, isbn, bookID, genre);
                    library.addBook(book);
                    break;
                case 2: // remove book
                    System.out.print("Enter delft.Book ID to remove: ");
                    int removeID = Integer.parseInt(scanner.nextLine());
                    library.removeBook(removeID);
                    break;
                case 3: // add member
                    System.out.print("delft.Member name: ");
                    String memberName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    int memberID;
                    while (true) {
                        System.out.print("delft.Member ID: ");
                        try {
                            memberID = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid delft.Member ID. Please enter a valid integer.");
                        }
                    }
                    Member newMember = new Member(memberName, email, memberID);
                    library.addMember(newMember);
                    break;
                case 4: // revoke membership
                    System.out.print("Enter delft.Member ID to revoke: ");
                    int revokeID = Integer.parseInt(scanner.nextLine());
                    library.revokeMembership(revokeID);
                    break;
                case 5: // checkout book
                    System.out.print("delft.Book ID to checkout: ");
                    int checkoutBookID = Integer.parseInt(scanner.nextLine());
                    System.out.print("delft.Member ID: ");
                    int checkoutMemberID = Integer.parseInt(scanner.nextLine());
                    library.checkoutBook(checkoutBookID, checkoutMemberID);
                    break;
                case 6: // return book
                    System.out.print("delft.Book ID to return: ");
                    int returnBookID = Integer.parseInt(scanner.nextLine());
                    System.out.print("delft.Member ID: ");
                    int checkinMemberID = Integer.parseInt(scanner.nextLine());
                    library.returnBook(returnBookID, checkinMemberID);
                    break;
                case 7: // check book availability
                    System.out.print("Enter delft.Book ID to check availability: ");
                    int availID = Integer.parseInt(scanner.nextLine());
                    System.out.println("Available: " + library.bookAvailability(availID));
                    break;
                case 8: // who has book
                    System.out.print("Enter delft.Book ID to check who has it: ");
                    int whoID = Integer.parseInt(scanner.nextLine());
                    System.out.println("Checked out by: " + library.whoHasBook(whoID));
                    break;
                case 9: // get all members
                    System.out.println("All Members: ");
                    for (Member m : library.getAllMembers()) {
                        m.printMemberInfo();
                    }
                    break;
                case 10: // find book ID by name
                    System.out.print("Enter delft.Book Name: ");
                    String bookName = scanner.nextLine();
                    System.out.println("delft.Book ID: " + library.findBookIdByName(bookName));
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleBookOptions(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- delft.Book Options ---");
            System.out.println("1. Check Availability");
            System.out.println("2. Update delft.Book Info");
            System.out.println("3. Get delft.Book Info");
            System.out.println("0. Return to delft.Main Menu");
            System.out.print("Select an option: ");

            int bookChoice = scanner.nextInt();
            scanner.nextLine();

            if (bookChoice == 0) {
                return;
            }

            switch (bookChoice) {
                case 1: // check availability
                    System.out.print("Enter delft.Book ID: ");
                    int bID = Integer.parseInt(scanner.nextLine());
                    Book b1 = library.AllBooksInLibrary.get(bID);
                    if (b1 != null) {
                        System.out.println("Available: " + b1.checkAvailability());
                    } else {
                        System.out.println("delft.Book not found.");
                    }
                    break;
                case 2: // update book info
                    System.out.print("Enter delft.Book ID: ");
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
                        System.out.println("delft.Book not found.");
                    }
                    break;
                case 3: // get book info
                    System.out.print("Enter delft.Book ID: ");
                    int gID = Integer.parseInt(scanner.nextLine());
                    Book b3 = library.AllBooksInLibrary.get(gID);
                    if (b3 != null) {
                        System.out.println(b3.getBookInfo());
                    } else {
                        System.out.println("delft.Book not found.");
                    }
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleMemberOptions(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- delft.Member Options ---");
            System.out.println("1. Print delft.Member Info");
            System.out.println("2. Get Borrowed delft.Book List");
            System.out.println("3. Add Borrowed delft.Book");
            System.out.println("4. Remove Borrowed delft.Book");
            System.out.println("5. Update delft.Member Info");
            System.out.println("0. Return to delft.Main Menu");
            System.out.print("Select an option: ");

            int memChoice = scanner.nextInt();
            scanner.nextLine();

            if (memChoice == 0) {
                return;
            }

            System.out.print("Enter delft.Member ID: ");
            int memberId = Integer.parseInt(scanner.nextLine());
            Member member = library.MemberIDs.get(memberId);

            if (member == null) {
                System.out.println("delft.Member not found.");
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
                    System.out.print("Enter delft.Book ID to add: ");
                    int addBookID = Integer.parseInt(scanner.nextLine());
                    member.addBorrowedBook(addBookID);
                    break;
                case 4: // remove borrowed book
                    System.out.print("Enter delft.Book ID to remove: ");
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
}
