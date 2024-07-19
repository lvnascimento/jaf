package br.edu.leonardo.jaf_teste_03_iot_mqtt;

import br.edu.leonardo.jaf.net.mqtt.MqttClientConnection;
import br.edu.leonardo.jaf.sensors.MqttSensor;
import br.edu.leonardo.jaf.sensors.SensorValue;
import javax.measure.Quantity;
import org.json.JSONArray;
import org.json.JSONObject;
import tec.units.ri.quantity.Quantities;
import tec.units.ri.unit.Units;

/**
 * A example of a <a href="https://www.khomp.com/iot/pt/produto/endpoint-ieee-802-15-4/">Khomp NIT 21ZI</a> 
 * sensor in a hypothetic smart city.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class KhompNITZSensor extends MqttSensor {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor builds a new KhompNITZSensor identified by the given id and that obtains
     * data from the given MQTT connection.
     * 
     * @param id The sensor identification.
     * @param connection The MQTT connection.
     */
    public KhompNITZSensor(String id, MqttClientConnection connection) {
        super(connection);
        this.id = id;
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

        // Check if the received payload contains the expected string pattern.
        if(json.matches(".*\"bn\":\""+id+"\".*")) {
            JSONArray ja = new JSONArray(json); // Parse received payload.
            
            // Variables that will store the received sensor data
            Quantity temperature = null;
            Quantity humidity = null;
            KhompSwitcherData c1 = null;
            KhompSwitcherData c2 = null;
            
            // For each received item
            for(int i = 0; i < ja.length(); i++) {
                Object obj = ja.get(i);
                
                if(obj instanceof JSONObject) {
                    JSONObject jobj = (JSONObject) obj;
                    String name = null;
                    Number valueNumber = null;
                    Boolean valueBool = null;
                    String unit = null;
                    
                    if(jobj.keySet().contains(KEY_NAME))
                        name = jobj.getString(KEY_NAME);
                    if(jobj.keySet().contains(KEY_VALUE_NUMBER))
                        valueNumber = jobj.getNumber(KEY_VALUE_NUMBER);
                    if(jobj.keySet().contains(KEY_VALUE_BOOLEAN))
                        valueBool = jobj.getBoolean(KEY_VALUE_BOOLEAN);
                    if(jobj.keySet().contains(KEY_UNIT))
                        unit = jobj.getString(KEY_UNIT);
                    
                    if("A".equals(name) && UNIT_CELSIUS.equals(unit) && valueNumber != null)
                        temperature = Quantities.getQuantity(valueNumber, Units.CELSIUS);
                    if("A".equals(name) && UNIT_PERCENT.equals(unit) && valueNumber != null)
                        humidity = Quantities.getQuantity(valueNumber, Units.PERCENT);
                    if("C1".equals(name) && valueBool != null) {
                        if(c1 == null)
                            c1 = new KhompSwitcherData();
                        c1.setOn(valueBool);
                    }
                    if("C1".equals(name) && UNIT_COUNT.equals(unit) && valueNumber != null) {
                        if(c1 == null)
                            c1 = new KhompSwitcherData();
                        c1.setCount(valueNumber.intValue());
                    }
                    if("C2".equals(name) && valueBool != null) {
                        if(c2 == null)
                            c2 = new KhompSwitcherData();
                        c2.setOn(valueBool);
                    }
                    if("C2".equals(name) && UNIT_COUNT.equals(unit) && valueNumber != null) {
                        if(c2 == null)
                            c2 = new KhompSwitcherData();
                        c2.setCount(valueNumber.intValue());
                    }
                } 
            }
            
            // Create and return the sensor value object
            KhompNITZSensorValue sv = new KhompNITZSensorValue(temperature, humidity, c1, c2);
            return sv;
        } else {
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   C O N S T A N T S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    private static final String KEY_NAME = "n";
    private static final String KEY_UNIT = "u";
    private static final String KEY_VALUE_NUMBER = "v";
    private static final String KEY_VALUE_BOOLEAN = "vb";
    
    private static final String UNIT_CELSIUS = "Cel";
    private static final String UNIT_PERCENT = "%RH";
    private static final String UNIT_COUNT = "count";
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The sensor identification
     */
    private final String id;
}
