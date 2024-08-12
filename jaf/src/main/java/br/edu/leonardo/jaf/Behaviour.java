package br.edu.leonardo.jaf;

import br.edu.leonardo.jaf.sensors.SensorNotification;

/**
 * An interface for implementation of classes implementing behaviours in agents.
 *
 * @author Leonardo Vianna do Nascimento
 */
public interface Behaviour {

    /**
     * This method must implement the behaviour main execution code for the processing of a sensor
     * notification.
     *
     * @param notification The notification received from a sensor.
     */
    public void execute(SensorNotification notification);
}
