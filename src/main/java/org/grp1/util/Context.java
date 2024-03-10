package org.grp1.util;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Context {
    /**
     * Represents the set to count the number of distinct things
     */
    private static final HashSet<Integer> set = new HashSet<>();
    /**
     * Represents the start time of tracking
     */
    private static long startTime = 0;
    /**
     * Represents the end time of tracking
     */
    private static long endTime = 0;
    /**
     * Represents the count of tracking
     */
    private static int count = 0;

    /**
     * The function to add the integer into the set
     *
     * @param integer The integer key to be added
     */

    public static void addSetInteger(int integer) {
        set.add(integer);
    }

    /**
     * The function to get the set count
     *
     * @return The length of the set
     */
    public static int getSetCount() {
        return set.size();
    }

    /**
     * The function to increment the count
     */
    public static void increment() {
        count++;
    }

    /**
     * The function to add the count by some addend
     *
     * @param addend The value that you want to add
     */
    private static void add(int addend) {
        count += addend;
    }

    /**
     * The function to get the count
     *
     * @return The count that we track
     */
    public static int getCount() {
        return count;
    }

    /**
     * The function to start the timer
     */
    public static void startTimer() {
        startTime = System.nanoTime();
    }

    /**
     * The function to end the timer
     */
    public static void endTimer() {
        endTime = System.nanoTime();
    }

    /**
     * The function to get the elapsed time between the start and end time
     *
     * @param unit The of the time
     * @return The time converted into the corresponding unit
     */
    public static long getElapsedTime(TimeUnit unit) {
        return unit.convert((endTime - startTime), TimeUnit.NANOSECONDS);
    }

    /**
     * The function to reset all the context fields
     */
    public static void reset() {
        startTime = 0;
        endTime = 0;
        count = 0;
        set.clear();
    }

}
