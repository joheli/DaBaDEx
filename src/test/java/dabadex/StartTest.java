/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dabadex;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class StartTest {
    @Test public void testStartHasALogger() throws SecurityException, IOException {
        Start classUnderTest = new Start();
        assertNotNull("Start should have a logger", classUnderTest.LOGGER);
    }
}
