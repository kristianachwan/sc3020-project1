package org.grp1;

public class Context {
    private int count;

    public Context() {
        this.count = 0;
    }

    private void increment() {
        this.count++;
    }

    private void add(int addend) {
        count += addend;
    }

}
