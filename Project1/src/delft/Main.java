package delft;
import java.util.*;

/*
 * Library Management System
 * Simulates a basic library system with functionality to manage books and members.
 * This class holds the whole interface system
 * the package delft above allows access to all the classes used in the Library Management System
 * those classes are: Book, Member, LirbaryAccount, Librarian, Library, and Purchasing
 */

public class Main {
    public static void main(String[] args) {
        Interface.start();
    }
}


class Interface {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        Librarian currentLibrarian = null;
        while (currentLibrarian == null) {
            System.out.println("\n-- Welcome to the Library of Your Dreams by Brice Tillman, Myles Crockem, and Ben Harrington");
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
            System.out.println("5. View Library Balance");
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
                case 5 -> System.out.printf("Current library balance: $%.2f\n", library.getAccount().getBalance());
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