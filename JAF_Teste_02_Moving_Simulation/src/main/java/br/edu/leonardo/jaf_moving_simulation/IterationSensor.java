package br.edu.leonardo.jaf_moving_simulation;

import br.edu.leonardo.jaf.sensors.BooleanSensorValue;
import br.edu.leonardo.jaf.sensors.Sensor;
import br.edu.leonardo.jaf.sensors.SensorInitializationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A sensor that periodically sends a notification.
 * 
 * @author Leonardo Vianna do Nascimento
 */
public class IterationSensor extends Sensor {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new IterationSensor that is owned by the given agent, sends 
     * notifications after each amount of time specified in period, and is inside the given
     * environment.
     * 
     * @param agent The agent that owns this sensor.
     * @param period The amount of time between each notification.
     * @param environment The environment where the sensor is in.
     */
    public IterationSensor(SimulatedRobot agent, Duration period, Environment environment) {
        this.period = period;
        this.environment = environment;
        this.agent = agent;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void init() throws SensorInitializationException {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(environment.isWall(agent.getNextX(), agent.getNextY()))
                    newReading(BooleanSensorValue.TRUE);
                else
                    newReading(BooleanSensorValue.FALSE);
            }
        }, Date.from(Instant.now().plusMillis(500)), period.toMillis());
    }
    
    /**
     * This method stops the sensor notifications.
     */
    public void stop() {
        timer.cancel();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The internal timer used to trigger notifications.
     */
    private final Timer timer = new Timer();
    
    /**
     * The amount of time between each notification.
     */
    private final Duration period;
    
    /**
     * The environment where the sensor is in.
     */
    private final Environment environment;
    
    /**
     * The agent that owns this sensor.
     */
    private final SimulatedRobot agent;
}