package br.edu.leonardo.jaf_moving_simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The environment of a simulation. The environment is a matrix of cells where each cell can
 * contain a wall or an empty space.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public class Environment {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T A N T S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * The wall character.
     */
    public static final char WALL = '*';
    
    /**
     * The empty space character.
     */
    public static final char EMPTY_SPACE = ' ';
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor builds a new Environment with the given width and height.
     * 
     * @param width The width in cells of the environment.
     * @param height The height in cells of the environment. 
     */
    public Environment(int width, int height) {
        this.width = width;
        this.matrix = new char[height][width];
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method creates an Environment from a given file. The first line of the file must contain
     * two positive integers separated by a space that specify, respectively, the width and height of
     * the environment. The remaining lines contain the environment drawing using * and space characters.
     * 
     * @param file The file containing the environment specification.
     * @return The created environment.
     * @throws FileNotFoundException If the specified file does not exist.
     */
    public static Environment fromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        
        // Load the width and height.
        int w = scanner.nextInt();
        int h = scanner.nextInt();
        scanner.nextLine();
        
        // Load cell specifications.
        Environment env = new Environment(w, h);
        int lineNo = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for(int i = 0; i < w; i++) {
                if(i < line.length())
                    env.setPosition(i, lineNo, line.charAt(i));
                else
                    env.addEmptySpace(i, lineNo);
            }
            lineNo++;
        }
        return env;
    }
    
    /**
     * This method checks if a given position in the environment contains a wall or not.
     * 
     * @param x The x coordinate of the desired position.
     * @param y The y coordinate of the desired position.
     * @return true if the position contains a wall; false otherwise.
     */
    public boolean isWall(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= matrix.length)
            return true;
        return getPosition(x, y) == WALL;
    }
    
    /**
     * This method sets a cell content as a wall.
     * 
     * @param x The x coordinate of the desired cell in the environment.
     * @param y The y coordinate of the desired cell in the environment.
     */
    public void addWall(int x, int y) {
        setPosition(x, y, WALL);
    }
    
    /**
     * This method checks if a given position in the environment contains an empty space
     * or not.
     * 
     * @param x The x coordinate of the desired position.
     * @param y The y coordinate of the desired position.
     * @return true if the position contains an empty space; false otherwise.
     */
    public boolean isEmptySpace(int x, int y) {
        return !isWall(x, y);
    }
    
    /**
     * This method sets a cell content as an empty space.
     * 
     * @param x The x coordinate of the desired cell in the environment.
     * @param y The y coordinate of the desired cell in the environment.
     */
    public void addEmptySpace(int x, int y) {
        setPosition(x, y, EMPTY_SPACE);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the environment width in cells.
     * 
     * @return The environment width. 
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * This method obtains the environment height in cells.
     * 
     * @return The environment height. 
     */
    public int getHeight() {
        return matrix.length;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the content of the cell in the given coordinates.
     * 
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @return The cell content.
     */
    private char getPosition(int x, int y) {
        return matrix[y][x];
    }
    
    /**
     * This method changes the content of the cell in the given coordinates.
     * 
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @param ch The new cell content.
     */
    private void setPosition(int x, int y, char ch) {
        matrix[y][x] = ch;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The matrix containg the cell contents of the environment.
     */
    private final char[][] matrix;
    
    /**
     * The width of the environment in cells.
     */
    private final int width;
}
