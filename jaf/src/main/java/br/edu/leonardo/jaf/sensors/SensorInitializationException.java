package br.edu.leonardo.jaf.sensors;

/**
 * An exception thrown when an error occurs in a sensor initialization
 */
public class SensorInitializationException extends Exception {
    public SensorInitializationException() {
    }

    public SensorInitializationException(String message) {
        super(message);
    }

    public SensorInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SensorInitializationException(Throwable cause) {
        super(cause);
    }

    public SensorInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}