package br.edu.leonardo.jaf_moving_simulation;

/**
 * A direction of a movement. A direction specifies how the robot coordinates (x and y) are
 * incremented.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public class MovementDirection {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T A N T S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Left movement.
     */
    public static final MovementDirection TO_LEFT = new MovementDirection(-1, 0);
    
    /**
     * Right movement.
     */
    public static final MovementDirection TO_RIGHT = new MovementDirection(+1, 0);
    
    /**
     * Up movement.
     */
    public static final MovementDirection TO_UP = new MovementDirection(0, -1);
    
    /**
     * Down movement.
     */
    public static final MovementDirection TO_DOWN = new MovementDirection(0, +1);
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor builds a new MovementDirection with the given increments.
     * 
     * @param incX The increment on the x coordinate; a negative number causes a decrement.
     * @param incY The increment on the y coordinate; a negative number causes a decrement.
     */
    public MovementDirection(int incX, int incY) {
        this.incX = incX;
        this.incY = incY;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the increment on the x coordinate defined in this object. A negative 
     * number causes a decrement.
     * 
     * @return The increment value.
     */
    public int getIncX() {
        return incX;
    }

    /**
     * This method obtains the increment on the y coordinate defined in this object. A negative 
     * number causes a decrement.
     * 
     * @return The increment value.
     */
    public int getIncY() {
        return incY;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The increment on the x coordinate defined in this object. A negative number causes a 
     * decrement.
     */
    private final int incX;
    
    /**
     * The increment on the y coordinate defined in this object. A negative number causes a 
     * decrement.
     */
    private final int incY;

    

    
}
