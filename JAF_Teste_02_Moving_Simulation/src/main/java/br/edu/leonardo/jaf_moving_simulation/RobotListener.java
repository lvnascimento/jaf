package br.edu.leonardo.jaf_moving_simulation;

/**
 * This listener is used to listen to robot events.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public interface RobotListener {
    
    /**
     * This method is executed by a robot after it moves.
     */
    public void onMove();
}
