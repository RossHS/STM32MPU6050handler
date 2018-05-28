package com.khapilov.dataparser;

/**
 * Subclass of Data. Class for calculating angles for 3 axes (Roll, Pitch, Yaw) (degrees).
 * <code>data[1]</code> 0x53 identify this package.
 * <p>
 * array of 11-bytes information.
 * <p>
 * <code>array[0]</code> is always 0х55.
 * <p>
 * <code>array[1]</code> - 0x53 identify this package is the acceleration package.
 * <p>
 * <code>array[2]</code> - X-axis angle low byte.
 * <p>
 * <code>array[3]</code> - X-axis angle high byte.
 * <p>
 * <code>array[4]</code> - Y-axis angle low byte.
 * <p>
 * <code>array[5]</code> - Y-axis angle high byte.
 * <p>
 * <code>array[6]</code> - Z-axis angle low byte.
 * <p>
 * <code>array[7]</code> - Z-axis angle high byte.
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
public final class AngleData extends Data {
    private float roll;
    private float pitch;
    private float yaw;

    /**
     * Set <code>data</code> arrays. Length of the array must be equal 11, <>data[1]</> equal 53 and NotNull.
     *
     * @param data array of 11-bytes information, where <code>array[0]</code> is always 0х55.
     * @throws NullPointerException     if <code>data</code> has a null value.
     * @throws IllegalArgumentException if <code>data[1]</code> don`t equal 53.
     */
    @Override
    public void setData(String[] data) {
        if (data[1] == null) throw new NullPointerException("data[1] has a null value");
        if (!data[1].equals("53")) throw new IllegalArgumentException("data[1] must be equal 53: not " + data[1]);
        super.setData(data);
        Buffer.addAngleDeque(roll + " " + pitch + " " + yaw);
    }

    /**
     * @return Roll.
     */
    public float getRoll() {
        return roll;
    }

    /**
     * @return Pitch.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * @return Yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Calculate Roll. Uses <code>data[2]</code> low byte and <code>data[3]</code> high byte.
     */
    public void calculateRoll() {
        roll = calculateMainData(getData()[2], getData()[3], 180);
    }

    /**
     * Calculate Pitch. Uses <code>data[4]</code> low byte and <code>data[5]</code> high byte.
     */
    public void calculatePitch() {
        pitch = calculateMainData(getData()[4], getData()[5], 180);
    }

    /**
     * Calculate Yaw. Uses <code>data[6]</code> low byte and <code>data[7]</code> high byte.
     */
    public void calculateYaw() {
        yaw = calculateMainData(getData()[6], getData()[7], 180);
    }

    /**
     * Bridge method for make usage a bit simpler.
     */
    public void calculateAllAngles() {
        calculateRoll();
        calculatePitch();
        calculateYaw();
    }
}