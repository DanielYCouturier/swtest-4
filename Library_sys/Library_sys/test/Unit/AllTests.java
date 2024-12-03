package Unit;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
@Suite
@SelectClasses({BookTest.class, LibraryTest.class, UserTest.class})
public class AllTests {
}
