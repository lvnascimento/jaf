package br.edu.leonardo.jaf_teste_03_iot_mqtt;

import br.edu.leonardo.jaf.sensors.SensorValue;
import javax.measure.Quantity;

/**
 * A sensor value produced by <a href="https://www.khomp.com/iot/pt/produto/endpoint-ieee-802-15-4/">Khomp NIT 21ZI</a> 
 * sensors.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class KhompNITZSensorValue implements SensorValue {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This constructor builds a new KhompNITZSensorValue with the given temperature, humidity, and
     * values of switchers 1 and 2.
     * 
     * @param temperature The temperature value.
     * @param humidity The humidity value.
     * @param switcher1 Data related to switcher 1 state.
     * @param switcher2 Data related to switcher 2 state.
     */
    public KhompNITZSensorValue(Quantity temperature, Quantity humidity, KhompSwitcherData switcher1, KhompSwitcherData switcher2) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.switcher1 = switcher1;
        this.switcher2 = switcher2;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This method obtains the temperature value in this object.
     * 
     * @return The temperature. 
     */
    public Quantity getTemperature() {
        return temperature;
    }

    /**
     * This method obtains the humidity value in this object.
     * 
     * @return The humidity 
     */
    public Quantity getHumidity() {
        return humidity;
    }

    /**
     * This method obtains the state of the switcher 1 in this object.
     * 
     * @return The state of switcher 1. 
     */
    public KhompSwitcherData getSwitcher1() {
        return switcher1;
    }

    /**
     * This method obtains the state of the switcher 2 in this object.
     * 
     * @return The state of switcher 2. 
     */
    public KhompSwitcherData getSwitcher2() {
        return switcher2;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // T O   S T R I N G ,   E Q U A L S ,   A N D   H A S H C O D E
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        return "KhompNITZSensorValue{" + "temperature=" + temperature + ", humidity=" + humidity + ", switcher1=" + switcher1 + ", switcher2=" + switcher2 + '}';
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The temperature value in this object.
     */
    private final Quantity temperature;
    
    /**
     * The humidity value in this object.
     */
    private final Quantity humidity;
    
    /**
     * Data related to switcher 1 state.
     */
    private final KhompSwitcherData switcher1;
    
    /**
     * Data related to switcher 2 state.
     */
    private final KhompSwitcherData switcher2;
}
