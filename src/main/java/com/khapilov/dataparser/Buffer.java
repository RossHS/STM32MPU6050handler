package com.khapilov.dataparser;

import java.util.ArrayDeque;

/**
 * Buffer class to collect data for a set time.
 *
 * @author Ross Khapilov
 * @version 1.0 26.05.2018
 */
public class Buffer {
    private static final ArrayDeque<String> allDataDeque = new ArrayDeque<>();
    private static final ArrayDeque<String> accelerationDeque = new ArrayDeque<>();
    private static final ArrayDeque<String> angularVelocityDeque = new ArrayDeque<>();
    private static final ArrayDeque<String> angleDeque = new ArrayDeque<>();
    private static boolean dequeIsReady = false;

    /**
     * Add pure data to the collection if the ArrayDeque is open.
     *
     * @param data pure Data from MPU6050.
     */
    static void addAllDataDeque(String data) {
        if (dequeIsReady) {
            allDataDeque.add(data);
        }
    }

    /**
     * Add acceleration data to the collection if the ArrayDeque is open.
     *
     * @param acceleration data from MPU6050.
     */
    static void addAccelerationDeque(String acceleration) {
        if (dequeIsReady)
            accelerationDeque.add(acceleration);
    }

    /**
     * Add angular velocity data to the collection if the ArrayDeque is open.
     *
     * @param angularVelocity data from MPU6050.
     */
    static void addAngularVelocityDeque(String angularVelocity) {
        if (dequeIsReady)
            angularVelocityDeque.add(angularVelocity);
    }

    /**
     * Add angle data to the queue if the ArrayDeque is open.
     *
     * @param angle data from MPU6050.
     */
    static void addAngleDeque(String angle) {
        if (dequeIsReady)
            angleDeque.add(angle);
    }

    /**
     * @return reference to pure data collection.
     */
    public static ArrayDeque<String> getAllDataDeque() {
        return allDataDeque;
    }

    /**
     * @return reference to acceleration data collection.
     */
    public static ArrayDeque<String> getAccelerationDeque() {
        return accelerationDeque;
    }

    /**
     * @return reference angular velocity data collection.
     */
    public static ArrayDeque<String> getAngularVelocityDeque() {
        return angularVelocityDeque;
    }

    /**
     * @return reference to angle data collection.
     */
    public static ArrayDeque<String> getAngleDeque() {
        return angleDeque;
    }

    /**
     * Set the time at which to open the collection for writing.
     *
     * @param timeInMillisecond time until it is open.
     */
    public static void setUpTamer(int timeInMillisecond) {
        Thread thread = new Thread(() -> {
            try {
                dequeIsReady = true;
                Thread.sleep(timeInMillisecond);
                dequeIsReady = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    /**
     * Clear out queues.
     */
    public static void clearOutBuffer() {
        allDataDeque.clear();
        accelerationDeque.clear();
        angularVelocityDeque.clear();
        angleDeque.clear();
    }
}
