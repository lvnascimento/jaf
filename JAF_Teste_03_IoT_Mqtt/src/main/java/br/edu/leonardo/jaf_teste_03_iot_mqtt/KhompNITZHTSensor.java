package br.edu.leonardo.jaf_teste_03_iot_mqtt;

import br.edu.leonardo.jaf.net.mqtt.MqttClientConnection;
import br.edu.leonardo.jaf.sensors.MqttSensor;
import br.edu.leonardo.jaf.sensors.SensorValue;
import br.edu.leonardo.jaf.sensors.SingleSensorValue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.measure.Unit;
import org.json.JSONObject;
import tec.units.ri.quantity.Quantities;

/**
 * A Khomp sensor (humidity or temperature) in a <a href="https://www.khomp.com/iot/pt/produto/endpoint-ieee-802-15-4/">NIT 21Z transmissor</a>.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class KhompNITZHTSensor extends MqttSensor {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor builds a new KhompNITZSensor identified by the given id and that obtains
     * data from the given MQTT connection. This sensor will process only objects that contains
     * the given name and unit key. The resulting sensor value will be a Quantity object related 
     * to the given unit.
     * 
     * @param id The sensor identification.
     * @param name The name in the payload object related to this sensor.
     * @param unitKey The unit key in the payload object related to this sensor.
     * @param unit The unit used in the values produced by this sensor.
     * @param connection The MQTT connection.
     */
    public KhompNITZHTSensor(String id, String name, String unitKey, Unit unit, MqttClientConnection connection) {
        super(connection);
        this.id = id;
        this.name = name;
        this.unitKey = unitKey;
        this.unit = unit;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This method obtains the identification of this sensor.
     * 
     * @return The sensor identification
     */
    public String getId() {
        return id;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    protected SensorValue decodePayload(byte[] payload) {
        String json = new String(payload).substring(2);
        json = json.substring(0, json.length()-1);
        if(json.matches(".*\"bn\":\""+id+"\".*\\{\""+KEY_NAME+"\":\""+name+"\",\""+KEY_UNIT+"\":\""+unitKey+"\",\""+KEY_VALUE_NUMBER+"\":\\d+\\.?\\d*\\}.*")) {
            Pattern pattern = Pattern.compile("\\{\""+KEY_NAME+"\":\""+name+"\",\""+KEY_UNIT+"\":\""+unitKey+"\",\""+KEY_VALUE_NUMBER+"\":\\d+\\.?\\d*\\}");
            Matcher matcher = pattern.matcher(json);
            if(matcher.find()) {
                JSONObject jobj = new JSONObject(matcher.group());
                return new SingleSensorValue(Quantities.getQuantity(jobj.getNumber(KEY_VALUE_NUMBER), unit));
            }
        }
        return null;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   C O N S T A N T S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    private static final String KEY_NAME = "n";
    private static final String KEY_UNIT = "u";
    private static final String KEY_VALUE_NUMBER = "v";
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The sensor identification
     */
    private final String id;
    
    /**
     * The name in the payload object related to this sensor.
     */
    private final String name;
    
    /**
     * The unit key in the payload object related to this sensor.
     */
    private final String unitKey;
    
    /**
     * The unit used in the values produced by this sensor.
     */
    private final Unit unit;
}
