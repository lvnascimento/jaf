package br.edu.leonardo.jaf_teste_aut_linear;

import br.edu.leonardo.jaf.sensors.Sensor;
import br.edu.leonardo.jaf.sensors.SensorInitializationException;
import br.edu.leonardo.jaf.sensors.SensorValue;
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
     * This constructor builds a new IterationSensor that sends notifications after each amount of
     * time specified in period.
     * 
     * @param period The amount of time between each notification.
     */
    public IterationSensor(Duration period) {
        this.period = period;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void init() throws SensorInitializationException {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                newReading(new SensorValue(){});
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
}