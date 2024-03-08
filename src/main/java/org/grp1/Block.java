package org.grp1;

import java.util.ArrayList;

public class Block {
    private final ArrayList<Record> records;

    public Block(ArrayList<Record> records) {
        this.records = records;
    }

    public int getNumberOfRecords() {
        return this.records.size();
    }

    public Record getRecord(int index) {
        return records.get(index);
    }

    public ArrayList<Record> getRecords() {
        return new ArrayList<>(this.records);
    }
}
