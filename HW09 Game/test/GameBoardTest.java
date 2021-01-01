import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JLabel;

import org.junit.jupiter.api.Test;

class GameBoardTest {
    
    
    JLabel run = new JLabel("Running...");

    @Test
    public void testWinningBoard() {
        GameBoard gb = new GameBoard(run);
        int[][] array = new int[1][1];
        assertFalse(gb.winningBoard(array));
        array[0][0] = 2;
        assertTrue(gb.winningBoard(array));
    }
    
    @Test
    public void testSnakeMove() {
        GameBoard gb = new GameBoard(run);
        gb.reset();
        gb.setEnemyPos(100, 300);
        gb.setApplePos(100, 100);
        gb.setSnakeVel(20, 0);
        assertEquals(1, gb.gameState()[5][10]);
        gb.tick();
        assertEquals(0, gb.gameState()[5][10]);
        assertEquals(1, gb.gameState()[6][10]);
        gb.setApplePos(140, 200);
        gb.tick();
        gb.tick();
        assertEquals(0, gb.gameState()[6][10]);
        assertEquals(1, gb.gameState()[7][10]);
        assertEquals(1, gb.gameState()[8][10]);
    }
    
    @Test
    public void testEnemyMove() {
        GameBoard gb = new GameBoard(run);
        assertEquals(0, gb.spacesOccupied());
        gb.reset();
        LinkedList<Point> snakes = gb.getLocations(1);
        LinkedList<Point> enemies = gb.getLocations(2);
        Point p = enemies.get(0);
        LinkedList<Point> apples = gb.getLocations(3);
        assertEquals(1, snakes.size());
        assertEquals(1, enemies.size());
        assertEquals(1, apples.size());
        assertEquals(3, gb.spacesOccupied());
        for (int i = 0; i < 101; i++) {
            gb.tick();
        }
        LinkedList<Point> newEnemies = gb.getLocations(2);
        Point p1 = newEnemies.get(0);
        assertEquals(1, newEnemies.size());
        assertEquals(3, gb.spacesOccupied());
        assertNotEquals(p, p1);
    }
    
    @Test
    public void snakeAppleIntersect() {
        GameBoard gb = new GameBoard(run);
        gb.reset();
        gb.setApplePos(120, 200);
        gb.setSnakeVel(20, 0);
        assertEquals(3, gb.spacesOccupied());
        assertEquals(1, gb.getLocations(1).size());
        gb.tick();
        gb.tick();
        assertEquals(2, gb.getLocations(1).size());
        assertEquals(4, gb.spacesOccupied());
        
    }
    
    @Test
    public void snakeEnemyIntersect() {
        GameBoard gb = new GameBoard(run);
        gb.reset();
        gb.setEnemyPos(120, 200);
        gb.setSnakeVel(20, 0);
        assertEquals(3, gb.spacesOccupied());
        assertEquals(1, gb.getLocations(1).size());
        gb.tick();
        assertEquals(2, gb.spacesOccupied());
        assertFalse(gb.getPlaying());
        assertEquals("You lose!", gb.getLabel());  
    }
    
    @Test 
    public void snakeWallIntersect() {
        GameBoard gb = new GameBoard(run);
        gb.reset();
        gb.setEnemyPos(100, 300);
        gb.setApplePos(100, 100);
        gb.setSnakeVel(-20, 0);
        for (int i = 0; i < 4; i++) {
            gb.tick();
        }
        assertTrue(gb.getPlaying());
        assertEquals("Running...", gb.getLabel());
        gb.tick();
        assertTrue(gb.getPlaying());
        assertEquals("Running...", gb.getLabel());
        gb.tick();
        assertFalse(gb.getPlaying());
        assertEquals("You lose!", gb.getLabel());
    }
    
    @Test
    public void newEnemySpawn() {
        GameBoard gb = new GameBoard(run);
        gb.reset();
        int xPos = 120;
        int y = 200;
        gb.setEnemyPos(100, 300);
        gb.setSnakeVel(20, 0);
        for (int i = 0; i < 8; i++) {
            gb.setApplePos(xPos, y);
            gb.tick();
            gb.tick();
            xPos += 40;
            assertEquals(i + 2, gb.getLocations(1).size());
        }
        assertEquals(2, gb.getLocations(2).size());
    }
    
    @Test
    public void testPause() {
        GameBoard gb = new GameBoard(run);
        gb.reset();
        gb.setSnakeVel(20, 0);
        gb.tick();
        gb.pause();
        gb.tick();
        assertFalse(gb.getPlaying());
        assertEquals(1, gb.gameState()[6][10]);
        gb.pause();
        gb.tick();
        assertTrue(gb.getPlaying());
        assertEquals(1, gb.gameState()[7][10]);
        assertEquals(0, gb.gameState()[6][10]);
    }

}
