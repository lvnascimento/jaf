package br.edu.leonardo.jaf_moving_simulation;

import br.edu.leonardo.jaf.AgentException;
import java.awt.Frame;
import javax.swing.JFrame;

/**
 * The main windows of the application.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public class MainFrame extends JFrame {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new MainFrame with the given environment and agent.
     * 
     * @param environment The environment of the simulation.
     * @param agent The agent that will appear in the simulation.
     */
    public MainFrame(Environment environment, SimulatedRobot agent) {
        setTitle("Simulador de robô se movimentando em ambiente com obstáculos");
        panel = new SimulationComponent(environment, agent);
        add(panel);
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes the frame.
     */
    public void init() throws AgentException {
        panel.init();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The component containing the simulation visualization.
     */
    private final SimulationComponent panel;
}