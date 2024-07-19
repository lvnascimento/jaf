package br.edu.leonardo.jaf_teste_03_iot_mqtt;

import br.edu.leonardo.jaf.net.mqtt.MqttClientConnection;
import br.edu.leonardo.jaf.sensors.BooleanSensorValue;
import br.edu.leonardo.jaf.sensors.MqttSensor;
import br.edu.leonardo.jaf.sensors.SensorValue;
import org.json.JSONObject;

/**
 * A sensor used on a parking space to detect if it is empty or not.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class ParkingSensor extends MqttSensor {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new ParkingSensor with the given id and that obtains data from the
     * given connection.
     * 
     * @param id The identification of the sensor.
     * @param connection The MQTT connection used by this sensor.
     */
    public ParkingSensor(String id, MqttClientConnection connection) {
        super(connection);
        this.id = id;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected SensorValue decodePayload(byte[] payload) {
        String json = new String(payload);
        if(json.matches("\\{\"id\":\""+id+"\",\"val\":(false|true)\\}")) {
            JSONObject jobj = new JSONObject(json);
            return BooleanSensorValue.getInstance(jobj.getBoolean("val"));
        }
        return null;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The identification of the sensor.
     */
    private final String id;
}
