package br.edu.leonardo.jaf_teste_03_iot_mqtt;

import br.edu.leonardo.jaf.Agent;
import br.edu.leonardo.jaf.Behaviour;
import br.edu.leonardo.jaf.net.SSLUtils;
import br.edu.leonardo.jaf.net.mqtt.MqttClientConnection;
import br.edu.leonardo.jaf.sensors.BooleanSensorValue;
import br.edu.leonardo.jaf.net.NetworkException;
import br.edu.leonardo.jaf.sensors.SensorNotification;
import br.edu.leonardo.jaf.sensors.SingleSensorValue;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.swing.JOptionPane;
import tec.units.ri.unit.Units;

/**
 * The main class of this application
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, URISyntaxException {
        // Create agent
        Agent agent1 = new Agent();
        
        // Create the general topic that this application will subscribe
        String generalTopic = "technopolis/#";
        
        // The URL of the MQTT broker
        URI broker = new URI("ssl://bfe11f14.ala.eu-central-1.emqxsl.com:8883");
                
        try {
            // Create the connection object.
            MqttClientConnection subscriber = new MqttClientConnection();
            
            // Create sensor objects
            KhompNITZSensor sensorA = new KhompNITZSensor("F80332010002B33A", subscriber);
            KhompNITZSensor sensorB = new KhompNITZSensor("F80332010002B33B", subscriber);
            KhompNITZHTSensor sensorCTemp = new KhompNITZHTSensor("F80332010002B33C", "A", "Cel", Units.CELSIUS, subscriber);
            KhompNITZHTSensor sensorCHum = new KhompNITZHTSensor("F80332010002B33C", "A", "%RH", Units.PERCENT, subscriber);
            
            // Main thread
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Create the main window frame
                        MainWindow frame = new MainWindow();
                        
                        // Add sensors to the agent
                        agent1.addBehaviour(new Behaviour() {
                            @Override
                            public void execute(SensorNotification notification) {
                                frame.updateAlphaParkInfo((KhompNITZSensorValue)notification.getValue());
                            }
                        }, sensorA);
                        agent1.addBehaviour(new Behaviour() {
                            @Override
                            public void execute(SensorNotification notification) {
                                frame.updateBetaParkInfo((KhompNITZSensorValue)notification.getValue());
                            }
                        }, sensorB);
                        agent1.addBehaviour(new Behaviour() {
                            @Override
                            public void execute(SensorNotification notification) {
                                frame.updateDowntownTemp((SingleSensorValue)notification.getValue());
                            }
                        }, sensorCTemp);
                        agent1.addBehaviour(new Behaviour() {
                            @Override
                            public void execute(SensorNotification notification) {
                                frame.updateDowntownHumidity((SingleSensorValue)notification.getValue());
                            }
                        }, sensorCHum);

                        // Create parking sensors and add them to the agent
                        ParkingSensor[] pSensors = new ParkingSensor[7];
                        for(int i = 0; i < pSensors.length; i++) {
                            pSensors[i] = new ParkingSensor("PARKING"+(i+1), subscriber);
                            agent1.addBehaviour(new ParkingProcess(frame, i+1), pSensors[i]);
                        }
                        
                        // Initialize the agent
                        agent1.init();

                        // Set the SSL socket factory for the connection
                        String caCrtFile = "C:\\Users\\lvian\\Downloads\\emqxsl-ca.crt";
                        subscriber.setSocketFactory(SSLUtils.getOneWaySocketFactory(new File(caCrtFile), "TLS"));

                        // Connect to the broker and subscribe to the specified topic.
                        subscriber.connectAndSubscribe(broker, "teste", "teste".toCharArray(), generalTopic, 0);
                        
                        // Show the window
                        frame.setVisible(true);
                        
                        // Disconnect from the broker when the window is closed.
                        frame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                try {
                                    subscriber.disconnect();
                                } catch (NetworkException ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * A process that is executed when a parking sensor notifies values.
     */
    private static class ParkingProcess implements br.edu.leonardo.jaf.Behaviour {

        private final MainWindow frame;
        private final int spaceNo;

        public ParkingProcess(MainWindow frame, int spaceNo) {
            this.frame = frame;
            this.spaceNo = spaceNo;
        }
        
        @Override
        public void execute(SensorNotification notification) {
            frame.updateParkingSpaceState(spaceNo, (BooleanSensorValue) notification.getValue());
        }
        
    }
}
