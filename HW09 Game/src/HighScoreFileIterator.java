import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

class HighScoreFileIterator implements Iterator<String> {
    
    private BufferedReader br;
    
    private String currentScore;
    
    // constructor that checks for null/invalid files
    public HighScoreFileIterator(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null.");
        } 
        try {
            FileReader f = new FileReader(filePath);
            br = new BufferedReader(f);
            try {
                currentScore = br.readLine();
            } catch (IOException e) {
                currentScore = null;
                try {
                    br.close();
                } catch (IOException e1) {
                }
                throw new NoSuchElementException("Reached end");
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
    }
    
    @Override
    public boolean hasNext() {
        return currentScore != null;
    }

    @Override
    public String next() {
        if (currentScore == null) {
            throw new NoSuchElementException("Reached end");
        }
        String s = currentScore;
        try {
            currentScore = br.readLine();
        } catch (IOException e) {
            currentScore = null;
            try {
                br.close();
            } catch (IOException e1) {
            }
        }
        return s;
    }
    
}