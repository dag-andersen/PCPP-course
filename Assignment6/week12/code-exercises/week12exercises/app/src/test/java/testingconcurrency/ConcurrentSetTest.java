package testingconcurrency;

import org.junit.jupiter.api.BeforeEach;
// TODO: Very likely you need to expand the list of imports

public class ConcurrentSetTest {

    // Variable with set under test
    private ConcurrentIntegerSet set;

    // TODO: Very likely you should add more variables here
        

    // Uncomment the appropriate line below to choose the class to
    // test
    // Remember that @BeforeEach is executed before each test
    @BeforeEach
    public void initialize() {
	// init set
	set = new ConcurrentIntegerSetBuggy();
	// set = new ConcurrentIntegerSetSync();	
	// set = new ConcurrentIntegerSetLibrary();
    }

    // TODO: Define your tests below
}
