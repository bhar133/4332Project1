import java.util.*;

public class Main {
    public static void main(String[] args) {
        Interface.start();
    }
}

// Book class where you can see and access the book info
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

    public boolean checkAvailability() {
        return IsAvailable;
    }

    public void updateBookInfo(String name, String author, int year, String isbn, String genre) {
        Name = name;
        Author = author;
        Year = year;
        ISBN = isbn;
        Genre = genre;
    }

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

    public Member(String name, String email, int id) {
        Name = name;
        Email = email;
        MemberID = id;
    }

    public void printMemberInfo() {
        System.out.println("[" + MemberID + "] " + Name + " (" + Email + ")");
    }

    public List<Integer> getborrowedbookList() {
        return BorrowedBookList;
    }

    public void addBorrowedBook(int bookID) {
        BorrowedBookList.add(bookID);
    }

    public void removeBorrowedBook(int bookID) {
        BorrowedBookList.remove(Integer.valueOf(bookID));
    }

    public void UpdateMemberInfo(String name, String email) {
        Name = name;
        Email = email;
    }
}

// Library class where you can find the books and member access is
class Library {
    Map<Integer, Book> AllBooksInLibrary = new HashMap<>();
    Map<Integer, Book> LoanedBooks = new HashMap<>();
    Map<Integer, Member> MemberIDs = new HashMap<>();
    Set<Integer> AvailableBookIds = new HashSet<>();

    public void addBook(Book b) {
        AllBooksInLibrary.put(b.BookID, b);
        AvailableBookIds.add(b.BookID);
    }

    public void removeBook(int bookID) {
        AllBooksInLibrary.remove(bookID);
        AvailableBookIds.remove(bookID);
        LoanedBooks.remove(bookID);
    }

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

    public void addMember(Member m) {
        MemberIDs.put(m.MemberID, m);
    }

    public void revokeMembership(int memberID) {
        MemberIDs.remove(memberID);
    }

    public boolean bookAvailability(int bookID) {
        return AvailableBookIds.contains(bookID);
    }

    public String whoHasBook(int bookID) {
        for (Member m : MemberIDs.values()) {
            if (m.BorrowedBookList.contains(bookID)) {
                return m.Name;
            }
        }
        return "None";
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(MemberIDs.values());
    }

    public int findBookIdByName(String name) {
        for (Book b : AllBooksInLibrary.values()) {
            if (b.Name.equalsIgnoreCase(name)) return b.BookID;
        }
        return -1;
    }
}

class Interface {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Library");
            System.out.println("2. Book");
            System.out.println("3. Member");
            System.out.println("4. Exit");
            System.out.print("Select a category: ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (mainChoice) {
                case 1: // Library
                    System.out.println("\n--- Library Options ---");
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
                    System.out.print("Select an option: ");
                    int libChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (libChoice) {
                        case 1:
                            System.out.print("Book name: ");
                            String name = scanner.nextLine();
                            System.out.print("Author: ");
                            String author = scanner.nextLine();
                            System.out.print("Year: ");
                            int year = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("ISBN: ");
                            String isbn = scanner.nextLine();
                            System.out.print("Book ID: ");
                            int bookID = Integer.parseInt(scanner.nextLine());
                            System.out.print("Genre: ");
                            String genre = scanner.nextLine();
                            Book book = new Book(name, author, year, isbn, bookID, genre);
                            library.addBook(book);
                            break;
                        case 2:
                            System.out.print("Enter Book ID to remove: ");
                            int removeID = Integer.parseInt(scanner.nextLine());
                            library.removeBook(removeID);
                            break;
                        case 3:
                            System.out.print("Member name: ");
                            String memberName = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Member ID: ");
                            int memberID = Integer.parseInt(scanner.nextLine());
                            Member newMember = new Member(memberName, email, memberID);
                            library.addMember(newMember);
                            break;
                        case 4:
                            System.out.print("Enter Member ID to revoke: ");
                            int revokeID = Integer.parseInt(scanner.nextLine());
                            library.revokeMembership(revokeID);
                            break;
                        case 5:
                            System.out.print("Book ID to checkout: ");
                            int checkoutBookID = Integer.parseInt(scanner.nextLine());
                            System.out.print("Member ID: ");
                            int checkoutMemberID = Integer.parseInt(scanner.nextLine());
                            library.checkoutBook(checkoutBookID, checkoutMemberID);
                            break;
                        case 6:
                            System.out.print("Book ID to return: ");
                            int returnBookID = Integer.parseInt(scanner.nextLine());
                            System.out.print("Member ID: ");
                            int checkinMemberID = Integer.parseInt(scanner.nextLine());
                            library.returnBook(returnBookID, checkinMemberID);
                            break;
                        case 7:
                            System.out.print("Enter Book ID to check availability: ");
                            int availID = Integer.parseInt(scanner.nextLine());
                            System.out.println("Available: " + library.bookAvailability(availID));
                            break;
                        case 8:
                            System.out.print("Enter Book ID to check who has it: ");
                            int whoID = Integer.parseInt(scanner.nextLine());
                            System.out.println("Checked out by: " + library.whoHasBook(whoID));
                            break;
                        case 9:
                            System.out.println("All Members: ");
                            for (Member m : library.getAllMembers()) {
                                m.printMemberInfo();
                            }
                            break;
                        case 10:
                            System.out.print("Enter Book Name: ");
                            String bookName = scanner.nextLine();
                            System.out.println("Book ID: " + library.findBookIdByName(bookName));
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                    break;

                case 2: // Book
                    System.out.println("\n--- Book Options ---");
                    System.out.println("1. Check Availability");
                    System.out.println("2. Update Book Info");
                    System.out.println("3. Get Book Info");
                    System.out.print("Select an option: ");
                    int bookChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (bookChoice) {
                        case 1:
                            System.out.print("Enter Book ID: ");
                            int bID = Integer.parseInt(scanner.nextLine());
                            Book b1 = library.AllBooksInLibrary.get(bID);
                            if (b1 != null) {
                                System.out.println("Available: " + b1.checkAvailability());
                            } else {
                                System.out.println("Book not found.");
                            }
                            break;
                        case 2:
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
                        case 3:
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
                    break;

                case 3: // Member
                    System.out.println("\n--- Member Options ---");
                    System.out.println("1. Print Member Info");
                    System.out.println("2. Get Borrowed Book List");
                    System.out.println("3. Add Borrowed Book");
                    System.out.println("4. Remove Borrowed Book");
                    System.out.println("5. Update Member Info");
                    System.out.print("Select an option: ");
                    int memChoice = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Member ID: ");
                    int memberId = Integer.parseInt(scanner.nextLine());
                    Member member = library.MemberIDs.get(memberId);

                    if (member == null) {
                        System.out.println("Member not found.");
                        break;
                    }

                    switch (memChoice) {
                        case 1:
                            member.printMemberInfo();
                            break;
                        case 2:
                            System.out.println("Borrowed Books: " + member.getborrowedbookList());
                            break;
                        case 3:
                            System.out.print("Enter Book ID to add: ");
                            int addBookID = Integer.parseInt(scanner.nextLine());
                            member.addBorrowedBook(addBookID);
                            break;
                        case 4:
                            System.out.print("Enter Book ID to remove: ");
                            int removeBookID = Integer.parseInt(scanner.nextLine());
                            member.removeBorrowedBook(removeBookID);
                            break;
                        case 5:
                            System.out.print("New Name: ");
                            member.Name = scanner.nextLine();
                            System.out.print("New Email: ");
                            member.Email = scanner.nextLine();
                            member.UpdateMemberInfo(member.Name, member.Email);
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                    break;

                case 4:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid main menu option.");
            }
        }
    }
}
