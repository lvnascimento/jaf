package br.edu.leonardo.jaf;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author lvian
 * @param <T>
 */
public class Sensor<T> {
    private final Set<NotificationObserver> observers = new HashSet();

    protected void newReading(T value) {
        SensorNotification notif = new SensorNotification(this, value);
        observers.forEach((obs) -> {
            obs.notify(notif);
        });
    }

    public void addObserver(NotificationObserver obs) {
        observers.add(obs);
    }
}
