package com.khapilov.dataparser;

import java.util.ArrayDeque;

/**
 * @author Ross Khapilov
 * @version 1.0 26.05.2018
 */
public class Buffer {
    private static final ArrayDeque<String> allDataDeque = new ArrayDeque<>();
    private static final ArrayDeque<String> accelerationDeque = new ArrayDeque<>();
    private static final ArrayDeque<String> angularVelocityDeque = new ArrayDeque<>();
    private static final ArrayDeque<String> angleDeque = new ArrayDeque<>();
    private static boolean dequeIsReady = false;

    static void addAllDataDeque(String data) {
        if (dequeIsReady) {
            allDataDeque.add(data);
        }
    }

    static void addAccelerationDeque(String acceleration) {
        if (dequeIsReady)
            accelerationDeque.add(acceleration);
    }

    static void addAngularVelocityDeque(String angularVelocity) {
        if (dequeIsReady)
            angularVelocityDeque.add(angularVelocity);
    }

    static void addAngleDeque(String angle) {
        if (dequeIsReady)
            angleDeque.add(angle);
    }

    public static ArrayDeque<String> getAllDataDeque() {
        return allDataDeque;
    }

    public static ArrayDeque<String> getAccelerationDeque() {
        return accelerationDeque;
    }

    public static ArrayDeque<String> getAngularVelocityDeque() {
        return angularVelocityDeque;
    }

    public static ArrayDeque<String> getAngleDeque() {
        return angleDeque;
    }

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

    public static void cleanOutBuffer() {
        allDataDeque.clear();
        accelerationDeque.clear();
        angularVelocityDeque.clear();
        angleDeque.clear();
    }
}
