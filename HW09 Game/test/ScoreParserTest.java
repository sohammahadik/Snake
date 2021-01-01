import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ScoreParserTest {

    @Test
    public void testConstructer() {
        ScoreParser sp = new ScoreParser("files/HighScore.txt");
        assertEquals("files/HighScore.txt", sp.getFileName());
    }
    
    @Test
    public void testNullFile() {
        try {
            new ScoreParser(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }
    
    @Test
    public void testInvalidFile() {
        try {
            new ScoreParser("HighScore.txt");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }
    
    
    @Test
    public void testAddScore() {
        ScoreParser sp = new ScoreParser("files/spTest.txt");
        sp.fileClean();
        sp.addScoreToHighScore(10);
        sp.addScoreToHighScore(4);
        sp.addScoreToHighScore(5);
        
        assertEquals(3, sp.highScoreLength());
        
        assertEquals("10", sp.getDisplayScores()[0].split(" ")[0]);
    }
    
    @Test
    public void testFileClean() {
        ScoreParser sp = new ScoreParser("files/spTest.txt");
        sp.addScoreToHighScore(10);
        sp.addScoreToHighScore(4);
        sp.addScoreToHighScore(5);
        assertNotEquals(0, sp.highScoreLength());
        sp.fileClean();
        assertEquals(0, sp.highScoreLength());
    }
    
    @Test
    public void testGetHighAndLowScore() {
        ScoreParser sp = new ScoreParser("files/spTest.txt");
        sp.fileClean();
        sp.addScoreToHighScore(10);
        sp.addScoreToHighScore(4);
        sp.addScoreToHighScore(5);
        sp.addScoreToHighScore(-1);
        
        assertEquals(10, sp.getHighScore());
        assertEquals(-1, sp.getLowScore());
    }
    
    @Test
    public void testGetDisplayScores() {
        ScoreParser sp = new ScoreParser("files/spTest.txt");
        sp.fileClean();
        sp.addScoreToHighScore(10);
        assertEquals("", sp.getDisplayScores()[2]);
        assertEquals("", sp.getDisplayScores()[1]);
        assertEquals("10", sp.getDisplayScores()[0].split(" ")[0]);
        
        sp.addScoreToHighScore(50);
        sp.addScoreToHighScore(33);
        
        assertEquals("10", sp.getDisplayScores()[2].split(" ")[0]);
        assertEquals("33", sp.getDisplayScores()[1].split(" ")[0]);
        assertEquals("50", sp.getDisplayScores()[0].split(" ")[0]);
        
        sp.addScoreToHighScore(50);
        sp.addScoreToHighScore(33);
        
        assertEquals("33", sp.getDisplayScores()[2].split(" ")[0]);
        assertEquals("50", sp.getDisplayScores()[1].split(" ")[0]);
        assertEquals("50", sp.getDisplayScores()[0].split(" ")[0]);
        sp.fileClean();
    }

}
