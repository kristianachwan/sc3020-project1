package org.grp1.index;

import java.util.ArrayList;

import org.grp1.exception.LeafFullException;
import org.grp1.model.Record;

public class LeafNode extends Node {
    private final int maxNumOfKeys;
    private ArrayList<Integer> keys;
    private ArrayList<Record> records;
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

    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, ArrayList<Integer> keys,
                    ArrayList<Record> records, int maxNumOfKeys) {
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


    public void insert(NodeChild newChild) {
        if (!(newChild instanceof Record newRecord)) {
            throw new Error("Inserted a non-record child");
        }
        if (isFull()) {
            throw new LeafFullException("Inserted a record in a full node");
        }

        int newIndex = getRecordIndexLowerBound(newRecord.getNumVotes());

        keys.add(newIndex, newRecord.getNumVotes());
        records.add(newIndex, newRecord);
    }

    public int getKey() {
        return this.keys.get(0);
    }

    public int getKeyByIndex(int index) {
        return this.keys.get(index);
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

    public void delete(int index) {
        if (index < 0 || index >= records.size()) {
            throw (new Error("Index out of bounds"));
        }

        keys.remove(index);
        records.remove(index);
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

    public NodeChild getChildAsNodeChild(int index) {
        return records.get(index);
    }

    public LeafNode getNext() {
        return next;
    }

    public void setNext(LeafNode next) {
        this.next = next;
    }

    public ArrayList<Record> splitRecordList(int x) {
        // [0..x) and returns [x..n)
        ArrayList<Record> left = new ArrayList<Record>(records.subList(0, x));
        ArrayList<Record> right = new ArrayList<Record>(records.subList(x, records.size()));

        records = left;

        return right;
    }

    public ArrayList<Integer> splitKeyList(int x) {
        // [0..x) and returns [x..n)
        ArrayList<Integer> left = new ArrayList<Integer>(keys.subList(0, x));
        ArrayList<Integer> right = new ArrayList<Integer>(keys.subList(x, keys.size()));

        keys = left;

        return right;
    }
}
