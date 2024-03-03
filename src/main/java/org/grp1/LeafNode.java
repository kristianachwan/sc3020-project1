package org.grp1;

import org.grp1.model.Record;
import java.util.ArrayList;
import java.util.List;


public class LeafNode extends Node {
    private final int maxNumOfKeys;
    private List<Integer> keys;
    private List<Record> records;
    private InternalNode parent;
    private LeafNode previous;
    private LeafNode next;

    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, int maxNumOfKeys) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
        this.maxNumOfKeys = maxNumOfKeys;
        this.keys = new ArrayList<Integer>();
        this.records = new ArrayList<Record>();
    }

    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, List<Integer> keys, List<Record> records, int maxNumOfKeys) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
        this.keys = keys;
        this.records = records;
        this.maxNumOfKeys = maxNumOfKeys;
    }

    public boolean isFull() {
        return maxNumOfKeys == this.keys.size();
    }

    public int size() {
        return this.keys.size();
    }

    public void setPrevious(LeafNode previous) {
        this.previous = previous;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setRecords(ArrayList<Integer> keys, ArrayList<Record> records) {
        this.keys = keys;
        this.records = records;
    }

    public void insertRecord(Record newRecord) {
        if (isFull()) {
            throw new Error("Inserted a record in a full node");
        }

        int newIndex = getRecordIndexLowerBound(newRecord.getNumVotes());

        keys.add(newIndex, newRecord.getNumVotes());
        records.add(newIndex, newRecord);
    }

    public int getRecordIndex(int key) {
        for (int i = 0; i < records.size(); i++) {
            if (key == keys.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public int getRecordIndexLowerBound(int key) {
        // Returns the first index which is not order before key
        for (int i = 0; i < keys.size(); i++) {
            if (key <= keys.get(i)) {
                return i;
            }
        }

        // Returns keys.size();
        return keys.size();
    }

    public Record getRecord(int key) {
        int recordIndex = getRecordIndex(key);
        if (recordIndex == -1) {
            return null;
        }

        return records.get(recordIndex);
    }

    public Record getRecordByIndex(int index) {
        return records.get(index);
    }

    public LeafNode getNext() {
        return next;
    }

    public void setNext(LeafNode next) {
        this.next = next;
    }

    public List<Record> splitRecordList(int x) {
        // [0..x) and returns [x..n)
        List<Record> left = new ArrayList<Record>(records.subList(0, x));
        List<Record> right = new ArrayList<Record>(records.subList(x, records.size()));

        records = left;

        return right;
    }

    public List<Integer> splitKeyList(int x) {
        // [0..x) and returns [x..n)
        List<Integer> left = new ArrayList<Integer>(keys.subList(0, x));
        List<Integer> right = new ArrayList<Integer>(keys.subList(x, keys.size()));

        keys = left;

        return right;
    }
}
