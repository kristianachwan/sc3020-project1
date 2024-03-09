package org.grp1.util;

import java.util.concurrent.TimeUnit;

public class Context {

    private static long startTime = 0;
    private static long endTime = 0;
    private static int count = 0;


    public static void increment() {
        count++;
    }

    private static void add(int addend) {
        count += addend;
    }

    public static int getCount() {
        return count;
    }

    public static void startTimer() {
        startTime = System.nanoTime();
    }

    public static void endTimer() {
        endTime = System.nanoTime();
    }

    public static long getElapsedTime(TimeUnit unit) {
        return unit.convert((endTime - startTime), TimeUnit.NANOSECONDS);
    }

    public static void reset() {
        startTime = 0;
        endTime = 0;
        count = 0;
    }

}
