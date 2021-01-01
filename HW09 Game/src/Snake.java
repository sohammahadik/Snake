import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

class Snake extends GameObj {
    public static final int SIZE = 20;
    public static final int INIT_POS_X = 100;
    public static final int INIT_POS_Y = 200;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private int courtWidth;
    private int courtHeight;

    private Color color;
    
    private LinkedList<Square> snake;
    
    private int length;
    
    private Square head;
    
    // creates new snake by adding square to internal representation
    public Snake(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        
        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;
        
        this.color = color;
        
        snake = new LinkedList<Square>();
        Square x = new Square(courtWidth, courtHeight, color);
        x.setPx(INIT_POS_X);
        x.setPy(INIT_POS_Y);
        head = x;
        snake.add(x);
        length = 1;
    }
    
    public int getLength() {
        return length;
    }
    
    public Square getHead() {
        return head;
    }
    
    @Override
    public void setVx(int vx) {
        head.setVx(vx);
    }
    
    @Override
    public void setVy(int vy) {
        head.setVy(vy);
    }
    
    @Override
    public int getVx() {
        return head.getVx();
    }
    
    @Override
    public int getVy() {
        return head.getVy();
    }
    
    @Override
    public int getPx() {
        return head.getPx();
    }
    
    @Override
    public int getPy() {
        return head.getPy();
    }
    
    // returns copy of internal state
    public LinkedList<Square> getCopyList() {
        LinkedList<Square> s = new LinkedList<Square>();
        for (Square square: snake) {
            s.add(square);
        }
        return s;
    }
          
    // method used for adding square to snake after collision with apple
    public void appendSquare() {
        Square sq = new Square(courtWidth, courtHeight, color);
        Square last = snake.getLast();
        sq.setPx(last.getPx());
        sq.setPy(last.getPy());
        sq.setVx(last.getVx());
        sq.setVy(last.getVy());
        snake.add(sq);
        length += 1;
    }
    
    // moves snake by passing on each square's movement/position to the square directly 
    // following it
    @Override
    public void move() {
        int prevX = 0;
        
        int prevY = 0;
        
        int prevVX = 0;
        
        int prevVY = 0;
        for (Square s: snake) {
            int x = s.getPx();
            int y = s.getPy();
            int vx = s.getVx();
            int vy = s.getVy();
            if (s.equals(head)) {
                s.move();
                head = s;
            } else {
                s.setPx(prevX);
                s.setPy(prevY);
                s.setVx(prevVX);
                s.setVy(prevVY);
            }
            prevX = x;
            prevY = y;
            prevVX = vx;
            prevVY = vy;
        }
    }
    
    @Override
    public boolean intersects(GameObj that) {
        return getHead().intersects(that);
    }
    
    // checks if snake intersects element of a list and returns that element
    public Square intersectsList(LinkedList<Square> l) {
        for (Square g: l) {
            if (intersects(g)) {
                return g;
            }
        }
        return null;
    }
    
    // checks if snakes runs into wall
    @Override
    public boolean getClipCalled() {
        return getHead().getClipCalled();
    }
    
    // checks if snakes runs into self
    public boolean selfIntersect() {
        LinkedList<Square> s = getCopyList();
        s.remove();
        if (intersectsList(s) != null) {
            return true;
        }
        return false;
    }
    

    @Override
    public void draw(Graphics g) {
        for (Square s: snake) {
            g.setColor(this.color.darker());
            g.fillRect(s.getPx(), s.getPy(), s.getWidth(), s.getHeight());
            g.setColor(this.color.darker().darker());
            g.drawRect(s.getPx(), s.getPy(), s.getWidth(), s.getHeight());
            g.drawRect(s.getPx() + 1, s.getPy() + 1, s.getWidth() - 2, s.getHeight() - 2);
        }
    }
    
}