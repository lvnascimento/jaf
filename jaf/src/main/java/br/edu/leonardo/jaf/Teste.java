package br.edu.leonardo.jaf;

import br.edu.leonardo.jaf.sensors.Sensor;
import br.edu.leonardo.jaf.sensors.SensorNotification;
import br.edu.leonardo.jaf.sensors.SingleSensorValue;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import tec.units.ri.AbstractUnit;

/**
 *
 * @author lvian
 */
public class Teste {
    public static void main(String[] args) throws InterruptedException {
        Agent agent1 = new Agent();
        SensorA sensor1 = new SensorA(500);
        SensorA sensor2 = new SensorA(800);
        agent1.addSensor(sensor1);
        agent1.addSensor(sensor2);
        agent1.addProcess((SensorNotification notification) -> {
            System.out.println("SENSOR 1 NOTIFICATION: " + notification.getValue());
        }, sensor1);
        agent1.addProcess((SensorNotification notification) -> {
            System.out.println("SENSOR 2 NOTIFICATION----->>>> " + notification.getValue());
        }, sensor2);
        sensor1.init();
        sensor2.init();
        Thread.sleep(10000);
        sensor1.stop();
        sensor2.stop();
    }
    
    private static class SensorA extends Sensor {
        Timer timer;
        long delay;

        public SensorA(long delay) {
            this.delay = delay;
        }
        
        @Override
        public void init() {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    newReading(new SingleSensorValue((int)(Math.random()*100), AbstractUnit.ONE));
                }
            }, new Date(), delay);
        }
        
        public void stop() {
            timer.cancel();
        }
    }
}
