package com.khapilov.dataparser;

/**
 * The main data class. Data we will receive from mpu6050 in arrays of Strings which containing 11 bytes of information.
 *
 * @author Ross Khapilov
 * @version 1.0 20.05.2018
 */
public class Data {
    /**
     * array of 11-bytes information.
     * <p>
     * <code>array[0]</code> is always 0х55.
     * <p>
     * <code>array[8]</code> - Temperature low byte.
     * <p>
     * <code>array[9]</code> - Temperature high byte.
     * <p>
     * <code>array[10]</code> - Checksum.
     * <p>
     * Other elements are different for each package.
     */
    private String[] data;

    /**
     * Return <code>data</code>.
     *
     * @return array of 11-bytes information, where <code>array[0]</code> is always 0х55.
     */
    public String[] getData() {
        return data;
    }

    /**
     * Set <code>data</code> arrays. Length of the array must be equal 11.
     *
     * @param data array of 11-bytes information, where <code>array[0]</code> is always 0х55.
     * @throws IllegalArgumentException if <code>data</code> length isn`t 11.
     */
    public void setData(String[] data) {
        if (data.length != 11)
            throw new IllegalArgumentException("Array must contain 11 elements: but get " + data.length + " elements");
        this.data = data;
        Buffer.addAllDataDeque(this.toString());
    }

    /**
     * For calculate temperature in Celsius degrees. The data index the same for all packages of information.
     *
     * @param lowByte  Temperature low byte always data[8].
     * @param highByte Temperature high byte always data[9].
     * @return temperature in Celsius degrees.
     */
    public final float calculateTemperature(String lowByte, String highByte) {
        char low = (char) Integer.parseInt(lowByte, 16);
        char high = (char) Integer.parseInt(highByte, 16);
        short temp = (short) ((high << 8) | low);
        return (float) ((float) temp / 340 + 36.53);
    }

    /**
     * Finds the main values.
     *
     * @param lowByte  low byte
     * @param highByte high byte
     * @param val      multiplier
     * @return Acceleration/Angular Velocity/Angle
     */
    protected final float calculateMainData(String lowByte, String highByte, double val) {
        char low = (char) Integer.parseInt(lowByte, 16);
        char high = (char) Integer.parseInt(highByte, 16);
        short temp = (short) ((high << 8) | low);
        return (float) ((float) temp / 32_768 * val);
    }

    /**
     * Simpler way to get the temperature.
     *
     * @return temperature in Celsius degrees.
     */
    public final float calculateTemperature() {
        return calculateTemperature(data[8], data[9]);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String s : data) {
            str.append(s).append(" ");
        }
        return str.toString();
    }
}