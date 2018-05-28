package com.khapilov.dataparser;

/**
 * Subclass of Data. Class for calculating the angular velocity of 3 axes (Wx, Wy, Wz) (0/s).
 * <code>data[1]</code> 0x52 identify this package.
 * <p>
 * array of 11-bytes information.
 * <p>
 * <code>array[0]</code> is always 0х55.
 * <p>
 * <code>array[1]</code> - 0x52 identify this package is the acceleration package.
 * <p>
 * <code>array[2]</code> - X-axis angular velocity low byte.
 * <p>
 * <code>array[3]</code> - X-axis angular velocity high byte.
 * <p>
 * <code>array[4]</code> - Y-axis angular velocity  low byte.
 * <p>
 * <code>array[5]</code> - Y-axis angular velocity high byte.
 * <p>
 * <code>array[6]</code> - Z-axis angular velocity low byte.
 * <p>
 * <code>array[7]</code> - Z-axis angular velocity high byte.
 * <p>
 * <code>array[8]</code> - Temperature low byte.
 * <p>
 * <code>array[9]</code> - Temperature high byte.
 * <p>
 * <code>array[10]</code> - Checksum.
 *
 * @author Ross Khapilov
 * @version 1.0 20.05.2018
 */
public final class AngularVelocityData extends Data {
    private float wx;
    private float wy;
    private float wz;

    /**
     * Set <code>data</code> arrays. Length of the array must be equal 11, <>data[1]</> equal 52 and NotNull.
     *
     * @param data array of 11-bytes information, where <code>array[0]</code> is always 0х55.
     * @throws NullPointerException     if <code>data</code> has a null value.
     * @throws IllegalArgumentException if <code>data[1]</code> don`t equal 52.
     */
    @Override
    public void setData(String[] data) {
        if (data[1] == null) throw new NullPointerException("data[1] has a null value");
        if (!data[1].equals("52")) throw new IllegalArgumentException("data[1] must be equal 52: not " + data[1]);
        super.setData(data);
        Buffer.addAngularVelocityDeque(wx + " " + wy + " " + wz);
    }

    /**
     * @return x-axis angular velocity.
     */
    public float getWx() {
        return wx;
    }

    /**
     * @return y-axis angular velocity.
     */
    public float getWy() {
        return wy;
    }

    /**
     * @return z-axis angular velocity.
     */
    public float getWz() {
        return wz;
    }

    /**
     * Calculate x-axis angular velocity. Uses <code>data[2]</code> low byte and <code>data[3]</code> high byte.
     */
    public void calculateWx() {
        wx = calculateMainData(getData()[2], getData()[3], 2000);
    }

    /**
     * Calculate x-axis angular velocity. Uses <code>data[4]</code> low byte and <code>data[5]</code> high byte.
     */
    public void calculateWy() {
        wy = calculateMainData(getData()[4], getData()[5], 2000);
    }

    /**
     * Calculate x-axis angular velocity. Uses <code>data[6]</code> low byte and <code>data[7]</code> high byte.
     */
    public void calculateWz() {
        wz = calculateMainData(getData()[6], getData()[7], 2000);
    }

    /**
     * Bridge method for make usage a bit simpler.
     */
    public void calculateAllAngularVelocities() {
        calculateWx();
        calculateWy();
        calculateWz();
    }
}
