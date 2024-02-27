package org.grp1;

public class LeafNode extends Node {
    private int[] keys;
    private Record[] records;
    private InternalNode parent;
    private LeafNode previous;
    private LeafNode next;

    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
    }

    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, int[] keys, Record[] records) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
        this.keys = keys;
        this.records = records;
    }

    public void setPrevious(LeafNode previous) {
        this.previous = previous;
    }

    public void setNext(LeafNode next) {
        this.next = next;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setRecords(int[] keys, Record[] records) {
        this.keys = keys;
        this.records = records;
    }

    public int getRecordIndex(int key) {
        for (int i = 0; i < records.length; i++) {
            if (key == keys[i]) {
                return i;
            }
        }
        return -1;
    }

    public Record getRecord(int key) {
        int recordIndex = getRecordIndex(key);
        if (recordIndex == -1) {
            return null;
        }

        return records[recordIndex];
    }
}
