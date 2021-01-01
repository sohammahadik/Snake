import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.LinkedList;


public class SnakeTest {
    
    private int courtWidth = 700;
    private int courtHeight = 500;
    private Color c = Color.GREEN;

    @Test
    public void testMoveTrivial() {
        Snake s = new Snake(courtWidth, courtHeight, c);
        s.setVx(20);
        s.setVy(0);
        s.move();
        assertEquals(120, s.getPx());
    }
    
    @Test
    public void testMoveNonTrivial() {
        Snake s = new Snake(courtWidth, courtHeight, c);
        s.setVx(20);
        s.setVy(0);
        s.move();
        
        assertEquals(120, s.getPx());
        
        s.appendSquare();
        s.move();
        
        assertEquals(140, s.getPx());
        
        s.appendSquare();
        s.move();
        
        assertEquals(160, s.getPx());
        
        assertEquals(3, s.getLength());
        
        for (Square sq: s.getCopyList()) {
            assertEquals(20, sq.getVx());
        }
        
        Square s2 = s.getCopyList().get(1);
        assertEquals(140, s2.getPx());
        
        Square s3 = s.getCopyList().get(2);
        assertEquals(120, s3.getPx());
        
        s.setVx(0);
        s.setVy(20);
        s.move();
        
        assertEquals(160, s.getPx());
        assertEquals(220, s.getPy());
        
        Square s2New = s.getCopyList().get(1);
        assertEquals(160, s2New.getPx());
        assertEquals(200, s2New.getPy());
        
        Square s3New = s.getCopyList().get(2);
        assertEquals(140, s3New.getPx());
        assertEquals(200, s3New.getPy());
        
        s.move();
        
        Square s2Final = s.getCopyList().get(1);
        assertEquals(160, s2Final.getPx());
        assertEquals(220, s2Final.getPy());
        assertEquals(0, s2Final.getVx());
        assertEquals(20, s2Final.getVy());
    }
    
    @Test
    public void testAppendSquare() {
        Snake s = new Snake(courtWidth, courtHeight, c);
        s.setVx(20);
        s.setVy(0);
        
        s.appendSquare();
        
        Square s2 = s.getCopyList().get(1);
        
        assertEquals(100, s2.getPx());
        assertEquals(200, s2.getPy());
        assertEquals(20, s2.getVx());
        assertEquals(0, s2.getVy());
        
        s.move();
        
        assertEquals(120, s.getPx());
        
        Square s2New = s.getCopyList().get(1);
        assertEquals(100, s2New.getPx());
        assertEquals(200, s2.getPy());
        assertEquals(20, s2.getVx());
        assertEquals(0, s2.getVy());
        
    }
    
    @Test
    public void testIntersectsTrivial() {
        Snake s = new Snake(courtWidth, courtHeight, c);
        s.setVx(20);
        s.setVy(0);
        
        Square sq = new Square(courtWidth, courtHeight, c);
        sq.setPx(120);
        sq.setPy(200);
        
        assertFalse(s.intersects(sq));
        
        s.move();
        
        assertTrue(s.intersects(sq));
    }
    
    @Test
    public void testIntersectsNonHead() {
        Snake s = new Snake(courtWidth, courtHeight, c);
        s.setVx(20);
        s.setVy(0);
        s.appendSquare();
        
        s.move();
        Square sq = new Square(courtWidth, courtHeight, c);
        sq.setPx(100);
        sq.setPy(200);
        
        assertFalse(s.intersects(sq));
    }
    
    @Test
    public void testIntersectsList() {
        Snake s = new Snake(courtWidth, courtHeight, c);
        s.setVx(20);
        s.setVy(0);
        s.appendSquare();
        
        LinkedList<Square> enemies = new LinkedList<Square>();
        Square sq = new Square(courtWidth, courtHeight, c);
        sq.setPx(120);
        sq.setPy(200);
        
        Square sq1 = new Square(courtWidth, courtHeight, c);
        sq1.setPx(300);
        sq1.setPy(200);
        
        enemies.add(sq);
        enemies.add(sq1);
        
        assertNull(s.intersectsList(enemies));
        
        s.move();
        
        assertEquals(sq, s.intersectsList(enemies));
    }
    
    
    @Test
    public void testSelfIntersect() {
        Snake s = new Snake(courtWidth, courtHeight, c);
        s.setVx(20);
        s.setVy(0);
        s.appendSquare();
        s.appendSquare();
        s.appendSquare();
        s.appendSquare();
        
        s.move();
        s.move();
        s.move();
        s.move();
        s.move();
        
        s.setVx(0);
        s.setVy(20);
        
        s.move();
        
        s.setVx(20);
        s.setVy(0);
        
        s.move();
        
        s.setVx(0);
        s.setVy(-20);
        
        s.move();
        
        assertFalse(s.selfIntersect());
        
        s.setVx(-20);
        s.setVy(0);
        
        s.move();
        
        assertTrue(s.selfIntersect());
        
    }

}
