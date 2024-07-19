package br.edu.leonardo.jaf.net.mqtt;

/**
 * This listener listen to events in a MQTT connection.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public interface MqttClientListener {
    
    /**
     * This method is called when a message is received by a MQTT connection.
     * 
     * @param payload The payload of the received message.
     * @throws Exception If an error occurs during message processing.
     */
    public void onMessageReceived(byte[] payload) throws Exception;
    
    /**
     * This method is called when a connection is lost.
     * 
     * @param thrwbl The exception related to the connection lost.
     */
    public void onConnectionLost(Throwable thrwbl);
    
}
