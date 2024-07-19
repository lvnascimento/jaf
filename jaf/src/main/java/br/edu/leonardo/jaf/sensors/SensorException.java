package br.edu.leonardo.jaf.sensors;

/**
 * An exception thrown when an error occurs in a sensor operation.
 */
public class SensorException extends Exception {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new SensorException related to the given sensor.
     * 
     * @param sensor The related sensor. 
     */
    public SensorException(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * This constructor builds a new SensorException with the given sensor and detail message. 
     * The cause is not initialized, and may subsequently be initialized by a call to 
     * <code>Throwable.initCause(java.lang.Throwable)</code>.
     * 
     * @param sensor The related sensor.
     * @param message The detail message. The detail message is saved for later retrieval by the 
     * <code>Throwable.getMessage()</code> method.
     */
    public SensorException(Sensor sensor, String message) {
        super(message);
        this.sensor = sensor;
    }

    /**
     * This constructor builds a new exception with the specified sensor, detail message, and cause. 
     * Note that the detail message associated with cause is not automatically incorporated in this 
     * exception's detail message.
     * 
     * @param sensor The related sensor.
     * @param message The detail message. The detail message is saved for later retrieval by the 
     * <code>Throwable.getMessage()</code> method.
     * @param cause The cause (which is saved for later retrieval by the <code>Throwable.getCause()</code> method). 
     * A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public SensorException(Sensor sensor, String message, Throwable cause) {
        super(message, cause);
        this.sensor = sensor;
    }

    /**
     * This method buids a new exception with the specified sensor and cause, and a detail message 
     * of <code>(cause==null ? null : cause.toString())</code> (which typically contains the class 
     * and detail message of cause).
     * 
     * @param sensor The related sensor.
     * @param cause The cause (which is saved for later retrieval by the <code>Throwable.getCause()</code> method). 
     * A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public SensorException(Sensor sensor, Throwable cause) {
        super(cause);
        this.sensor = sensor;
    }

    /**
     * This constructor builds a new exception with the specified detail message, cause, suppression enabled 
     * or disabled, and writable stack trace enabled or disabled.
     * 
     * @param sensor The related sensor.
     * @param message The detail message. The detail message is saved for later retrieval by the 
     * <code>Throwable.getMessage()</code> method.
     * @param cause The cause (which is saved for later retrieval by the <code>Throwable.getCause()</code> method). 
     * A null value is permitted, and indicates that the cause is nonexistent or unknown.
     * @param enableSuppression Whether or not suppression is enabled or disabled.
     * @param writableStackTrace Whether or not the stack trace should be writable.
     */
    public SensorException(Sensor sensor, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.sensor = sensor;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the sensor related to this exception.
     * 
     * @return The related sensor. 
     */
    public Sensor getSensor() {
        return sensor;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The sensor related to this exception.
     */
    private final Sensor sensor;
}