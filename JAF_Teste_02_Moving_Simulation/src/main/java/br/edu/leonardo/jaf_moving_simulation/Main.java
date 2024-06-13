package br.edu.leonardo.jaf_moving_simulation;

import br.edu.leonardo.jaf.AgentException;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author lvian
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // The user can select a file 
                    JFileChooser chooser = new JFileChooser();
                    int returnVal = chooser.showOpenDialog(null);
                    
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        // Creates the environment from the file selected by the user.
                        File file = chooser.getSelectedFile();
                        Environment env = Environment.fromFile(file);
                        
                        // Read the initial coordinates of the robot
                        int agentStartX = Integer.parseInt(JOptionPane.showInputDialog("Coordenada X inicial do agente: "));
                        int agentStartY = Integer.parseInt(JOptionPane.showInputDialog("Coordenada Y inicial do agente: "));
                        
                        // Create the robot
                        SimulatedRobot agent = new SimulatedRobot(env, agentStartX, agentStartY, Duration.ofMillis(100));
                        
                        // Create and show the main frame.
                        MainFrame frame = new MainFrame(env, agent);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setVisible(true);
                        
                        // Adds a listener to the agent to repaint the screen each time the agent moves.
                        agent.addListener(new RobotListener() {
                            @Override
                            public void onMove() {
                                frame.repaint();
                            }
                        });
                        
                        // Initialize the window
                        frame.init();
                    }
                } catch (FileNotFoundException | AgentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
