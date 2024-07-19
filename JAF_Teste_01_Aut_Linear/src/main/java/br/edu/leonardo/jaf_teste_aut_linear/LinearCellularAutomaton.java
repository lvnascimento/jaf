package br.edu.leonardo.jaf_teste_aut_linear;

import br.edu.leonardo.jaf.AgentException;
import br.edu.leonardo.jaf.sensors.SensorException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic linear cellular automaton. 
 * 
 * @author Leonardo Vianna do Nascimento
 * @param <T> The type used to represent states.
 */
public abstract class LinearCellularAutomaton<T> {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new LinearCellularAutomaton with the given number of cells and
     * update period.
     * 
     * @param numCells Number of cells in the automaton.
     * @param updatePeriod The time period between successive updates.
     */
    public LinearCellularAutomaton(int numCells, Duration updatePeriod) {
        
        internalSensor = new IterationSensor(updatePeriod);
        
        // Create the automaton cells.
        cells = new ArrayList(numCells); 
        for(int i = 0; i < numCells; i++) {
            CellAgent<T> c = new CellAgent<T>(internalSensor) {
                @Override
                protected T generateNextState(T previousCellState, T cellState, T nextCellState) {
                   return generateNextCellState(previousCellState, cellState, nextCellState);
                }

                @Override
                protected void onNextStateGenerated() {
                    notifyCellUpdate();
                }
                
            };
            cells.add(c);
            
            // Update the references to the previous and next agents.
            if(i > 0) {
                CellAgent<T> prevCell = cells.get(i - 1);
                c.setPreviousAgent(prevCell);
                prevCell.setNextAgent(c);
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes the automaton. The onUpdate method is executed, all cell agents
     * are initializes, and the iteration sensor of the automaton is initialized too.
     * 
     * @throws SensorException If an error occured during sensor initialization.
     * @throws AgentException If an error occurred during agent initialization.
     */
    public void init() throws SensorException, AgentException {
        onUpdate();
        for(CellAgent c : cells) {
            c.init(false);
        }
        internalSensor.init();
    }
    
    /**
     * This method stops the automaton iterations.
     */
    public void stop() {
        internalSensor.stop();
    }
    
    /**
     * This method obtains the number of cells in the automaton.
     * @return 
     */
    public int getNumOfCells() {
        return cells.size();
    }
    
    /**
     * This method obtains the state of the cell "i" in the automaton.
     * 
     * @param i The cell index (the first cell is in the index zero).
     * @return The cell state.
     */
    public T getCellState(int i) {
        return cells.get(i).getCurrentState();
    }

    /**
     * This method changes the state of the cell "i" in the automaton.
     * 
     * @param i The cell index (the first cell is in the index zero).
     * @param state The new cell state.
     */
    public void setCellState(int i, T state) {
        cells.get(i).setCurrentState(state);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the reference to the sensor used by this automaton to notify iterations
     * to the cell agents.
     * 
     * @return The sensor reference.
     */
    public IterationSensor getInternalSensor() {
        return internalSensor;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method must be implemeted in concrete subclasses to specify how to obtain a cell's next
     * state from the given state of the previous cell (cell at the left), the current cell's state,
     * and the state of the next cell (the cell at the right).
     * 
     * @param previousCellState The state of the cell at the left side; null if there is no previous cell.
     * @param cellState The current state of the cell.
     * @param nextCellState The state of the cell at the right side; null if there is no next cell.
     * @return The next cell state.
     */
    protected abstract T generateNextCellState(T previousCellState, T cellState, T nextCellState);
    
    /**
     * This method must be implemented in concrete subclasses to specify what will be done after each
     * automaton update.
     */
    protected abstract void onUpdate();
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method updates all cells in the automaton.
     */
    private void updateCells() {
        for(CellAgent c : cells) {
            c.updateState();
        }
        onUpdate();
    }
    
    /**
     * This method is used to notify the automaton that one of its cells updated his next state.
     */
    private synchronized void notifyCellUpdate() {
        internalCounter++;
        if(internalCounter == cells.size()) {
            updateCells();
            internalCounter = 0;
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The cells of the automaton.
     */   
    private List<CellAgent<T>> cells;
    
    /**
     * The reference to the sensor used by this automaton to notify iterations to the cell agents.
     */
    private final IterationSensor internalSensor;
    
    /**
     * The internal counter used to count the number of agents that notified updates in each iteration.
     * This counter is set to zero when a new iteration starts.
     */
    private int internalCounter;
}
