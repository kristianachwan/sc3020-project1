package org.grp1.index;

import org.grp1.exception.LeafFullException;
import org.grp1.model.Address;
import org.grp1.model.Bucket;

import java.util.ArrayList;
import java.util.List;

public class LeafNode extends Node {
    private final int maxNumOfKeys;
    private List<Integer> keys;
    private List<Bucket> buckets;
    private InternalNode parent;
    private LeafNode previous;
    private LeafNode next;

    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, int maxNumOfKeys) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
        this.maxNumOfKeys = maxNumOfKeys;
        this.keys = new ArrayList<Integer>();
        this.buckets = new ArrayList<Bucket>();
    }

    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, List<Integer> keys,
                    List<Bucket> buckets, int maxNumOfKeys) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
        this.keys = keys;
        this.buckets = buckets;
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

    public void setRecords(ArrayList<Integer> keys, ArrayList<Bucket> buckets) {
        this.keys = keys;
        this.buckets = buckets;
    }

    public void insertAddress(Address newAddress, int key) {
        int idx = this.getRecordIndex(key);

        if (idx == -1) {
            // creates a new bucket
            Bucket newBucket = new Bucket(key);
            try {
                insert(newBucket);
            } catch (LeafFullException e) {
                throw new Error("Leaf is full");
            }

        } else {
            Bucket bucket = this.getBucketByIndex(idx);
            bucket.insertAddress(newAddress);
        }
    }

    public void insert(NodeChild newChild) throws LeafFullException {
        if (!(newChild instanceof Bucket newBucket)) {
            throw new Error("Inserted a non-address child");
        }
        if (isFull()) {
            throw new LeafFullException("Inserted a record in a full node");
        }

        int newIndex = getRecordIndexLowerBound(newBucket.getKey());

        keys.add(newIndex, newBucket.getKey());
        buckets.add(newIndex, newBucket);
    }

    public int getKey() {
        return this.keys.get(0);
    }

    public int getKeyByIndex(int index) {
        return this.keys.get(index);
    }

    public int getRecordIndex(int key) {
        for (int i = 0; i < buckets.size(); i++) {
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

    public List<Integer> getKeys() {
        return this.keys;
    }

    public void delete(int index) {
        if (index < 0 || index >= buckets.size()) {
            throw (new Error("Index out of bounds"));
        }

        keys.remove(index);
        buckets.remove(index);
    }

    public Bucket getBucket(int key) {
        int recordIndex = getRecordIndex(key);
        if (recordIndex == -1) {
            return null;
        }

        return buckets.get(recordIndex);
    }

    public Bucket getBucketByIndex(int index) {
        return buckets.get(index);
    }

    public NodeChild getChildAsNodeChild(int index) {
        return buckets.get(index);
    }

    public LeafNode getNext() {
        return next;
    }

    public void setNext(LeafNode next) {
        this.next = next;
    }

    public List<Bucket> splitBucketList(int x) {
        // [0..x) and returns [x..n)
        List<Bucket> left = new ArrayList<>(buckets.subList(0, x));
        List<Bucket> right = new ArrayList<>(buckets.subList(x, buckets.size()));

        buckets = left;

        return right;
    }

    public List<Integer> splitKeyList(int x) {
        // [0..x) and returns [x..n)
        List<Integer> left = new ArrayList<Integer>(keys.subList(0, x));
        List<Integer> right = new ArrayList<Integer>(keys.subList(x, keys.size()));

        keys = left;

        return right;
    }

    public int getMaxNumOfKeys() {
        return this.maxNumOfKeys;
    }

    public int getMinNumOfKeys() {
        return this.maxNumOfKeys / 2;
    }
}
