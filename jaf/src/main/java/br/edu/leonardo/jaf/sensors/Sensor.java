package br.edu.leonardo.jaf.sensors;

import java.util.HashSet;
import java.util.Set;

/**
 * A sensor that can be used by an agent. An agent uses a sensor to obtain information about its
 * environment and other external data.
 *
 * @author Leonardo Vianna do Nascimento
 */
public abstract class Sensor {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method must initialize the sensor object. It must be implemented in concrete subclasses
     *
     * @throws SensorInitializationException If an error occurred during sensor initialization.
     */
    public abstract void init() throws SensorInitializationException;

    /**
     * This method adds a new listener to this sensor. A listener will listen for notifications.
     * This sensor will execute the method "notify" of the listener when a new value must be notified.
     *
     * @param listener The desired listener.
     */
    public void addListener(NotificationListener listener) {
        observers.add(listener);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method receives a new value notification from the sensor. The subclasses of Sensor
     * should use this method to process new values.
     *
     * @param value The new value as a SensorValue reference.
     */
    protected final void newReading(SensorValue value) {
        SensorNotification notif = new SensorNotification(this, value);
        for (NotificationListener obs : observers) {
            obs.notify(notif);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The set of listeners added to this sensor.
     */
    private final Set<NotificationListener> observers = new HashSet();
}
