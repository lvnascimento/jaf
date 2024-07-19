package br.edu.leonardo.jaf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.edu.leonardo.jaf.sensors.NotificationListener;
import br.edu.leonardo.jaf.sensors.Sensor;
import br.edu.leonardo.jaf.sensors.SensorException;
import br.edu.leonardo.jaf.sensors.SensorNotification;

/**
 * An agent in the platform.
 *
 * @author Leonardo Vianna do Nascimento
 */
public class Agent {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new Agent that uses a pool of unlimited cached threads to execute
     * its processes.
     */
    public Agent(){
        this(false);
    }

    /**
     * This constructor builds a new Agent that uses a pool of threads to execute its processes.
     * The pool can use a single thread (the processes are executed one after each other, in a queue)
     * or in a unlimited cache pool (if there is an available thread in the pool, a new process use
     * it; otherwise a new thread is created - each thread is kept in the pool for 60 seconds).
     *
     * @param singleThread True if the agent must use a single thread pool; false otherwise.
     */
    public Agent(boolean singleThread) {
        if(singleThread)
            this.threadExecService = Executors.newSingleThreadExecutor();
        else
            this.threadExecService = Executors.newCachedThreadPool();
    }

    /**
     * This constructor builds a new Agent that uses a fixed pool of threads to execute its
     * processes. The pool's size is specified at the given argument. A predefined number of threads
     * is created and receives processes on demand. If a process is created but there are no
     * available threads in the pool, the process must wait in a queue for execution.
     *
     * @param numOfThreads The size of the pool (the value must be positive).
     * @throws IllegalArgumentException If a negative value is informed in numOfThreads argument.
     */
    public Agent(int numOfThreads) {
        if(numOfThreads < 1)
            throw new IllegalArgumentException("The number of threads in an agent cannot be negative or zero.");
        else
            this.threadExecService = Executors.newFixedThreadPool(numOfThreads);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method adds a new sensor to this agent. A sensor is a component that allows an agent to
     * obtain information from the environment. A sensor is an optional component of an agent. An
     * individual agent can have one or more sensors. A process should be added to the agent and
     * related to the sensor to do something when a new sensor value is notified.
     *
     * @param sensor The new sensor reference. If the sensor has already been added to the agent,
     *               nothing happens.
     */
    public void addSensor(Sensor sensor) {
        if(!sensors.containsKey(sensor)) {
            NotificationListener obs = new NotificationListener() {
                @Override
                public void notify(SensorNotification notification) {
                    notifyNewSensorReading(notification);
                }

                @Override
                public void onFatalError(SensorException exception) {
                    sensors.remove(sensor);
                    onRemoveSensor(exception);
                }
            };
            sensors.put(sensor, new SensorEntry(sensor));
            sensor.addListener(obs);
        }
    }

    /**
     * This method adds a new process to this agent that will be executed when the sensor s notifies
     * new values. If the sensor s has not been added to the agent, this method adds s as a new
     * sensor.
     *
     * @param p The reference to the new process.
     * @param s The Sensor reference related to the process.
     */
    public void addProcess(Process p, Sensor s) {
        SensorEntry entry = sensors.get(s);
        if(entry == null) { // If the agent does not contain the sensor s, add it to the agent.
            addSensor(s);
            entry = sensors.get(s);
        }
        entry.addRelatedProcess(p);
    }

    /**
     * This method initializes an Agent. The method "setup" is executed and,
     * if the initSensors argument is true, all sensors in the agent
     * are initialized (the method "init" of each sensor is invoked).
     *
     * @param initSensors true if all sensors in the agent should be initialized;
     *                    false otherwise.
     * @throws AgentException If an error occurred during the agent initialization.
     */
    public void init(boolean initSensors) throws AgentException {
        try {
            setup();
            if(initSensors)
                initializeSensors();
        } catch (SensorException ex) {
            throw new AgentException(
                    "It is not possible to initialize the agent because an error occurred during sensor initialization.",
                    ex
            );
        }
    }
    
    /**
     * This method initializes an Agent. The method "setup" is executed and all sensors in the agent
     * are initialized (the method "init" of each sensor is invoked).
     *
     * @throws AgentException If an error occurred during the agent initialization.
     */
    public void init() throws AgentException {
        this.init(true);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R O T E C T E D   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method should be overridden by subclasses to implement specific initialization code. This
     * method is invoked once, when the agent is initialized.
     * 
     * @throws AgentException If an error occurred during initialization process.
     */
    protected void setup() throws AgentException {
    }
    
    /**
     * This method should be overridden by subclasses to implement specific actions when a sensor stops
     * working. 
     * 
     * @param exception The error that caused the sensor to stop.
     */
    protected void onRemoveSensor(SensorException exception) { 
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This internal method initialize all sensors added to this agent.
     *
     * @throws SensorException If an error occurred during a sensor initialization.
     */
    private void initializeSensors() throws SensorException {
        for(Sensor s : sensors.keySet()) {
            s.init();
        }
    }

    /**
     * The agent uses this internal method to process a new notification received from a sensor.
     * This method execute all processes related to the sensor that sends the notification.
     *
     * @param notification The received notification.
     */
    private void notifyNewSensorReading(SensorNotification notification) {
        // Search processes related to the sensor that sent the notification.
        Set<Process> filteredProcesses = searchProcesses(notification);

        // Execute each process
        for (Process p : filteredProcesses) {
            executeProcess(p, notification);
        }
    }

    /**
     * This method searches for processes that are related to the sensor that sent the given
     * notification.
     *
     * @param notification The notification.
     * @return A set of processes. A empty set will be returned if there are no processes related
     * to the given sensor.
     */
    private Set<Process> searchProcesses(SensorNotification notification) {
        SensorEntry entry = sensors.get(notification.getSensor());
        return entry == null ? new HashSet<>() : entry.getRelatedProcesses();
    }

    /**
     * This method executes the given process p that will process the given sensor notification. The
     * process will be executed in a separated thread. The thread will finish when the process
     * finish or when it will be interrupted.
     *
     * @param p The desired process.
     * @param notification The received sensor notification.
     */
    private void executeProcess(Process p, SensorNotification notification) {
        // Create a Runnable to execute the process in a separated thread.
        Runnable r = new Runnable() {
            @Override
            public void run() {
                p.execute(notification);

                // Remove the process Future from the processes map when the runnable finishes.
                processesThreads.remove(p);
            }
        };

        // Add the process runnable to the thread pool for execution.
        Future<?> f = threadExecService.submit(r);

        // Put the returned Future object in the processes map. This object allows future
        // manipulations of the process thread.
        processesThreads.put(p, f);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The map of sensors in the agent. The map allows that the agent can access sensor data from
     * a sensor reference that is usually informed in a notification.
     */
    private final Map<Sensor,SensorEntry> sensors = new HashMap<>();

    /**
     * The executor service that manage the thread pool used to execute the agent processes.
     */
    private final ExecutorService threadExecService;

    /**
     * The map of future objects used to interact with the threads used to execute each process in
     * the agent.
     */
    private Map<Process, Future<?>> processesThreads = new HashMap<>();

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   I N T E R N A L   C L A S S E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * An entry in the agent's sensor map that stores internal information about sensors.
     */
    private static class SensorEntry {

        //////////////////////////////////////////////////////////////////////////////////////////
        // P U B L I C   C O N S T R U C T O R S
        //////////////////////////////////////////////////////////////////////////////////////////

        /**
         * This constructor creates a new SensorEntry related to the given sensor.
         *
         * @param sensor The desired sensor.
         */
        public SensorEntry(Sensor sensor) {
            this.sensor = sensor;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // P U B L I C   M E T H O D S
        //////////////////////////////////////////////////////////////////////////////////////////

        /**
         * This method adds a process to this entry. When a process is added to an entry, it will
         * be executed by the agent when the sensor related to that entry notifies new values.
         *
         * @param p The desired process.
         */
        public void addRelatedProcess(Process p) {
            relatedProcesses.add(p);
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // G E T T E R S   A N D   S E T T E R S
        //////////////////////////////////////////////////////////////////////////////////////////

        /**
         * This method returns the sensor related to this entry.
         *
         * @return The Sensor reference.
         */
        public Sensor getSensor() {
            return sensor;
        }

        /**
         * This method returns the set of processes in this entry. These processes should be executed
         * when the sensor related to this entry notifies a new value.
         *
         * @return The set of processes.
         */
        public Set<Process> getRelatedProcesses() {
            return relatedProcesses;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // P R I V A T E   A T T R I B U T E S
        //////////////////////////////////////////////////////////////////////////////////////////

        /**
         * The sensor related to this entry.
         */
        private final Sensor sensor;

        /**
         * The set of processes in this entry and that be executed when the sensor notifies a new
         * value.
         */
        private final Set<Process> relatedProcesses = new HashSet<>();
    }
}