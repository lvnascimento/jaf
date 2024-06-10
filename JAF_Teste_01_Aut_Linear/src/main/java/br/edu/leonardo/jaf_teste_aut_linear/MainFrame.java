package br.edu.leonardo.jaf_teste_aut_linear;

import java.awt.Frame;
import java.awt.HeadlessException;
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
     * This constructor builds a new MainFrame with an automaton configured with the given
     * number of cells, number of iterations, and initial config.
     * 
     * @param numberOfCells The number of cells in the automaton.
     * @param numberOfIterations The number of iterations.
     * @param initialConfig The initial config of the automaton.
     * @throws HeadlessException 
     */
    public MainFrame(int numberOfCells, int numberOfIterations, String initialConfig) throws HeadlessException {
        setTitle("Autômato Celular Linear Multi-agente");
        panel = new AutomatonLineComponent(numberOfCells, numberOfIterations, initialConfig);
        add(panel);
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes the frame.
     */
    public void init() {
        panel.init();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The component containing the automaton visualization.
     */
    private final AutomatonLineComponent panel;
}
