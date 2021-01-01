/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.
               

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        
        
        final JFrame frame = new JFrame("Snake");
        frame.setLocation(300, 300);
        
        final ScoreParser scoreP = new ScoreParser("files/HighScore.txt");
        
        final JPanel m = new JPanel();
        
        // menu text; spaced weirdly for centering purposes
        final JLabel m1 = new JLabel("                                        "
                + "                                 Welcome to Snake!");
        final JLabel m2 = new JLabel("                                  This is just like "
                + "your traditional Snake game, "
                + "but with a few twists.                                     ");
        final JLabel m3 = new JLabel("                                                          "
                + "You (the green snake) will try to capture");
        final JLabel m4 = new JLabel("                    "
                + "as many apples (yellow squares) as you can by moving"
                + " around with the arrow keys...");
        final JLabel m5 = new JLabel("                                               "
                + "but watch out for the sneaky enemies (red squares).");
        final JLabel m6 = new JLabel("                                                  "
                + "Beware, the game gets harder as you progress!!");
        final JLabel m7 = new JLabel("                   "
                + "More apples and enemies will randomly appear when your "
                + "score reaches a certain value.");
        final JLabel space = new JLabel("");
        
        final JLabel high = new JLabel("                                        "
                + "                                      High Scores");
        
        final JLabel h1 = new JLabel("                                                "
                + "                      1. " + scoreP.getDisplayScores()[0]);
        
        final JLabel h2 = new JLabel("                                                "
                + "                      2. " + scoreP.getDisplayScores()[1]);
        
        final JLabel h3 = new JLabel("                                                "
                + "                      3. " + scoreP.getDisplayScores()[2]);
        
        final JButton start = new JButton("Play");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(m);
                
                final JLabel status = new JLabel("Running...");
                
                final GameBoard board = new GameBoard(status);
                
                // Status panel
                final JPanel status_panel = new JPanel();
                frame.add(status_panel, BorderLayout.SOUTH);
                status_panel.add(status);

                // Main playing area
                frame.add(board, BorderLayout.CENTER);

                
                final JPanel control_panel = new JPanel();
                frame.add(control_panel, BorderLayout.NORTH);
                
                // displays in game score
                JLabel scoreBoard = new JLabel("               Score: " + board.getScore());
                ActionListener al = new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        scoreBoard.setText("                Score: " + board.getScore());
                    }
                };

                Timer t1 = new Timer(100, al);
                t1.start();
                
                // shows top high score in game and updates if player passes score
                JLabel highScoreBoard = new JLabel("        High Score: " + scoreP.getHighScore());
                ActionListener al1 = new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        if (board.getScore() > scoreP.getHighScore()) {
                            highScoreBoard.setText("        High Score: " + board.getScore());
                        } else if (scoreP.getDisplayScores()[0] == "") {
                            highScoreBoard.setText("        High Score: " + 0);
                        } else {
                            highScoreBoard.setText("        High Score: " + scoreP.getHighScore());
                        }
                    }
                };
                Timer t = new Timer(100, al1);
                t.start();
                control_panel.add(scoreBoard);

                // Reset button
                final JButton reset = new JButton("Reset");
                reset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.reset();
                    }
                });
                
                ActionListener al3 = new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        if (board.getLabel() == "You lose!") {
                            reset.setText("Play Again");
                        } else {
                            reset.setText("Reset");
                        }
                    }
                };

                Timer t3 = new Timer(100, al3);
                t3.start();
                
                // Pause button
                final JButton pause = new JButton("Pause");
                pause.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (board.getLabel() == "You lose!") {
                            board.maintainState();
                        } else {
                            board.pause();
                            if (board.getPlaying()) {
                                pause.setText("Pause");
                            } else {
                                pause.setText("Resume");
                            }
                        }
                    }
                });
                
                control_panel.setLayout(new GridLayout(1, 3));
                
                control_panel.add(scoreBoard);
                
                control_panel.add(reset);
                
                control_panel.add(pause);
                
                control_panel.add(highScoreBoard, BorderLayout.EAST);
                
                board.reset();

            }
        });
        
        m.setLayout(new GridLayout(20, 1));
        
        JPanel m1r = new JPanel();
        m1r.setLayout(new GridLayout(1,3));
        m1r.add(m1);
        
        m.add(space);
        m.add(space);
        m.add(space);
        m.add(space);
        m.add(space);
        m.add(m1r);
        m.add(m2);
        m.add(m3);
        m.add(m4);
        m.add(m5);
        m.add(m6);
        m.add(m7);
        m.add(start);
        m.add(high);
        m.add(h1);
        m.add(h2);
        m.add(h3);
        frame.add(m);


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}