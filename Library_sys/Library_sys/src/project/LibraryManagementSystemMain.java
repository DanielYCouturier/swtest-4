package project;

public class LibraryManagementSystemMain {
    public static void main(String[] args) {
        
    	// Create a library
        Library library = new Library();

        // Add books to the library
        library.addBook(new Book("Java Programming", "Author A", "ISBN123"));
        library.addBook(new Book("Data Structures", "Author B", "ISBN456"));
        library.addBook(new Book("Software Testing", "Author C", "ISBN789"));
        library.addBook(new Book("Auto Testing", "Author D", "ISBN321"));

        // Register users
        User user1 = new User("Steve Harvey", "User001");
        User user2 = new User("Jim Carrey", "User002");
        library.registerUser(user1);
        library.registerUser(user2);

        // Borrow a book
        library.borrowBook("User001", "ISBN123");
        library.borrowBook("User002", "ISBN789");

        // Display available books
        //System.out.println();
        library.displayAvailableBooks();

        // Return the book
        System.out.println();
        //System.out.println("Returning 'Java Programming'...");
        library.returnBook("User002", "ISBN789");
        //System.out.println("Book returned successfully!");

        // Display available books again
        System.out.println();
        library.displayAvailableBooks();
        
        // Display statistics
        System.out.println();
        System.out.println("Total number of books: " + library.getTotalNumberOfBooks());
        System.out.println("Total number of borrowed books: " + library.getTotalBorrowedBooks());
        System.out.println("Average number of books per user: " + library.getAverageBooksPerUser());
    }
}
