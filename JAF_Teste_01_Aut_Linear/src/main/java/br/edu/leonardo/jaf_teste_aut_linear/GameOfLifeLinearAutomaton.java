package br.edu.leonardo.jaf_teste_aut_linear;

import java.time.Duration;

/**
 * The implementation of a generic Game of Life linear cellular automaton.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public abstract class GameOfLifeLinearAutomaton extends LinearCellularAutomaton<GameOfLifeStates>{

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new GameOfLifeLinearAutomaton with the given numCells, updatePeriod,
     * and initialConfig.
     * 
     * @param numCells The number of cells in the automaton.
     * @param updatePeriod The time period between successive updates.
     * @param initialConfig The initial configuration of the automaton in a String, where "*" indicates
     *                      a living cell and "-" indicates a dead cell.
     */
    public GameOfLifeLinearAutomaton(int numCells, Duration updatePeriod, String initialConfig) {
        super(numCells, updatePeriod);
        
        for(int i = 0; i < numCells; i++) {
            if(i >= initialConfig.length() || initialConfig.charAt(i) == '-')
                setCellState(i, GameOfLifeStates.DEAD);
            else
                setCellState(i, GameOfLifeStates.ALIVE);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected GameOfLifeStates generateNextCellState(GameOfLifeStates previousCellState, GameOfLifeStates cellState, GameOfLifeStates nextCellState) {
        if(previousCellState == null)
            previousCellState = GameOfLifeStates.DEAD;
        if(nextCellState == null)
            nextCellState = GameOfLifeStates.DEAD;
        
        if(cellState == GameOfLifeStates.ALIVE) {
            if(previousCellState == nextCellState)
                return GameOfLifeStates.DEAD;
            else
                return GameOfLifeStates.ALIVE;
        } else {
            if(previousCellState == GameOfLifeStates.ALIVE || nextCellState == GameOfLifeStates.ALIVE)
                return GameOfLifeStates.ALIVE;
            else
                return GameOfLifeStates.DEAD;
        }
    }    
}
