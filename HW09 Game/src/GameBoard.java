/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 * 
 * My game board closely replicated many of the methods in the MoD starter code;
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * GameBoard is very similar to GameCourt, but additional logic added for snake.
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    // the state of the game logic
    private Snake snake; // Green snake controlled by arrow keys    
    private LinkedList<Square> appleList; // List of Yellow square that remains stationary, 
                                          // collision grows snake
    private LinkedList<Square> enemyList; //List of Red square that remains stationary, 
                                          //collision kills snake
    private int elCount; // counter that is used to updates apple List

    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    private RandomPositionGenerator ng; // constant position generator for whole game
    private int prev; // holds previous key call so it cannot be called 
                      // again and its opposite as well
    private int[][] game; // holds the state of the game, which positions hold a item or not
    private int score; // score of a current game
    private int enemyTimeCounter;
    private int time; // stores time passed in order to randomly move enemy
    private int delay;
    private ScoreParser sp;

    // Game constants
    public static final int COURT_WIDTH = 700;
    public static final int COURT_HEIGHT = 500;
    public static final int SQUARE_VELOCITY = 20;

    // Update interval for timer, in milliseconds
    private static int interval;

    
    public GameBoard(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // initializes some game states
        game = new int[COURT_WIDTH / SQUARE_VELOCITY][COURT_HEIGHT / SQUARE_VELOCITY];
        
        score = 0;
        
        enemyTimeCounter = 0;
        
        elCount = 0;
        
        time = 0;
        
        delay = 0;
        
        sp = new ScoreParser("files/HighScore.txt");
        
        ng = new RandomPositionGenerator(COURT_WIDTH / SQUARE_VELOCITY, 
                COURT_HEIGHT / SQUARE_VELOCITY);
        
        interval = 100;


        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(interval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        //
        // Extends MoD so that user cannot go in opposite direction of previous move
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (playing && delay == 0) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT && prev != KeyEvent.VK_RIGHT) {
                        snake.setVy(0);
                        snake.setVx(-SQUARE_VELOCITY);
                        prev = KeyEvent.VK_LEFT;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && prev != KeyEvent.VK_LEFT) {
                        snake.setVy(0);
                        snake.setVx(SQUARE_VELOCITY);
                        prev = KeyEvent.VK_RIGHT;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN && prev != KeyEvent.VK_UP) {
                        snake.setVx(0);
                        snake.setVy(SQUARE_VELOCITY);
                        prev = KeyEvent.VK_DOWN;
                    } else if (e.getKeyCode() == KeyEvent.VK_UP && prev != KeyEvent.VK_DOWN) {
                        snake.setVx(0);
                        snake.setVy(-SQUARE_VELOCITY);
                        prev = KeyEvent.VK_UP;
                    }
                    delay++;
                }
            }

        });

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        // Creates snake and stores value of 1 in array to signify snake piece
        snake = new Snake(COURT_WIDTH, COURT_HEIGHT, Color.GREEN);
        game[snake.getPx() / snake.getWidth()][snake.getPy() / snake.getHeight()] = 1;
        
        // Creates an enemy piece stores value of 2 in array to signify enemy piece
        Square enemy = new Square(COURT_WIDTH, COURT_HEIGHT, Color.RED);
        moveObj(enemy, 2);
        
        enemyList = new LinkedList<Square>();
        enemyList.add(enemy);
        
        // Creates an apple piece stores value of 3 in array to signify apple piece
        Square apple = new Square(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);
        // loop used to insure no pieces are already in location
        moveObj(apple, 3);
        appleList = new LinkedList<Square>();
        appleList.add(apple);
        
        
        // resets game state to initial conditions
        playing = true;
        status.setText("Running...");
        prev = 0;
        score = 0;
        time = 0;
        elCount = 1;
        enemyTimeCounter = 0;
        delay = 0;

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    // determines whether board is full
    public boolean winningBoard(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Randomly moves object on board
    private void moveObj(GameObj go, int type) {
        if (type == 2) {
            game[go.getPx() / go.getWidth()][go.getPy() / go.getHeight()] = 0;
        }
        int w = snake.getWidth();
        Point t = ng.generate();
        int x = (int) t.getX();
        int y = (int) t.getY(); 
        while (game[x][y] != 0) {
            t = ng.generate();
            x = (int) t.getX();
            y = (int) t.getY();
        }
        go.setPx(x * w);
        go.setPy(y * w);
        game[x][y] = type;
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            // increments time counter
            time += interval;
            if (status.getText() != "Running...") {
                status.setText("Running...");
            }
            
            delay = 0;
                        
            // moves enemy if certain amount of time has passed
            if (time >= 100 * interval) {
                for (Square s: enemyList) {
                    moveObj(s, 2);
                }
                time = 0;
            }
            if (enemyTimeCounter >= 8) {
                // after 8 apples have been eaten, a new enemy is generated
                Square s = new Square(COURT_WIDTH, COURT_HEIGHT, Color.RED);
                moveObj(s, 2);
                enemyList.add(s);
                enemyTimeCounter = 0;
                elCount += 1;
            }
            if (elCount >= 2) {
                // for every 2 enemies a new apple will be generated
                Square apple = new Square(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);
                moveObj(apple, 3);
                appleList.add(apple);
                elCount = 0;
            }
            if (winningBoard(game)) {
                // checks if the board is full
                playing = false;
                status.setText("You Win!");
                if (score > sp.getLowScore() || sp.highScoreLength() <= 3) {
                    sp.addScoreToHighScore(score);
                }
            }
            
            // set all the current locations occupied by snake to 0 (empty)
            for (Square s: snake.getCopyList()) {
                game[s.getPx() / s.getWidth()][s.getPy() / s.getHeight()] = 0;
            }
            
            snake.move();
            
            // set all the new locations occupied by snake to 1 
            for (Square s: snake.getCopyList()) {
                game[s.getPx() / s.getWidth()][s.getPy() / s.getHeight()] = 1;
            }
            
            // check if snake hits enemy and ends game
            if (snake.intersectsList(enemyList) != null) {
                playing = false;
                status.setText("You lose!");
                if (score > sp.getLowScore() || sp.highScoreLength() <= 3) {
                    sp.addScoreToHighScore(score);
                }
            } else if (snake.intersectsList(appleList) != null) {
                //check if snake intersects apple, increments score, and moves apple
                if (winningBoard(game)) {
                    playing = false;
                    status.setText("You Win!");
                    if (score > sp.getLowScore() || sp.highScoreLength() <= 3) {
                        sp.addScoreToHighScore(score);
                    }
                }
                Square apple = snake.intersectsList(appleList);
                moveObj(apple, 3);
                snake.appendSquare();
                score += 1;
                enemyTimeCounter += 1;
            } else if (snake.getClipCalled()) {
                // checks if snake runs into wall
                playing = false;
                status.setText("You lose!");
                if (score > sp.getLowScore() || sp.highScoreLength() <= 3) {
                    sp.addScoreToHighScore(score);
                }
            } else if (snake.selfIntersect()) {
                //checks if snake runs into self
                playing = false;
                status.setText("You lose!");
                if (score > sp.getLowScore() || sp.highScoreLength() <= 3) {
                    sp.addScoreToHighScore(score);
                }
            }
            // update the display
            repaint();
            
        }
    }
    
    
    // method used for pause button
    public void pause() {
        playing = !(playing);
        status.setText("Paused!");
        requestFocusInWindow();
    }
    
    public boolean getPlaying() {
        return playing;
    }
    
    public String getLabel() {
        return status.getText();
    }
    
    // used so nothing happens when game ends and pause is selected
    public void maintainState() {}
    
    // returns current game score
    public int getScore() {
        return score;
    }
    
    
    //Testing helper methods
    public int[][] gameState() {
        int[][] copy = new int[COURT_WIDTH / SQUARE_VELOCITY][COURT_HEIGHT / SQUARE_VELOCITY];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = game[i][j];
            }
        }
        return copy;
    }
    
    public int spacesOccupied() {
        int spaces = 0;
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[0].length; j++) {
                if (game[i][j] != 0) {
                    spaces += 1;
                }
            }
        }
        return spaces;
    }
    
    public LinkedList<Point> getLocations(int type) {
        LinkedList<Point> set = new LinkedList<Point>();
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[0].length; j++) {
                if (game[i][j] == type) {
                    Point p = new Point(i, j);
                    set.add(p);
                }
            }
        }
        return set;
    }
    
    public void setSnakePos(int x, int y) {
        game[snake.getPx() / snake.getWidth()][snake.getPy() / snake.getHeight()] = 0;
        snake.setPx(x);
        snake.setPy(y);
        game[x / snake.getWidth()][y / snake.getHeight()] = 1;
    }
    
    public void setEnemyPos(int x, int y) {
        Square s = enemyList.get(0);
        game[s.getPx() / s.getWidth()][s.getPy() / s.getHeight()] = 0;
        s.setPx(x);
        s.setPy(y);
        game[x / s.getWidth()][y / s.getHeight()] = 2;
    }
    
    public void setApplePos(int x, int y) {
        Square s = appleList.get(0);
        game[s.getPx() / s.getWidth()][s.getPy() / s.getHeight()] = 0;
        s.setPx(x);
        s.setPy(y);
        game[x / s.getWidth()][y / s.getHeight()] = 3;
    }
    
    public void setSnakeVel(int vx, int vy) {
        snake.setVx(vx);
        snake.setVy(vy);
    }
    
    
    // GameObj methods
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, COURT_WIDTH, COURT_HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, COURT_WIDTH, COURT_HEIGHT);
        snake.draw(g);
        for (Square enemy: enemyList) {
            enemy.draw(g);
        }
        for (Square apple: appleList) {
            apple.draw(g);
        }        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}