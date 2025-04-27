package delft;

import java.util.*;

public class Library {
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
