package br.edu.leonardo.jaf_teste_aut_linear;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Main class of this test program.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int numberOfCells = Integer.parseInt(JOptionPane.showInputDialog("Número de células: "));
                int numberOfIterations = Integer.parseInt(JOptionPane.showInputDialog("Número de iterações: "));
        
                String initialConfig = JOptionPane.showInputDialog("Configuração inicial (use \"*\" para células vivas e \"-\" para células mortas):");
         
                MainFrame frame = new MainFrame(numberOfCells, numberOfIterations, initialConfig);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.init();
            }
        });
    }
}