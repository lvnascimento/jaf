package br.edu.leonardo.jaf;

/**
 *
 * @author lvian
 * @param <T>
 */
public class SensorNotification<T> {
    private final Sensor sensor;
    private final T value;

    public SensorNotification(Sensor sensor, T value) {
        this.sensor = sensor;
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public T getValue() {
        return value;
    }
}
