
import org.junit.Test;
import  static org.junit.Assert.*;
import project.Library;
import project.Book;
import project.User;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class LibraryTest {
    @Test
    public void testAddUser() throws Exception {
        Library lib = new Library();
        User user1 = new User("name_1", "userid_1");
        User user2 = new User("name_2", "userid_2");
        User user3 = new User("name_3", "userid_1");

        lib.registerUser(user1);
        lib.registerUser(user2);
        lib.registerUser(user3);

        Field usersField = Library.class.getDeclaredField("users");
        usersField.setAccessible(true);
        Map<String,User> users = (Map<String,User>) usersField.get(lib);
        // Expect all users are registered in map correctly
        assertSame(users.get(user1.getUserId()), user1);
        assertSame(users.get(user2.getUserId()), user2);
        assertSame(users.get(user3.getUserId()), user3);

    }
    @Test
    public void testAddBook() throws Exception {
        Library lib = new Library();
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        Book book3 = new Book("title_3","author_3","isbn_1");

        lib.addBook(book1);
        lib.addBook(book2);
        lib.addBook(book3);

        Field booksField = Library.class.getDeclaredField("books");
        booksField.setAccessible(true); // Allows access to the private field
        List<Book> books = (List<Book>) booksField.get(lib);
        // Expect all books  are registered in list correctly

        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
        assertTrue(books.contains(book3));
    }

    @Test
    public void testBorrowBook(){
        Library lib = new Library();
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        lib.addBook(book1);

        User user1 = new User("name_1", "userid_1");
        User user2 = new User("name_2", "userid_2");
        User user3 = new User("name_3", "userid_3");
        lib.registerUser(user1);
        lib.registerUser(user2);


        // BorrowBook should be reflected in User.getBorrowedBooks() and Book.isAvailable()
        lib.borrowBook(user1.getUserId(),book1.getIsbn());
        assertTrue(user1.getBorrowedBooks().contains(book1));
        assertFalse(book1.isAvailable());

        // Unregistered user should not be able to check out books
        assertThrows(Exception.class,() ->{
                lib.borrowBook(user2.getUserId(), book1.getIsbn());
            }
        );
        // Registered user should not be able to check out unavailable book
        assertThrows(Exception.class,() ->{
                    lib.borrowBook(user3.getUserId(), book1.getIsbn());
                }
        );
        // Unregistered book should not be checkout-able
        assertThrows(Exception.class,() ->{
                    lib.borrowBook(user1.getUserId(), book2.getIsbn());
                }
        );



    }

    @Test public void returnBookTest(){
        Library lib = new Library();
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        lib.addBook(book1);

        User user1 = new User("name_1", "userid_1");
        User user2 = new User("name_2", "userid_2");
        User user3 = new User("name_3", "userid_3");
        lib.registerUser(user1);
        lib.registerUser(user2);

        // Establish baseline
        lib.borrowBook(user1.getUserId(),book1.getIsbn());
        assertTrue(user1.getBorrowedBooks().contains(book1));
        assertFalse(book1.isAvailable());

        // User cannot return other users book
        assertThrows(Exception.class,() ->{
            lib.returnBook(user2.getUserId(),book1.getIsbn());

        });
        // Unregistered users cannot return books
        assertThrows(Exception.class,() ->{
            lib.returnBook(user3.getUserId(),book1.getIsbn());
        });
        // Unregistered books cannot be returned
        assertThrows(Exception.class,() ->{
            lib.returnBook(user1.getUserId(),book2.getIsbn());
        });

        // Borrowed and Borrower state are reset upon return
        lib.returnBook(user1.getUserId(),book1.getIsbn());
        assertFalse(user1.getBorrowedBooks().contains(book1));
        assertTrue(book1.isAvailable());

    }

    @Test
    public void testDisplayBooks(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream sysOut = System.out;
        System.setOut(new PrintStream(outputStream));


        Library lib = new Library();
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        User user1 = new User("name_1", "userid_1");
        lib.addBook(book1);
        lib.addBook(book2);
        lib.registerUser(user1);
        lib.borrowBook(user1.getUserId(),book1.getIsbn());

        lib.displayAvailableBooks();
        String capturedOutput = outputStream.toString();
        System.setOut(sysOut);

        assertFalse(capturedOutput.contains("title_1"));
        assertTrue(capturedOutput.contains("title_2"));


    }
    @Test
    public void testAvailableBooks(){
        Library lib = new Library();
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        User user1 = new User("name_1", "userid_1");
        lib.addBook(book1);
        lib.addBook(book2);
        lib.registerUser(user1);
        List<Book> available = lib.getAvailableBooks();

        //Establish Baseline
        assertTrue(available.contains(book1));
        assertTrue(available.contains(book2));

        // Test available list
        lib.borrowBook(user1.getUserId(),book1.getIsbn());
        available = lib.getAvailableBooks();
        assertFalse(available.contains(book1));
        assertTrue(available.contains(book2));
    }

    @Test
    public void testBookCount(){
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        Book book3 = new Book("title_3","author_3","isbn_3");
        Library lib = new Library();
        lib.addBook(book1);
        lib.addBook(book2);
        lib.addBook(book3);
        int bookCount = lib.getTotalNumberOfBooks();
        assertEquals(3, bookCount);
    }

    @Test
    public void testBorrowedBookCount(){
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        Book book3 = new Book("title_3","author_3","isbn_3");
        User user1 = new User("name_1", "userid_1");
        Library lib = new Library();
        lib.addBook(book1);
        lib.addBook(book2);
        lib.addBook(book3);
        lib.registerUser(user1);
        assertEquals(0, lib.getTotalBorrowedBooks());
        lib.borrowBook(user1.getUserId(),book1.getIsbn());
        assertEquals(1, lib.getTotalBorrowedBooks());
        lib.borrowBook(user1.getUserId(),book2.getIsbn());
        assertEquals(2, lib.getTotalBorrowedBooks());
        lib.borrowBook(user1.getUserId(),book3.getIsbn());
        assertEquals(3, lib.getTotalBorrowedBooks());
    }

    @Test
    public void averageBookTest(){
        // Test empty library
        Library lib = new Library();
        assertEquals(0, lib.getAverageBooksPerUser(), 0.01);
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");

        User user1 = new User("name_1", "userid_1");
        User user2 = new User("name_2", "userid_2");

        lib.addBook(book1);
        lib.registerUser(user1);

        // Test one user
        assertEquals(0, lib.getAverageBooksPerUser(), 0.01);

        //Test one borrowed
        lib.borrowBook(user1.getUserId(),book1.getIsbn());
        assertEquals(1, lib.getAverageBooksPerUser(), 0.01);

        //Test 1/2 borrowed
        lib.registerUser(user2);
        assertEquals(0.5, lib.getAverageBooksPerUser(), 0.01);

        //Test 2/2 borrowed
        lib.addBook(book2);
        lib.borrowBook(user2.getUserId(),book2.getIsbn());
        assertEquals(1, lib.getAverageBooksPerUser(), 0.01);
    }

    @Test
    public void findByIsbnTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Book book1 = new Book("title_1","author_1","isbn_1");
        Book book2 = new Book("title_2","author_2","isbn_2");
        Book book3 = new Book("title_3","author_3","isbn_1");
        Library lib = new Library();
        lib.addBook(book1);
        lib.addBook(book2);
        lib.addBook(book3);
        Method findBookByIsbn = Library.class.getDeclaredMethod("findBookByIsbn", String.class);
        findBookByIsbn.setAccessible(true);

        Book book1Search = (Book) findBookByIsbn.invoke(lib, book1.getIsbn());
        Book book2Search = (Book) findBookByIsbn.invoke(lib, book2.getIsbn());
        Book book3Search = (Book) findBookByIsbn.invoke(lib, book3.getIsbn());


        assertEquals(book1, book1Search);
        assertEquals(book2, book2Search);
        assertEquals(book3, book3Search);



    }
}
