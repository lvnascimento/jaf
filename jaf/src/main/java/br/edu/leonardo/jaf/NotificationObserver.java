package br.edu.leonardo.jaf;

/**
 *
 * @author lvian
 */
public class NotificationObserver {
    private final Sensor sensor;
    private final Agent agent;

    public NotificationObserver(Sensor sensor, Agent agent) {
        this.sensor = sensor;
        this.agent = agent;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Agent getAgent() {
        return agent;
    }

    public void notify(SensorNotification notification) {
        agent.notifyNewSensorReading(notification);
    }
}
