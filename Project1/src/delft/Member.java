package delft;

import java.util.ArrayList;
import java.util.List;

// Member class where you can access and see member info
public class Member {
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
