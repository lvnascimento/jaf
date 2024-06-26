package br.edu.leonardo.jaf.sensors;

import javax.measure.Quantity;
import javax.measure.Unit;

import tec.units.ri.quantity.Quantities;

/**
 * A value generated by a sensor that contains three quantity value representing a vector with
 * three components (x, y, and z).
 */
public class ThreeAxisSensorValue implements SensorValue {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a bew ThreeAxisSensorValue with the three given Quantity values for
     * each axis: x, y, and z.
     *
     * @param x The Quantity reference for the x axis.
     * @param y The Quantity reference for the y axis.
     * @param z The Quantity reference for the z axis.
     */
    public ThreeAxisSensorValue(Quantity x, Quantity y, Quantity z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * This constructor builds a bew ThreeAxisSensorValue where the axis will be filled with the
     * double values given in the values array that contains measures in the given unit.
     *
     * @param values The array containing, at least, three double values. The index 0 contains
     *               the value for the x axis, the index 1 contains the value for the y axis, and
     *               the index 2 contains the value for the z axis. If the array contains less than
     *               three elements, an ArrayIndexOfBoundsException is thrown. If the array contains
     *               more than three elements, the surplus values are ignored.
     * @param unit The Unit reference related to all values informed in the first array.
     */
    public ThreeAxisSensorValue(double[] values, Unit unit) {
        this(
                Quantities.getQuantity(values[0], unit),
                Quantities.getQuantity(values[1], unit),
                Quantities.getQuantity(values[2], unit)
        );
    }

    /**
     * This constructor builds a bew ThreeAxisSensorValue where the axis will be filled with the
     * float values given in the values array that contains measures in the given unit.
     *
     * @param values The array containing, at least, three float values. The index 0 contains
     *               the value for the x axis, the index 1 contains the value for the y axis, and
     *               the index 2 contains the value for the z axis. If the array contains less than
     *               three elements, an ArrayIndexOfBoundsException is thrown. If the array contains
     *               more than three elements, the surplus values are ignored.
     * @param unit The Unit reference related to all values informed in the first array.
     */
    public ThreeAxisSensorValue(float[] values, Unit unit) {
        this(
                Quantities.getQuantity(values[0], unit),
                Quantities.getQuantity(values[1], unit),
                Quantities.getQuantity(values[2], unit)
        );
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // G E T T E R S   A N D   S E T T E R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method obtains the value stored for x axis in this object.
     *
     * @return The reference of the quantity stored in this object for x axis.
     */
    public Quantity getX() {
        return x;
    }

    /**
     * This method obtains the value stored for y axis in this object.
     *
     * @return The reference of the quantity stored in this object for y axis.
     */
    public Quantity getY() {
        return y;
    }

    /**
     * This method obtains the value stored for z axis in this object.
     *
     * @return The reference of the quantity stored in this object for z axis.
     */
    public Quantity getZ() {
        return z;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // TOSTRING, EQUALS, HASHCODE
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        return "ThreeAxisSensorValue{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The quantity value stored in this object for x axis.
     */
    private final Quantity x;

    /**
     * The quantity value stored in this object for y axis.
     */
    private final Quantity y;

    /**
     * The quantity value stored in this object for z axis.
     */
    private final Quantity z;
}