package br.edu.leonardo.jaf;

import br.edu.leonardo.jaf.sensors.SensorNotification;

/**
 * An interface for implementation of classes implementing processes in agents.
 *
 * @author Leonardo Vianna do Nascimento
 */
public interface Process {

    /**
     * This method must implement the process main execution code for the processing of a sensor
     * notification.
     *
     * @param notification The notification received from a sensor.
     */
    public void execute(SensorNotification notification);
}
