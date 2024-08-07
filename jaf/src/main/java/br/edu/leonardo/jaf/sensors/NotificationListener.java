package br.edu.leonardo.jaf.sensors;

/**
 * A listener passed to a sensor to execute some action when a new value is notified.
 *
 * @author Leonardo Vianna do Nascimento
 */
public interface NotificationListener {

    /**
     * This method is executed by a sensor to report a fatal error that caused 
     * the sensor to stop notifying new values.
     * 
     * @param exception The exception related to the reported error. 
     */
    public void onFatalError(SensorException exception);
    
    /**
     * This method is executed when a sensor notifies a new value.
     *
     * @param notification The sensor notification containing the new value and other related
     *                     information.
     */
    public void notify(SensorNotification notification);
}
