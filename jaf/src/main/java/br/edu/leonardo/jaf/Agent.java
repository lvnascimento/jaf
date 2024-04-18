package br.edu.leonardo.jaf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author lvian
 */
public class Agent {
    private final Map<Sensor,SensorEntry> sensors = new HashMap();

    public void addSensor(Sensor sensor) {
        NotificationObserver obs = new NotificationObserver(sensor, this);
        sensors.put(sensor, new SensorEntry(obs));
        sensor.addObserver(obs);
    }
    
    public void addProcess(Process p, Sensor s) {
        SensorEntry entry = sensors.get(s);
        if(entry == null)
            throw new IllegalArgumentException("The informed sensor was not added to the agent.");
        else
            entry.addRelatedProcess(p);
    }
    
    public void notifyNewSensorReading(SensorNotification notification) {
        Set<Process> filteredProcesses = searchProcesses(notification);
        filteredProcesses.forEach((p) -> {
            p.execute(notification);
        });
    }

    protected Set<Process> searchProcesses(SensorNotification notification) {
        SensorEntry entry = sensors.get(notification.getSensor());
        return entry == null ? new HashSet() : entry.getRelatedProcesses();
    }

    private static class SensorEntry {
        private final NotificationObserver observer;
        private final Set<Process> relatedProcesses = new HashSet();

        public SensorEntry(NotificationObserver observer) {
            this.observer = observer;
        }

        public NotificationObserver getObserver() {
            return observer;
        }
        
        public void addRelatedProcess(Process p) {
            relatedProcesses.add(p);
        }

        public Set<Process> getRelatedProcesses() {
            return relatedProcesses;
        }
    }
}
