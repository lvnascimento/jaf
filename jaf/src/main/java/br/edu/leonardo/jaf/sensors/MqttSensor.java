package br.edu.leonardo.jaf.sensors;

import br.edu.leonardo.jaf.net.mqtt.MqttClientConnection;
import br.edu.leonardo.jaf.net.mqtt.MqttClientListener;

/**
 * A sensor that obtains data from a MQTT client connection. This sensor can be used to interact with
 * IoT devices as agent sensors. Developers should extend this class to implement specific MQTT sensors.
 * The methos decodePayload must provide the necessary code to verify if a incoming message is related
 * to this sensor and decode its payload to a SensorValue.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public abstract class MqttSensor extends Sensor {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor creates a new MqttSensor that obtains data from the given MQTT client
     * connection.
     * 
     * @param mediator The MQTT client connection object.
     */
    public MqttSensor(MqttClientConnection mediator) {
        this.mediator = mediator;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void init() throws SensorException {
        // This sensor will listen for messages and can report connection losses.
        mediator.addListener(new MqttClientListener() {
            @Override
            public void onMessageReceived(byte[] payload) {
                receiveMqttPayload(payload);
            }

            @Override
            public void onConnectionLost(Throwable thrwbl) {
                reportFatalError(thrwbl);
            }
        });
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This method must provide the necessary code to decode a message payload to a SensorValue
     * object. This method should also verify if the message is related to this sensor and should
     * return null if the message should not be processed by this sensor.
     * 
     * @param payload The message payload.
     * @return The sensor value or null, if the payload should not be processed by this sensor.
     */
    protected abstract SensorValue decodePayload(byte[] payload);
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This method process a payload received from the MQTT connection.
     * 
     * @param payload The message payload.
     */
    private void receiveMqttPayload(byte[] payload) {
        SensorValue value = decodePayload(payload);
        if(value != null)
            newReading(value);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * The MQTT client connection used by this sensor.
     */
    private final MqttClientConnection mediator;
}
