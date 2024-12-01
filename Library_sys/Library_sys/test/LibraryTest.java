import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.Library;
import project.Book;
import project.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    private Library lib;
    private User user1;
    private User user2;
    private User user3;
    private Book book1;
    private Book book2;
    private Book book3;
    private List<Book> lib_books;
    private Map<String, User> lib_users;

    @BeforeEach
    public void setUpLibrary() throws Exception {
        lib = new Library();
        user1 = new User("name_1", "userid_1");
        user2 = new User("name_2", "userid_2");
        user3 = new User("name_3", "userid_3");
        book1 = new Book("title_1", "author_1", "isbn_1");
        book2 = new Book("title_2", "author_2", "isbn_2");
        book3 = new Book("title_3", "author_3", "isbn_3");
        lib.registerUser(user1);
        lib.registerUser(user2);
        lib.registerUser(user3);
        lib.addBook(book1);
        lib.addBook(book2);
        lib.addBook(book3);
        Field usersField = Library.class.getDeclaredField("users");
        usersField.setAccessible(true);
        lib_users = (Map<String, User>) usersField.get(lib);

        Field booksField = Library.class.getDeclaredField("books");
        booksField.setAccessible(true);
        lib_books = (List<Book>) booksField.get(lib);
    }

    @Test
    void testAdd() {
        User user4 = new User("name_4", "userid_4");
        lib.registerUser(user4);
        assertSame(lib_users.get(user4.getUserId()),user4);
    }
    @Test
    public void testAddDuplicate(){
        User user4 = new User("name_4", "userid_1");
        lib.registerUser(user4);
        assertSame(lib_users.get(user4.getUserId()), user4);
        assertSame(lib_users.get(user1.getUserId()), user1);
    }

    @Test
    public void testAddBook() throws Exception {
        Book book4 = new Book("title_4", "author_4", "isbn_4");
        lib.addBook(book4);
        assertTrue(lib_books.contains(book4));
    }

    @Test
    public void testBorrowBook() {
        lib.borrowBook(user1.getUserId(), book1.getIsbn());
        assertTrue(user1.getBorrowedBooks().contains(book1));
        assertFalse(book1.isAvailable());
    }
    @Test
    public void testBorrowInvalidUser(){
        User user4 = new User("name_4", "userid_4");
        assertThrows(Exception.class,() ->{
            lib.borrowBook(user4.getUserId(), book1.getIsbn());
        });
    }
    @Test
    public void testBorrowInvalidBook(){
        Book book4 = new Book("title_4","author_4","isbn_4");
        assertThrows(Exception.class,() ->{
            lib.borrowBook(user1.getUserId(), book4.getIsbn());
        });
    }

    public void testBorrowUnavailableBook(){
        lib.borrowBook();
        assertThrows(Exception.class,() ->{
            lib.borrowBook(user1.getUserId(), book4.getIsbn());
        });
    }


//        // Registered user should not be able to check out unavailable book
//        assertThrows(Exception.class,() ->{
//                    lib.borrowBook(user3.getUserId(), book1.getIsbn());
//                }
//        );
//        // Unregistered book should not be checkout-able
//        assertThrows(Exception.class,() ->{
//                    lib.borrowBook(user1.getUserId(), book2.getIsbn());
//                }
//        );
//
//
//
//    }
//
//    @Test public void returnBookTest(){
//        Library lib = new Library();
//        Book book1 = new Book("title_1","author_1","isbn_1");
//        Book book2 = new Book("title_2","author_2","isbn_2");
//        lib.addBook(book1);
//
//        User user1 = new User("name_1", "userid_1");
//        User user2 = new User("name_2", "userid_2");
//        User user3 = new User("name_3", "userid_3");
//        lib.registerUser(user1);
//        lib.registerUser(user2);
//
//        // Establish baseline
//        lib.borrowBook(user1.getUserId(),book1.getIsbn());
//        assertTrue(user1.getBorrowedBooks().contains(book1));
//        assertFalse(book1.isAvailable());
//
//        // User cannot return other users book
//        assertThrows(Exception.class,() ->{
//            lib.returnBook(user2.getUserId(),book1.getIsbn());
//
//        });
//        // Unregistered users cannot return books
//        assertThrows(Exception.class,() ->{
//            lib.returnBook(user3.getUserId(),book1.getIsbn());
//        });
//        // Unregistered books cannot be returned
//        assertThrows(Exception.class,() ->{
//            lib.returnBook(user1.getUserId(),book2.getIsbn());
//        });
//
//        // Borrowed and Borrower state are reset upon return
//        lib.returnBook(user1.getUserId(),book1.getIsbn());
//        assertFalse(user1.getBorrowedBooks().contains(book1));
//        assertTrue(book1.isAvailable());
//
//    }
//
//    @Test
//    public void testDisplayBooks(){
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PrintStream sysOut = System.out;
//        System.setOut(new PrintStream(outputStream));
//
//
//        Library lib = new Library();
//        Book book1 = new Book("title_1","author_1","isbn_1");
//        Book book2 = new Book("title_2","author_2","isbn_2");
//        User user1 = new User("name_1", "userid_1");
//        lib.addBook(book1);
//        lib.addBook(book2);
//        lib.registerUser(user1);
//        lib.borrowBook(user1.getUserId(),book1.getIsbn());
//
//        lib.displayAvailableBooks();
//        String capturedOutput = outputStream.toString();
//        System.setOut(sysOut);
//
//        assertFalse(capturedOutput.contains("title_1"));
//        assertTrue(capturedOutput.contains("title_2"));
//
//
//    }
//    @Test
//    public void testAvailableBooks(){
//        Library lib = new Library();
//        Book book1 = new Book("title_1","author_1","isbn_1");
//        Book book2 = new Book("title_2","author_2","isbn_2");
//        User user1 = new User("name_1", "userid_1");
//        lib.addBook(book1);
//        lib.addBook(book2);
//        lib.registerUser(user1);
//        List<Book> available = lib.getAvailableBooks();
//
//        //Establish Baseline
//        assertTrue(available.contains(book1));
//        assertTrue(available.contains(book2));
//
//        // Test available list
//        lib.borrowBook(user1.getUserId(),book1.getIsbn());
//        available = lib.getAvailableBooks();
//        assertFalse(available.contains(book1));
//        assertTrue(available.contains(book2));
//    }
//
//    @Test
//    public void testBookCount(){
//        Book book1 = new Book("title_1","author_1","isbn_1");
//        Book book2 = new Book("title_2","author_2","isbn_2");
//        Book book3 = new Book("title_3","author_3","isbn_3");
//        Library lib = new Library();
//        lib.addBook(book1);
//        lib.addBook(book2);
//        lib.addBook(book3);
//        int bookCount = lib.getTotalNumberOfBooks();
//        assertEquals(3, bookCount);
//    }
//
//    @Test
//    public void testBorrowedBookCount(){
//        Book book1 = new Book("title_1","author_1","isbn_1");
//        Book book2 = new Book("title_2","author_2","isbn_2");
//        Book book3 = new Book("title_3","author_3","isbn_3");
//        User user1 = new User("name_1", "userid_1");
//        Library lib = new Library();
//        lib.addBook(book1);
//        lib.addBook(book2);
//        lib.addBook(book3);
//        lib.registerUser(user1);
//        assertEquals(0, lib.getTotalBorrowedBooks());
//        lib.borrowBook(user1.getUserId(),book1.getIsbn());
//        assertEquals(1, lib.getTotalBorrowedBooks());
//        lib.borrowBook(user1.getUserId(),book2.getIsbn());
//        assertEquals(2, lib.getTotalBorrowedBooks());
//        lib.borrowBook(user1.getUserId(),book3.getIsbn());
//        assertEquals(3, lib.getTotalBorrowedBooks());
//    }
//
//    @Test
//    public void averageBookTest(){
//        // Test empty library
//        Library lib = new Library();
//        assertEquals(0, lib.getAverageBooksPerUser(), 0.01);
//        Book book1 = new Book("title_1","author_1","isbn_1");
//        Book book2 = new Book("title_2","author_2","isbn_2");
//
//        User user1 = new User("name_1", "userid_1");
//        User user2 = new User("name_2", "userid_2");
//
//        lib.addBook(book1);
//        lib.registerUser(user1);
//
//        // Test one user
//        assertEquals(0, lib.getAverageBooksPerUser(), 0.01);
//
//        //Test one borrowed
//        lib.borrowBook(user1.getUserId(),book1.getIsbn());
//        assertEquals(1, lib.getAverageBooksPerUser(), 0.01);
//
//        //Test 1/2 borrowed
//        lib.registerUser(user2);
//        assertEquals(0.5, lib.getAverageBooksPerUser(), 0.01);
//
//        //Test 2/2 borrowed
//        lib.addBook(book2);
//        lib.borrowBook(user2.getUserId(),book2.getIsbn());
//        assertEquals(1, lib.getAverageBooksPerUser(), 0.01);
//    }
//
//    @Test
//    public void findByIsbnTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Book book1 = new Book("title_1","author_1","isbn_1");
//        Book book2 = new Book("title_2","author_2","isbn_2");
//        Book book3 = new Book("title_3","author_3","isbn_1");
//        Library lib = new Library();
//        lib.addBook(book1);
//        lib.addBook(book2);
//        lib.addBook(book3);
//        Method findBookByIsbn = Library.class.getDeclaredMethod("findBookByIsbn", String.class);
//        findBookByIsbn.setAccessible(true);
//
//        Book book1Search = (Book) findBookByIsbn.invoke(lib, book1.getIsbn());
//        Book book2Search = (Book) findBookByIsbn.invoke(lib, book2.getIsbn());
//        Book book3Search = (Book) findBookByIsbn.invoke(lib, book3.getIsbn());
//
//
//        assertEquals(book1, book1Search);
//        assertEquals(book2, book2Search);
//        assertEquals(book3, book3Search);
//
//
//
//    }
}
