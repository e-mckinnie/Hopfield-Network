package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides functionality to parse the data set. Also "cleans up the data";
 * reduces image size and converts the grayscale, images to black and white,
 * if needed (second getStates method)
 *
 */
public class DataParser {
    //cutoff between white and black
    private static final int THRESHOLD = 128;
    
    /**
     * Gets all of the states from the files (must be stored in data package) where the
     * image is black and white (stored as 1s and 0s) and doesn't need to be cropped
     * 
     * @param dataFileName file where data set is located
     * @requires dataFileName is valid
     * @return collection of states mapped by their names, which are the digits 0-9
     */
    public static Map<State, String> getStates(String dataFileName){
        Map<State, String> states = new HashMap<State, String>();
        try {
            BufferedReader readData = new BufferedReader(new FileReader(dataFileName));
            String line = readData.readLine();
            while (line != null) {
            String[] terms = line.split(",");
            String label = terms[0];
            int[] values = new int[terms.length - 1];
            for (int i = 1; i < terms.length; i++) {
                values[i-1] = Integer.parseInt(terms[i]);
            }
            states.put(new State(values), label);
            line = readData.readLine();
        }
        readData.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return states;
    }

    /**
     * Gets all of the states from the files (must be stored in data package) where the
     * image is greyscale and needs to be whittled down
     * 
     * @param dataFileName file where data set is located
     * @oldSize length of one side of the image
     * @newSize new length of one side of the image
     * @requires dataFileName is valid
     * @return collection of states mapped by their names, which are the digits 0-9
     */
    public static Map<State, String> getStates(String dataFileName, int oldSize, int newSize){
        Map<State, String> states = new HashMap<State, String>();
        try {
            BufferedReader readData = new BufferedReader(new FileReader(dataFileName));
            String line = readData.readLine();
            while (line != null) {
                String[] terms = line.split(",");
                String label = terms[0];
                int[][] values = new int[oldSize][oldSize];
                for (int i = 0; i < values.length; i++) {
                    for (int j = 0; j < values.length; j++) {
                        int index = oldSize * i + j;
                        int value = toBlackAndWhite(Integer.parseInt(terms[index + 1]));
                        values[i][j] = value;
                    }
                }
                states.put(new State(shrink(values, oldSize, newSize)), label);
                line = readData.readLine();
            }
            readData.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return states;
    }
    
    /*
     * Converts a SIZExSIZE pixel state into a newSizexnewSize one
     * 
     * @param state the image as represented by a list of ints
     * @requires state is SIZExSIZE
     * @returns new newSizexnewSize state
     */
    private static int[] shrink(int[][] values, int size, int newSize) {
        if (values.length != size) {
            throw new RuntimeException("state isn't " + size + " by " + size);
        }
        int chop = (size - newSize) / 2; //number of rows and columns to remove
        List<Integer> temp = new ArrayList<Integer>();
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                boolean firstRows = i < chop;
                boolean lastRows = i >= size - chop;
                boolean firstCols = j < chop;
                boolean lastCols = j >= size - chop;
                if (!firstRows && !lastRows && !firstCols && !lastCols) {
                    temp.add(values[i][j]);
                }
                       
            }
        }
        
        int[] compact = new int[newSize*newSize];
        for (int i = 0; i < temp.size(); i++) {
            compact[i] = temp.get(i);
        }
        return compact;
    }
    
    /*
     * Converts a single grayscale pixel into a black and white one
     * 
     * @param pixel pixel to be converted
     * @requires 0 <= pixel <= 255
     * @returns 0 (white) or 1 (black)
     */
    private static int toBlackAndWhite(int pixel) {
        if (pixel < THRESHOLD) {
            return 0;
        } else {
            return 1;
        }
    }

}
