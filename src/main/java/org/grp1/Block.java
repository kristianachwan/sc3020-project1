package org.grp1;

import java.util.List;

public class Block<T> {

    private final List<T> records;

    public Block(List<T> records) {
        this.records = records;
    }

    public int getNumberOfRecords() {
        return this.records.size();
    }
}
