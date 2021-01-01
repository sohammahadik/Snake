import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

// Class that deals with putting/getting high scores
public class ScoreParser {
    
    String fileName;
    
    
    //constructor
    public ScoreParser(String file) {
        if (file == null) {
            throw new IllegalArgumentException("Cannot pass an null file");
        }
        try {
            FileReader f = new FileReader(file);
            try {
                f.close();
            } catch (IOException e) {
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Cannot pass an invalid file");
        }
        
        fileName = file;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    // helper method
    private String highScoreAndDate(String s) {
        if (s == null) {
            return null;
        }
        String[] split = s.split(" ");
        if (split.length < 3) {
            return null;
        }
        return split[0] + " on " + split[1];
    }
    
    // sorts all scores in ascending order
    private ArrayList<Integer> getSortedScores() {
        ArrayList<Integer> scores = new ArrayList<>();
        HighScoreFileIterator hsfi = new HighScoreFileIterator(fileName);
        while (hsfi.hasNext()) {
            String s = hsfi.next();
            String[] split = s.split(" ");
            int i = Integer.parseInt(split[0]);
            scores.add(i);
        }
        scores.sort(null);
        return scores;
    }
    
    // gets data (time and date) of a certain score, stored in linked list by 
    // chronological order 
    private TreeMap<Integer, LinkedList<String>> getScoresData() {
        TreeMap<Integer, LinkedList<String>> scoresData = new TreeMap<>();
        HighScoreFileIterator hsfi = new HighScoreFileIterator(fileName);
        while (hsfi.hasNext()) {
            String s = hsfi.next();
            String[] split = s.split(" ");
            int i = Integer.parseInt(split[0]);
            String[] dateFormat = split[1].split("-");
            String d = " on " + dateFormat[1] + "/" + dateFormat[0] + "/" + dateFormat[2];
            if (scoresData.containsKey(i)) {
                LinkedList<String> ls = scoresData.get(i);
                ls.add(d);
                scoresData.put(i, ls);
            } else {
                LinkedList<String> lls = new LinkedList<>();
                lls.add(d);
                scoresData.put(i, lls);
            }
            
        }
        return scoresData;
    }
    
    // returns high scores string[] that gets displayed in menu (Score on mm/ss/yyyy)
    public String[] getDisplayScores() {
        ArrayList<Integer> ali = getSortedScores();
        TreeMap<Integer, LinkedList<String>> scoresData = getScoresData();
        String[] display = new String[3];
        if (ali.size() < 3) {
            String hs = "";
            String ms = "";
            String ls = "";
            for (int i = 0; i < ali.size(); i++) {
                int score = 0;
                if (i == 0) {
                    score = ali.get(ali.size() - 1);
                } else if (i == 1) {
                    score = ali.get(ali.size() - 2);
                } else if (i == 2) {
                    score = ali.get(ali.size() - 3);
                }
                if (hs == "") {
                    hs = score + scoresData.get(score).get(0);
                } else if (ms == "") {
                    if (hs != "" && hs.split(" ")[0] == Integer.toString(score)) {
                        ms = score + scoresData.get(score).get(1);
                    } else {
                        ms = score + scoresData.get(score).get(0);
                    }
                } else if (ls == "") {
                    if (hs != "" && hs.split(" ")[0] == Integer.toString(score)) {
                        if (ms != "" && ms.split(" ")[0] == Integer.toString(score)) {
                            ls = score + scoresData.get(score).get(2);
                        } else {
                            ls = score + scoresData.get(score).get(1);
                        }
                    } else {
                        if (ms != "" && ms.split(" ")[0] == Integer.toString(score)) {
                            ls = score + scoresData.get(score).get(1);
                        } else {
                            ls = score + scoresData.get(score).get(0);
                        }
                    }
                }
                
            }
            display[0] = hs;
            display[1] = ms;
            display[2] = ls;
        } else {
            int hs = ali.get(ali.size() - 1);
            int ms = ali.get(ali.size() - 2);
            int ls = ali.get(ali.size() - 3);
            display[0] = hs + scoresData.get(hs).get(0);
            if (ms == hs) {
                display[1] = ms + scoresData.get(ms).get(1);
            } else {
                display[1] = ms + scoresData.get(ms).get(0);
            }
            if (ls == ms) {
                if (ls == hs) {
                    display[2] = ls + scoresData.get(ls).get(2);
                } else {
                    display[2] = ls + scoresData.get(ls).get(1);
                }
            } else {
                display[2] = ls + scoresData.get(ls).get(0);
            }
        }
        return display;
    }
    
    // gets the high score of all games
    public int getHighScore() {
        ArrayList<Integer> ali = getSortedScores();
        if (ali.size() == 0) {
            return 0;
        }
        return ali.get(ali.size() - 1);
    }
        
    // gets lowest score in highscore file
    public int getLowScore() {
        ArrayList<Integer> ali = getSortedScores();
        if (ali.size() == 0) {
            return 0;
        }
        return ali.get(0);
    }
    
    // gets number of scores in file
    public int highScoreLength() {
        HighScoreFileIterator hsfi = new HighScoreFileIterator(fileName);
        int count = 0;
        while (hsfi.hasNext()) {
            hsfi.next();
            count += 1;
        }
        return count;
    }
    
    // returns high score string that gets displayed in menu (Score on mm/ss/yyyy)
    public String highScoreDisplay() {
        int score = 0;
        String highScore = "";
        HighScoreFileIterator hsfi = new HighScoreFileIterator(fileName);
        while (hsfi.hasNext()) {
            String s = hsfi.next();
            String date = highScoreAndDate(s);
            String[] split = date.split(" ");
            int hs = 0;
            try {
                hs = Integer.parseInt(split[0]);
            } catch (NumberFormatException e) {
                hs = -1;
            }
            if (hs > score) {
                score = hs;
                String[] dateFormat = split[1].split("-");
                String d = " on " + dateFormat[1] + "/" + dateFormat[0] + "/" + dateFormat[2];
                highScore = hs + d;
            }
        }
        return highScore;
    }
    
    // writes into scores into file with date and time
    public void addScoreToHighScore(int value) {
        File file = Paths.get(fileName).toFile();
        StringWriter outputWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(outputWriter);
        HighScoreFileIterator hsfi = new HighScoreFileIterator(fileName);
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
        LinkedList<String> lst = new LinkedList<>();
        while (hsfi.hasNext()) {
            String g = hsfi.next();
            lst.add(g);
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String d = formatter.format(date);
        String s = Integer.toString(value);
        s = s + " " + d;
        lst.add(s);
        for (String str: lst) {
            pw.write(str);
            pw.append("\n");
        }
        pw.close();
    }
    
    // test method that cleans file before ScoreParser methods are called
    public void fileClean() {
        File file = Paths.get(fileName).toFile();
        StringWriter outputWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(outputWriter);
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
        pw.write("");
        pw.close();
    }
    
}