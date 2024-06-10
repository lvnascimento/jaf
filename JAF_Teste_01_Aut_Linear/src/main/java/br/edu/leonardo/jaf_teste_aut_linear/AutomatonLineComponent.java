package br.edu.leonardo.jaf_teste_aut_linear;

import br.edu.leonardo.jaf.AgentException;
import br.edu.leonardo.jaf.sensors.SensorInitializationException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.Duration;
import javax.swing.JComponent;

/**
 * A component that shows an automaton.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public class AutomatonLineComponent extends JComponent {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new AutomatonLineComponent containing an automaton with the given
     * number of cells, number of iterations, and initial configuration.
     * 
     * @param numberOfCells The number of cells in the automaton.
     * @param numberOfIterations The number of iterations of the automaton.
     * @param initialConfig The initial configuration of the automaton in a String, where "*" indicates
     *                      a living cell and "-" indicates a dead cell.
     */
    public AutomatonLineComponent(int numberOfCells, int numberOfIterations, String initialConfig) {
        // Configure the component size.
        setSize(numberOfCells * CELL_WIDTH, numberOfIterations * CELL_HEIGHT);
        
        // Create the states matrix.
        states = new GameOfLifeStates[numberOfIterations][numberOfCells];
        
        // Create the automaton.
        automata = new GameOfLifeLinearAutomaton(numberOfCells, Duration.ofMillis(500), initialConfig){
            @Override
            protected void onUpdate() {
                for(int i = 0; i < numberOfCells; i++) {
                    states[currentIteration][i] = getCellState(i);
                }
                currentIteration++;
                if(currentIteration == states.length)
                    stop();
                repaint();
            }
        };
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   C O N S T A N T S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * The width in pixels of each automaton cell.
     */
    private static final int CELL_WIDTH = 10;
    
    /**
     * The height in pixels of each automaton cell.
     */
    private static final int CELL_HEIGHT = 10;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This methd initializes this component and the internal automaton.
     */
    public void init() {
        try {
            automata.init();
        } catch (SensorInitializationException | AgentException ex) {
            ex.printStackTrace();
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        for(int i = 0; i < states.length; i++) {
            for(int j = 0; j < states[i].length; j++) {
                if(states[i][j] == GameOfLifeStates.ALIVE)
                    graphics.setPaint(Color.BLACK);
                else
                    graphics.setPaint(Color.WHITE);
                graphics.fillRect(j * CELL_HEIGHT, i * CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // The states matrix that saves the history of states of each automaton cell.
    private GameOfLifeStates[][] states;
    
    // The number of current iteration.
    private int currentIteration = 0;
    
    // The inner automaton.
    private final GameOfLifeLinearAutomaton automata;
}
