package org.grp1.index;

import org.grp1.exception.LeafFullException;
import org.grp1.model.Address;
import org.grp1.model.Bucket;

import java.util.ArrayList;
import java.util.List;

public class LeafNode extends Node {
    /**
     * Represents the maximum number of keys
     */
    private final int maxNumOfKeys;
    /**
     * Represents the keys of the node
     */
    private List<Integer> keys;
    /**
     * Represents the buckets of the node
     */
    private List<Bucket> buckets;
    /**
     * Represents the node that points to this node
     */
    private InternalNode parent;
    /**
     * Represents the previous node
     */
    private LeafNode previous;
    /**
     * Represents the next node
     */
    private LeafNode next;

    /**
     * Constructor to instantiate the leaf node
     *
     * @param previous     The previous node
     * @param next         The next node
     * @param parent       The parent node
     * @param maxNumOfKeys The maximum number of keys
     */
    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, int maxNumOfKeys) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
        this.maxNumOfKeys = maxNumOfKeys;
        this.keys = new ArrayList<Integer>();
        this.buckets = new ArrayList<Bucket>();
    }

    /**
     * Constructor to instantiate the leaf node
     *
     * @param previous     The previous node
     * @param next         The next node
     * @param parent       The parent node
     * @param maxNumOfKeys The maximum number of keys
     * @param keys         The keys of the node
     * @param buckets      The buckets of the node
     */
    public LeafNode(LeafNode previous, LeafNode next, InternalNode parent, List<Integer> keys,
                    List<Bucket> buckets, int maxNumOfKeys) {
        this.previous = previous;
        this.next = next;
        this.parent = parent;
        this.keys = keys;
        this.buckets = buckets;
        this.maxNumOfKeys = maxNumOfKeys;
    }

    /**
     * The function to return the status of the node
     *
     * @return The status of the node whether it is full or not
     */
    public boolean isFull() {
        return maxNumOfKeys == this.keys.size();
    }

    /**
     * The function to return the size of the node
     *
     * @return The number of keys contained in this node
     */
    public int size() {
        return this.keys.size();
    }

    /**
     * The function to set the previous node
     *
     * @param previous The previous node
     */
    public void setPrevious(LeafNode previous) {
        this.previous = previous;
    }

    /**
     * The function to set the parent of the node
     *
     * @param parent The parent of the node
     */
    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    /**
     * The function to set the records to the node using the address
     *
     * @param keys    The keys of the node
     * @param buckets The buckets that will contain an address pointing to the record
     */
    public void setRecords(ArrayList<Integer> keys, ArrayList<Bucket> buckets) {
        this.keys = keys;
        this.buckets = buckets;
    }


    /**
     * The function to insert the address corresponding to the Record
     *
     * @param newAddress The address of the record
     * @param key        The key of the record
     */
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

            newBucket.insertAddress(newAddress);

        } else {
            Bucket bucket = this.getBucketByIndex(idx);
            bucket.insertAddress(newAddress);
        }
    }

    /**
     * The function to insert the new child
     *
     * @param newChild The new child to be inserted
     */
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

    /**
     * The function to return the minimum key
     *
     * @return The minimum key
     */
    public int getKey() {
        return this.keys.get(0);
    }

    /**
     * The function to return the key by index
     *
     * @param index The index of the key
     * @return The corresponding index
     */

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

    /**
     * The function to return the lower bound index of the node
     *
     * @param key The key of the record
     * @return The index lower bound corresponding to the key
     */
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

    /**
     * The function to clear the content of the node
     */
    public void clear() {
        this.buckets = new ArrayList<>();
        this.keys = new ArrayList<>();
    }

    /**
     * The function to return the list of keys of the node
     *
     * @return The keys of the node
     */
    public List<Integer> getKeys() {
        return this.keys;
    }

    /**
     * The function to return the buckets of the node
     *
     * @return The buckets of the node
     */
    public List<Bucket> getBuckets() {
        return this.buckets;
    }

    /**
     * The function that deletes an entry based on the index
     *
     * @param index The index of the entry to be deleted
     */
    public void delete(int index) {
        if (index < 0 || index >= buckets.size()) {
            throw (new Error("Index out of bounds"));
        }

        keys.remove(index);
        buckets.remove(index);
    }

    /**
     * The function to return the bucket based on the key
     *
     * @param key The key of the bucket
     * @return The bucket corresponding to the key
     */
    public Bucket getBucket(int key) {
        int recordIndex = getRecordIndex(key);
        if (recordIndex == -1) {
            return null;
        }

        return buckets.get(recordIndex);
    }

    /**
     * The function to return the bucket by index
     *
     * @param index The index of the bucket
     * @return The bucket corresponding to the index
     */
    public Bucket getBucketByIndex(int index) {
        return buckets.get(index);
    }

    /**
     * The function to return the child node as ChildNode
     *
     * @param index The index of the child node
     * @return The corresponding child node
     */
    public NodeChild getChildAsNodeChild(int index) {
        return buckets.get(index);
    }


    /**
     * The function to return the next ndoe
     *
     * @return The next child ndoe
     */
    public LeafNode getNext() {
        return next;
    }

    /**
     * The function to set the next node
     *
     * @param next The next node
     */
    public void setNext(LeafNode next) {
        this.next = next;
    }

    /**
     * The function to split the bucket list
     *
     * @param x The boundary between the left and right list
     * @return The split list of nodes
     */
    public List<Bucket> splitBucketList(int x) {
        // [0..x) and returns [x..n)
        List<Bucket> left = new ArrayList<>(buckets.subList(0, x));
        List<Bucket> right = new ArrayList<>(buckets.subList(x, buckets.size()));

        buckets = left;

        return right;
    }

    /**
     * The function to split the key list
     *
     * @param x The boundary between the left and right ist
     * @return The split list of nodes
     */
    public List<Integer> splitKeyList(int x) {
        // [0..x) and returns [x..n)
        List<Integer> left = new ArrayList<Integer>(keys.subList(0, x));
        List<Integer> right = new ArrayList<Integer>(keys.subList(x, keys.size()));

        keys = left;

        return right;
    }

    /**
     * The function to return the maximum number of keys
     *
     * @return The maximum number of keys
     */
    public int getMaxNumOfKeys() {
        return this.maxNumOfKeys;
    }

    /**
     * The function to return the minimum number of keys
     *
     * @return The minimum number of keys
     */
    public int getMinNumOfKeys() {
        return this.maxNumOfKeys / 2;
    }
}
