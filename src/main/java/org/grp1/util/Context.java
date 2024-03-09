package org.grp1.util;

import java.util.concurrent.TimeUnit;

public class Context {

    private long startTime;
    private long endTime;
    private int count;

    public Context() {
        this.count = 0;
    }

    public void increment() {
        this.count++;
    }

    private void add(int addend) {
        count += addend;
    }

    public int getCount() {
        return count;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void endTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime(TimeUnit unit) {
        return unit.convert((endTime - startTime), TimeUnit.NANOSECONDS);
    }

}
