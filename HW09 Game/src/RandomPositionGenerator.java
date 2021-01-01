import java.awt.Point;
import java.util.Random;

/**
 * 
 * Generates random Tuples with parameters of maximum boundary in both x and y direction.
 * Could not test class due to randomization
 */
class RandomPositionGenerator {
    
    // store max values for x and y coordinates
    private int maxX;
    private int maxY;
    private Point p;
    
    public RandomPositionGenerator(int width, int height) {
        maxX = width;
        maxY = height;
        p = new Point(width, height);
    }
    
    // randomly generates point
    public Point generate() {
        Random px = new Random();
        Random py = new Random();
        p.move(px.nextInt(maxX), py.nextInt(maxY));
        return p;
    }
    
}