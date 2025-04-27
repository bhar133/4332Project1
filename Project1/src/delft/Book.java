package delft;

public class Book {
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