package br.edu.leonardo.jaf_moving_simulation;

import br.edu.leonardo.jaf.sensors.BooleanSensorValue;
import br.edu.leonardo.jaf.Agent;
import br.edu.leonardo.jaf.Behaviour;
import br.edu.leonardo.jaf.sensors.SensorNotification;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * A robot in the simulation.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public class SimulatedRobot extends Agent {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor builds a new SimulatedRobot in the given environment, placed at the cell
     * in the given startX and starY coordinates and that moves at the given iteration period.
     * 
     * @param environment The environment where the robot is placed on.
     * @param startX The x coordinate of the initial position of the robot.
     * @param startY The y coordinate of the initial position of the robot.
     * @param iterationPeriod The time period between robot movements. 
     */
    public SimulatedRobot(Environment environment, int startX, int startY, Duration iterationPeriod) {
        this.x = startX;
        this.y = startY;
        
        IterationSensor iterSensor = new IterationSensor(this, iterationPeriod, environment);
        
        addBehaviour(new Behaviour() {
            @Override
            public void execute(SensorNotification notification) {
                if(notification.getValue() == BooleanSensorValue.TRUE)
                    changeDirection();
                else
                    move();
            }
        }, iterSensor);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method adds a new listener to this robot. A listener can execute actions when some events
     * related to the robot occur.
     * 
     * @param listener The listener.
     */
    public void addListener(RobotListener listener) {
        listeners.add(listener);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the x coordinate of the current position of the robot in the environment.
     * 
     * @return The value of x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * This method obtains the y coordinate of the current position of the robot in the environment.
     * 
     * @return The value of y coordinate.
     */
    
    public int getY() {
        return y;
    }
    
    /**
     * This method obtains the x coordinate of the possible next position of the robot in the 
     * environment, if the robot keeps the current direction.
     * 
     * @return The value of x coordinate.
     */
    public int getNextX() {
        return x + currentDirection.getIncX();
    }
    
    /**
     * This method obtains the y coordinate of the possible next position of the robot in the 
     * environment, if the robot keeps the current direction.
     * 
     * @return The value of y coordinate.
     */
    public int getNextY() {
        return y + currentDirection.getIncY();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method moves the agent one cell in the current direction. The method onMove of added
     * listeners is executed.
     */
    private void move() {
        x = getNextX();
        y = getNextY();
        listeners.forEach((listener) -> {
            listener.onMove();
        });
    }
    
    /**
     * This method changes the direction of the agent. The direction changes clockwise.
     */
    private void changeDirection() {
        if(currentDirection == MovementDirection.TO_LEFT)
            currentDirection = MovementDirection.TO_UP;
        else if(currentDirection == MovementDirection.TO_UP)
            currentDirection = MovementDirection.TO_RIGHT;
        else if(currentDirection == MovementDirection.TO_RIGHT)
            currentDirection = MovementDirection.TO_DOWN;
        else if(currentDirection == MovementDirection.TO_DOWN)
            currentDirection = MovementDirection.TO_LEFT;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The x coordinate of the current position of the agent.
     */
    private int x;
    
    /**
     * The y coordinate of the current position of the agent.
     */
    private int y;
    
    /**
     * The current direction of the robot.
     */
    private MovementDirection currentDirection = MovementDirection.TO_LEFT;
    
    /**
     * The list of listeners in this robot.
     */
    private final List<RobotListener> listeners = new ArrayList();
}
