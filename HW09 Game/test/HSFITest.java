import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class HSFITest {

    @Test
    public void testNull() {
        try {
            new HighScoreFileIterator(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }
    
    @Test
    public void testInvalidFile() {
        try {
            new HighScoreFileIterator("Test.txt");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }
    
    @Test
    public void testNextFail() {
        HighScoreFileIterator li = new HighScoreFileIterator("files/Test.txt");
        assertTrue(li.hasNext());
        assertEquals("Hello everyone?", li.next());
        assertTrue(li.hasNext());
        assertEquals("My name is steve!", li.next());
        assertTrue(li.hasNext());
        assertEquals("I like CIS 120.", li.next());
        assertFalse(li.hasNext());
        try {
            li.next();
        } catch (NoSuchElementException e) {
            return;
        }
        fail();
    }

}
