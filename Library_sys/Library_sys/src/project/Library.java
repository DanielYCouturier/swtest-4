package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Library {
    private List<Book> books;
    private Map<String, User> users;

    public Library() {
        books = new ArrayList<>();
        users = new HashMap<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void borrowBook(String userId, String isbn) {
        User user = users.get(userId);
        Book book = findBookByIsbn(isbn);

        if (user != null && book != null) {
            user.borrowBook(book);
        } else {
            throw new IllegalStateException("User or Book not found");
        }
    }

    public void returnBook(String userId, String isbn) {
        User user = users.get(userId);
        Book book = findBookByIsbn(isbn);

        if (user != null && book != null) {
            user.returnBook(book);
        } else {
            throw new IllegalStateException("User or Book not found");
        }
    }

    public void displayAvailableBooks() {
        System.out.println("Available books in the library:");
        for (Book book : getAvailableBooks()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    // Calculate the total number of books
    public int getTotalNumberOfBooks() {
        return books.size();
    }

    // Calculate the total number of borrowed books
    public int getTotalBorrowedBooks() {
        int borrowedBooks = 0;
        for (Book book : books) {
            if (!book.isAvailable()) {
                borrowedBooks++;
            }
        }
        return borrowedBooks;
    }

    // Calculate the average number of books per user
    public double getAverageBooksPerUser() {
        if (users.isEmpty()) {
            return 0.0;
        }
        
        int totalBorrowed = 0;
        for (User user : users.values()) {
            totalBorrowed += user.getBorrowedBooks().size();
        }
        
        return (double) totalBorrowed / users.size();
    }

    private Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }
}