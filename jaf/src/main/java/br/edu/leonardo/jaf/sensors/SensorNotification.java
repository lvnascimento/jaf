package br.edu.leonardo.jaf.sensors;

/**
 * A notification sent by a sensor. This notification contains the value notified by the sensor and
 * any other related information.
 *
 * @author Leonardo Vianna do Nascimento
 */
public class SensorNotification {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new SensorNotification with the given sensor and value.
     *
     * @param sensor The sensor that sent this notification.
     * @param value The value notified by the sensor.
     */
    public SensorNotification(Sensor sensor, SensorValue value) {
        this.sensor = sensor;
        this.value = value;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the sensor that sent this notification.
     *
     * @return The sensor reference.
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * The value notified by the sensor in this notification.
     *
     * @return The sensor value reference.
     */
    public SensorValue getValue() {
        return value;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The sensor that sent this notification.
     */
    private final Sensor sensor;

    /**
     * The value notified by the sensor.
     */
    private final SensorValue value;
}
