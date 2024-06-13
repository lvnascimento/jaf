package br.edu.leonardo.jaf_moving_simulation;

import br.edu.leonardo.jaf.AgentException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 * The visual component that show the environment and agent simulation.
 * 
 * @author Leonardo Vianna do Nascimento.
 */
public class SimulationComponent extends JComponent {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor build a new SimulationComponent with the given environment and agent.
     * 
     * @param environment The environment of the simulation.
     * @param agent The agent that will appear in the simulation.
     */
    public SimulationComponent(Environment environment, SimulatedRobot agent) {
        this.environment = environment;
        this.agent = agent;
        setSize(environment.getWidth() * CELL_WIDTH, environment.getHeight() * CELL_HEIGHT);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes this component. The simulation robot is initialized.
     * 
     * @throws AgentException If an error occurred during robot initialization.
     */
    public void init() throws AgentException {
        agent.init();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        
        // Draw the environment. The environment is always surronded by walls.
        for(int i = -1; i < environment.getHeight()+1; i++) {
            for(int j = -1; j < environment.getWidth()+1; j++) {
                if(environment.isWall(j, i))
                    paintWall(graphics, j+1, i+1);
                else
                    paintSpace(graphics, j+1, i+1);
            }
        }
        
        // Draw the agent
        graphics.setPaint(Color.YELLOW);
        graphics.fillOval((agent.getX() + 1) * CELL_HEIGHT, (agent.getY() + 1) * CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method draws a wall in the environment.
     * 
     * @param graphics The graphics reference.
     * @param x The x coordinate of the top left corner of the wall.
     * @param y The y coordinate of the top left corner of the wall.
     */
    private void paintWall(Graphics2D graphics, int x, int y) {
        graphics.setPaint(Color.BLACK);
        graphics.fillRect(x * CELL_HEIGHT, y * CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
    }
    
    /**
     * This method draws an empty space in the environment.
     * 
     * @param graphics The graphics reference.
     * @param x The x coordinate of the top left corner of the empty space.
     * @param y The y coordinate of the top left corner of the empty space.
     */
    private void paintSpace(Graphics2D graphics, int x, int y) {
        graphics.setPaint(Color.WHITE);
        graphics.fillRect(x * CELL_HEIGHT, y * CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   C O N S T A N T S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * The width in pixels of each environment cell.
     */
    private static final int CELL_WIDTH = 20;
    
    /**
     * The height in pixels of each environment cell.
     */
    private static final int CELL_HEIGHT = 20;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The environment used in the simulation.
     */
    private final Environment environment;
    
    /**
     * The agent that will appear in the simulation.
     */
    private final SimulatedRobot agent;
}
