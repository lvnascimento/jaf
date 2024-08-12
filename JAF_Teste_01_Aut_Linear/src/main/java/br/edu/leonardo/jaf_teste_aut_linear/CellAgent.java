package br.edu.leonardo.jaf_teste_aut_linear;

import br.edu.leonardo.jaf.Agent;
import br.edu.leonardo.jaf.AgentException;
import br.edu.leonardo.jaf.Behaviour;
import br.edu.leonardo.jaf.sensors.SensorNotification;

/**
 * Each cell in the linear automata is an agent. This class is the implementation
 * of these agents. 
 * 
 * @author Leonardo Vianna do Nascimento
 * @param <T> The type used to represent cell states.
 */
public abstract class CellAgent<T> extends Agent {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new CellAgent that uses the given iteration sensor.
     * 
     * @param sensor The sensor that notifies new iterations of the automata to
     *               the agent.
     */
    public CellAgent(IterationSensor sensor) {
        this.sensor = sensor;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method updates the state of the agent (the state of the cell controlled by the agent).
     * The current state of the agent receives the next state and the next state is setted to null.
     */
    public void updateState() {
        if(nextState != null) {
            currentState = nextState;
            nextState = null;
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the current state of the agent (the current state of the cell controlled
     * by the agent).
     * 
     * @return The current state of the agent.
     */
    public T getCurrentState() {
        return currentState;
    }

    /**
     * This method changes the current state of the agent (the current state of the cell controlled
     * by the agent).
     * 
     * @param currentState The new current state of the agent.
     */
    public void setCurrentState(T currentState) {
        this.currentState = currentState;
    }

    /**
     * This method obtains the agent that controlled the cell at the right side of the cell 
     * controlled by this agent. 
     * 
     * @return The reference to agent; null if the cell is at the end of the automata line and does
     *         not have another cell at the right side.
     */
    public CellAgent getNextAgent() {
        return nextAgent;
    }

    /**
     * This method sets the agent that controlled the cell at the right side of the cell 
     * controlled by this agent. 
     * 
     * @param nextAgent The reference to agent; null if the cell is at the end of the automata line and does
     *                  not have another cell at the right side.
     */
    public void setNextAgent(CellAgent nextAgent) {
        this.nextAgent = nextAgent;
    }

    /**
     * This method obtains the agent that controlled the cell at the left side of the cell 
     * controlled by this agent.
     * 
     * @return The reference to agent; null if the cell is at the beginning of the automata 
     *         line and does not have another cell at the left side.
     */
    public CellAgent getPreviousAgent() {
        return previousAgent;
    }

    /**
     * This method sets the agent that controlled the cell at the left side of the cell 
     * controlled by this agent.
     * 
     * @param previousAgent The reference to agent; null if the cell is at the beginning of 
     *                      the automata line and does not have another cell at the left side.
     */
    public void setPreviousAgent(CellAgent previousAgent) {
        this.previousAgent = previousAgent;
    }
  
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void setup() throws AgentException {
        addBehaviour(
                new Behaviour() {
                    @Override
                    public void execute(SensorNotification notification) {
                        nextState = generateNextState(
                                previousAgent == null ? null : previousAgent.currentState, 
                                currentState, 
                                nextAgent == null ? null : nextAgent.currentState
                        );
                        onNextStateGenerated();
                    }
                }, 
                sensor
        );
    }
    
    /**
     * This method must be implemeted in concrete subclasses to specify how to obtain the agent's next
     * state from the given state of the previous cell (cell at the left), the current agent's state,
     * and the state of the next cell (the cell at the right).
     * 
     * @param previousCellState The state of the cell at the left side; null if there is no previous cell.
     * @param cellState The current state of the agent.
     * @param nextCellState The state of the cell at the right side; null if there is no next cell.
     * @return The next agent state.
     */
    protected abstract T generateNextState(T previousCellState, T cellState, T nextCellState);
    
    /**
     * This method should be implemented if subclasses want to specify something to be done after the 
     * agent generates his next state.
     */
    protected abstract void onNextStateGenerated();
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The current state of the agent (the current state of the cell controlled by the agent).
     */
    private T currentState;
    
    /**
     * The next state of the agent (the state to be held by the agent in the next iteration).
     */
    private T nextState;
    
    /**
     * The reference to the agent that controls the cell at the right side of the cell controlled
     * by this agent. This atribute is null if there is no cell at the right side.
     */
    private CellAgent<T> nextAgent;
    
    /**
     * The reference to the agent that controls the cell at the left side of the cell controlled
     * by this agent. This atribute is null if there is no cell at the left side.
     */
    private CellAgent<T> previousAgent;
    
    /**
     * The sensor that notifies this agent of each iteration of the automata. In an iteration, the
     * each cell os the automata generates its next state (the general state of the automata changes).
     */
    private final IterationSensor sensor;
}
