package com.khapilov.dataparser;

/**
 * Subclass of Data. Class for calculating the acceleration of 3 axes (Ax, Ay, Az) (m/s^2).
 * <code>data[1]</code> 0x51 identify this package.
 * <p>
 * array of 11-bytes information.
 * <p>
 * <code>array[0]</code> is always 0х55.
 * <p>
 * <code>array[1]</code> - 0x51 identify this package is the acceleration package.
 * <p>
 * <code>array[2]</code> - X-axis acceleration low byte.
 * <p>
 * <code>array[3]</code> - X-axis acceleration high byte.
 * <p>
 * <code>array[4]</code> - Y-axis acceleration low byte.
 * <p>
 * <code>array[5]</code> - Y-axis acceleration high byte.
 * <p>
 * <code>array[6]</code> - Z-axis acceleration low byte.
 * <p>
 * <code>array[7]</code> - Z-axis acceleration high byte.
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
public final class AccelerationData extends Data {
    private float ax;
    private float ay;
    private float az;

    /**
     * Set <code>data</code> arrays. Length of the array must be equal 11, <>data[1]</> equal 51 and NotNull.
     *
     * @param data array of 11-bytes information, where <code>array[0]</code> is always 0х55.
     * @throws NullPointerException     if <code>data</code> has a null value.
     * @throws IllegalArgumentException if <code>data[1]</code> don`t equal 51.
     */
    @Override
    public void setData(String[] data) {
        if (data[1] == null) throw new NullPointerException("data[1] has a null value");
        if (!data[1].equals("51")) throw new IllegalArgumentException("data[1] must be equal 51: not " + data[1]);
        super.setData(data);
        Buffer.addAccelerationDeque(ax + " " + ay + " " + az);
    }

    /**
     * @return x-axis acceleration.
     */
    public float getAx() {
        return ax;
    }

    /**
     * @return y-axis acceleration.
     */
    public float getAy() {
        return ay;
    }

    /**
     * @return z-axis acceleration.
     */
    public float getAz() {
        return az;
    }

    /**
     * Calculate x-axis acceleration. Use <code>data[2]</code> low byte and <code>data[3]</code> high byte.
     */
    public void calculateAx() {
        ax = calculateMainData(getData()[2], getData()[3], 156.8);
    }

    /**
     * Calculate y-axis acceleration. Use <code>data[4]</code> low byte and <code>data[5]</code> high byte.
     */
    public void calculateAy() {
        ay = calculateMainData(getData()[4], getData()[5], 156.8);
    }

    /**
     * Calculate y-axis acceleration. Use <code>data[6]</code> low byte and <code>data[7]</code> high byte.
     */
    public void calculateAz() {
        az = calculateMainData(getData()[6], getData()[7], 156.8);
    }

    /**
     * Bridge method for make usage a bit simpler.
     */
    public void calculateAllAccelerations() {
        calculateAx();
        calculateAy();
        calculateAz();
    }
}
