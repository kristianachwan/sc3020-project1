package org.grp1.index;

import org.grp1.exception.LeafFullException;
import org.grp1.model.Address;
import org.grp1.model.Bucket;
import org.grp1.model.Record;
import org.grp1.storage.Disk;
import org.grp1.util.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BPlusTree {

    /**
     * The number of maximum key inside a node
     */
    private final int maxKeyNumber;

    /**
     * The disk that contains this BPlus Tree
     */
    private final Disk disk;
    /**
     * Sentinel node that helps us to generalize the logic, also known as dummy/ghost node
     */
    private final InternalNode sentinelNode;

    /**
     * Constructor to instantiate the BPlusTree
     *
     * @param maxKeyNumber The maximum number of key
     * @param disk         The disk where this BPlusTree belong
     */
    public BPlusTree(int maxKeyNumber, Disk disk) {
        this.maxKeyNumber = maxKeyNumber;
        this.sentinelNode = new InternalNode(new ArrayList<>(), new ArrayList<>(), this.maxKeyNumber);
        this.disk = disk;
    }

    /**
     * The function to test the correctness of the tree based on the key value
     *
     * @return Whether the tree is valid or not
     */
    public boolean testTree() {
        Node root = getRoot();

        while (!(root instanceof LeafNode cur)) {
            root = (Node) root.getChildAsNodeChild(0);
        }

        while (cur != null) {
            for (int i = 1; i < cur.getKeys().size(); i++) {
                if (cur.getKeyByIndex(i) <= cur.getKeyByIndex(i - 1)) {
                    return false;
                }
            }
            cur = cur.getNext();
        }

        return true;
    }


    /**
     * The function that returns the first key
     *
     * @param node the node that it wants to get the first key
     * @return the first key that belongs to the node
     */
    private int getNodeFirstKey(Node node) {
        if (node instanceof LeafNode) {
            return ((LeafNode) node).getKeys().get(0);
        } else {
            return ((InternalNode) node).getKeys().get(0);
        }
    }

    /**
     * The function that get the record by numVotes
     *
     * @param numVotes The associated target numVotes
     * @return The list of records that match numVotes
     */
    public List<Record> getRecordsByNumVotes(int numVotes) {
        Node node = getRoot();
        Context.increment();
        if (node == null) {
            return new ArrayList<>();
        }

        while (!(node instanceof LeafNode leafNode)) {

            InternalNode internalNode = (InternalNode) node;
            node = internalNode.getChild(numVotes);
            Context.increment();
        }

        List<Record> records = new ArrayList<>();
        boolean finished = false;
        while (leafNode != null && !finished) {

            List<Integer> keys = leafNode.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                if (numVotes == keys.get(i)) {
                    leafNode.getBucketByIndex(i).getAddresses().forEach(addr ->
                            records.add(this.disk.getRecordByPointer(addr))
                    );
                } else if (numVotes < keys.get(i)) {
                    finished = true;
                    break;
                }
            }
            leafNode = leafNode.getNext();
            Context.increment();
        }

        return records;
    }

    /**
     * The function that get the multiple records by the range of numVotes
     *
     * @param lowerNumVotes  The lower bound of target
     * @param higherNumVotes The upper bound of target
     * @return The list of Records that fall under the range
     */
    public List<Record> getRecordsByNumVotes(int lowerNumVotes, int higherNumVotes) {
        Node node = getRoot();
        Context.increment();
        if (node == null) {
            return new ArrayList<>();
        }

        while (!(node instanceof LeafNode leafNode)) {
            InternalNode internalNode = (InternalNode) node;
            node = internalNode.getChild(lowerNumVotes);
            Context.increment();
        }

        List<Bucket> buckets = new ArrayList<>();
        boolean finished = false;
        while (leafNode != null && !finished) {
            List<Integer> keys = leafNode.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) >= lowerNumVotes && keys.get(i) <= higherNumVotes) {
                    buckets.add(leafNode.getBucketByIndex(i));
                } else if (higherNumVotes < keys.get(i)) {
                    finished = true;
                    break;
                }
            }
            leafNode = leafNode.getNext();
            Context.increment();
        }

        List<Record> recordsResult = new ArrayList<>();

        buckets.forEach(bucket ->
                bucket.getAddresses().forEach(addr ->
                        recordsResult.add(this.disk.getRecordByPointer(addr))
                )
        );

        return recordsResult;
    }

    /**
     * The function to return the root of the tree
     *
     * @return The root of the BPlus Tree
     */
    public Node getRoot() {
        List<Node> children = sentinelNode.getChildren();
        if (children.isEmpty()) {
            return null;
        }

        return children.get(0);
    }

    /**
     * The function to recursively delete the node by numVotes
     *
     * @param node     The node of consideration recursively
     * @param numVotes The target numVotes to delete
     * @return Whether one of the children has been deleted
     */
    private boolean recursiveDeleteNode(Node node, int numVotes) throws LeafFullException {
        // Returning true = one of the children have been deleted (# of child changed)
        if (node instanceof LeafNode leafNode) {

            int idx = leafNode.getRecordIndex(numVotes);

            if (idx == -1) return false;

            leafNode.delete(idx);

            return true;
        } else {
            InternalNode internalNode = (InternalNode) node;
            int childIndex = internalNode.getChildIndex(numVotes);
            Node childNode = internalNode.getChildByIndex(childIndex);

            int minimumNoOfChild = childNode instanceof LeafNode ? (maxKeyNumber + 1) / 2 : maxKeyNumber / 2;

            if (!recursiveDeleteNode(childNode, numVotes)) {
                // Child node does not change
                if (childIndex > 0) internalNode.updateKey(childIndex);
                return false;
            }

            if (childIndex > 0) internalNode.updateKey(childIndex);

            // Checks if the recursive-ed child is above the legal minimum amount of children
            if (childNode.size() >= minimumNoOfChild) {
                return false;
            }

            // Borrows from the left sibling node;
            Node siblingChild = null;
            boolean isLeftSibling = true;

            if (childIndex > 0 && (
                    internalNode.getChildByIndex(childIndex - 1).size() > minimumNoOfChild
            )) {
                // Check if the left sibling is available
                Context.increment();
                siblingChild = internalNode.getChildByIndex(childIndex - 1);
            } else if (childIndex < internalNode.size() && (
                    internalNode.getChildByIndex(childIndex + 1).size() > minimumNoOfChild
            )) {
                // Check if the right sibling is available
                Context.increment();
                Context.increment();
                siblingChild = internalNode.getChildByIndex(childIndex + 1);
                isLeftSibling = false;
            }

            if (siblingChild != null) {
                // Found a sibling child to borrow from
                int indexToBeDeleted = (isLeftSibling ? siblingChild.size() - (siblingChild instanceof LeafNode ? 1 : 0) : 0);

                NodeChild child = siblingChild.getChildAsNodeChild(indexToBeDeleted);

                siblingChild.delete(indexToBeDeleted);

                // Adding to the current node
                childNode.insert(child);

                if (isLeftSibling) {
                    if (childIndex > 1) internalNode.updateKey(childIndex - 1);
                } else {
                    internalNode.updateKey(childIndex + 1);
                }

                if (childIndex > 0) internalNode.updateKey(childIndex);

                return false;
            }

            // If neither nodes can borrow, then it should be able to merge with either left node or right node
            // This child node will be deleted
            // but do also check if it is the first or the last index
            // No change in number of nodes accessed (already two)

            if (childIndex > 0) {
                siblingChild = internalNode.getChildByIndex(childIndex - 1);
            } else {
                siblingChild = internalNode.getChildByIndex(childIndex + 1);
            }

            if (siblingChild != null) {
                if (childNode instanceof LeafNode childLeafNode) {
                    for (int i = 0; i < childLeafNode.size(); i++) {
                        siblingChild.insert(childLeafNode.getBucketByIndex((i)));
                    }
                } else {
                    InternalNode childInternalNode = (InternalNode) childNode;
                    for (int i = 0; i < childInternalNode.size(); i++) {
                        siblingChild.insert(childInternalNode.getChildByIndex((i)));
                    }
                }

                internalNode.delete(childIndex);

                return true;
            } else {
                // This should not happen under large branching factors.
                throw new Error("No sibling is found");
            }
        }
    }

    /**
     * The function to recursively insert the new record into the BPlus Tree
     *
     * @param node       The node of consideration recursively
     * @param newAddress The address of the record that is to be inserted
     * @param key        The key of the record that we want to insert
     * @return Null if there is no need to change the node, else return the new node created in the process of insertion
     */
    private Node recursiveInsertNode(Node node, Address newAddress, int key) throws LeafFullException {
        // Returns null if there is no need to change the node
        Context.increment();

        if (node instanceof LeafNode leafNode) {
            // It is the leafNode a.k.a. the base case
            if (leafNode.isFull() && leafNode.getRecordIndex(key) == -1) {

                List<Bucket> newBuckets = leafNode.getBuckets();
                List<Integer> keys = leafNode.getKeys();
                Bucket newBucket = new Bucket(key);
                newBucket.insertAddress(newAddress);

                boolean inserted = false;

                for (int i = 0; i < keys.size(); i++) {
                    if (keys.get(i) > key) {
                        keys.add(i, key);
                        newBuckets.add(i, newBucket);
                        inserted = true;
                        break;
                    }
                }

                if (!inserted) {
                    keys.add(key);
                    newBuckets.add(newBucket);
                    inserted = true;
                }

                leafNode.clear();
                leafNode.setRecords(new ArrayList<>(keys.subList(0, (maxKeyNumber + 1) / 2)), new ArrayList<>(newBuckets.subList(0, (maxKeyNumber + 1) / 2)));

                LeafNode newLeafNode = new LeafNode(leafNode, leafNode.getNext(), null, keys.subList((maxKeyNumber + 1) / 2, maxKeyNumber + 1), newBuckets.subList((maxKeyNumber + 1) / 2, maxKeyNumber + 1), this.maxKeyNumber);

                // Modify the old leaf node
                if (leafNode.getNext() != null) {
                    leafNode.getNext().setPrevious(newLeafNode);
                }
                leafNode.setNext(newLeafNode);

                return newLeafNode;
            } else {
                // Find correct index and returns null
                leafNode.insertAddress(newAddress, key);
            }
        } else {
            // recursive case
            InternalNode internalNode = (InternalNode) node;

            int childIndex = internalNode.getChildIndex(key);
            Node child = internalNode.getChildByIndex(childIndex);

            Node newNode = recursiveInsertNode(child, newAddress, key);
            // Update child's key
            if (childIndex > 0) internalNode.updateKey(childIndex);

            if (newNode != null) {
                if (internalNode.isFull()) {

                    List<Node> newNodes = internalNode.getChildren(); // maxKeyNumber + 1
                    List<Integer> keys = internalNode.getKeys(); // maxKeyNumber

                    int newKey = newNode.getKey();
                    boolean inserted = false;

                    if (newNodes.get(0).getKey() > newKey) {
                        keys.add(0, newNodes.get(0).getKey());
                        newNodes.add(0, newNode);
                        inserted = true;
                    } else {
                        for (int i = 0; i < keys.size(); i++) {
                            if (keys.get(i) > key) {
                                keys.add(i, newKey);
                                newNodes.add(i + 1, newNode);
                                inserted = true;
                                break;
                            }
                        }
                    }

                    if (!inserted) {
                        keys.add(newKey);
                        newNodes.add(newNode);
                    }

                    internalNode.clear();
                    int splitIndex = (maxKeyNumber + 2) / 2;
                    internalNode.setRecords(new ArrayList<>(keys.subList(0, splitIndex - 1)), new ArrayList<>(newNodes.subList(0, splitIndex)));

                    return new InternalNode(keys.subList(splitIndex, maxKeyNumber + 1), newNodes.subList(splitIndex, maxKeyNumber + 2), this.maxKeyNumber);
                } else {
                    internalNode.insert(newNode);
                }

                if (newNode instanceof LeafNode) {
                    newNode.setParent(internalNode);
                } else {
                    newNode.setParent(internalNode);
                }
            }

        }

        return null;
    }


    /**
     * The function to insert the record using its address
     *
     * @param newAddress The address of the new record
     * @param key        The key of the record to be inserted
     */
    public void insertAddress(Address newAddress, int key) throws LeafFullException {
        Node root = getRoot();

        if (root == null) {
            // Creates a leaf node
            LeafNode newNode = new LeafNode(null, null, sentinelNode, this.maxKeyNumber);
            ArrayList<Integer> sentinelKeyList = new ArrayList<Integer>();
            ArrayList<Node> sentinelNodeList = new ArrayList<Node>();

            sentinelKeyList.add(0);
            sentinelNodeList.add(newNode);

            sentinelNode.setChildren(sentinelKeyList, sentinelNodeList);

            root = newNode;
        }

        Node newNode = recursiveInsertNode(root, newAddress, key);

        if (newNode != null) {
            // Root node is split into two
            ArrayList<Node> nodeList = new ArrayList<Node>();
            ArrayList<Integer> keyList = new ArrayList<Integer>();

            nodeList.add(root);
            nodeList.add(newNode);

            keyList.add(getNodeFirstKey(newNode));

            InternalNode newRoot = new InternalNode(keyList, nodeList, this.maxKeyNumber);

            root.setParent(newRoot);
            newNode.setParent(newRoot);

            root = newRoot;

            // Create new sentinel

            ArrayList<Node> sentinelNodeList = new ArrayList<Node>();
            ArrayList<Integer> sentinelKeyList = new ArrayList<Integer>();

            sentinelNodeList.add(root);
            sentinelKeyList.add(0);

            this.sentinelNode.setChildren(sentinelKeyList, sentinelNodeList);
        }
    }

    /**
     * The function to delete by numVotes
     *
     * @param numVotes The numVotes of the records to be deleted
     */
    public void deleteRecord(int numVotes) throws Exception {
        Node root = getRoot();

        if (root == null) {
            throw new Error("Deleting on an empty is not allowed");
        }

        boolean isRootChanged = recursiveDeleteNode(root, numVotes);

        if (isRootChanged && root.size() == 0) {
            if (root instanceof InternalNode internalRoot) {
                Node newRoot = internalRoot.getChildByIndex(0);

                this.sentinelNode.getChildren().remove(0);
                this.sentinelNode.getChildren().add(newRoot);
            }
        }
    }

    /**
     * The function to get the maximum number of key
     *
     * @return
     */
    public int getMaxKeyNumber() {
        return maxKeyNumber;
    }

    /**
     * The function to display the content of the root
     */
    public void printRootKeys() {
        InternalNode root = (InternalNode) getRoot();
        System.out.println("Root Node Keys: " + root.getKeys());
    }


    /**
     * The function to calculate the number of levels of BPlus Tree
     *
     * @return The number of levels that BPlus Tree has
     */
    public int calculateNumLevels() {
        Node root = getRoot();
        int height = 0;
        if (root == null) {
            return height;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelLength = queue.size();
            for (int i = 0; i < levelLength; i++) {
                Node node = queue.remove();
                if (node instanceof InternalNode) {
                    queue.addAll(((InternalNode) node).getChildren());
                }
            }
            height++;
        }

        return height;
    }

    /**
     * The function to calculate the number of nodes inside BPlus Tree
     *
     * @return The number of nodes inside BPlus Tree
     */
    public int calculateNodes() {
        Node root = getRoot();
        int cnt = 0;
        if (root == null) {
            return cnt;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {

            int levelLength = queue.size();
            for (int i = 0; i < levelLength; i++) {
                Node node = queue.remove();
                cnt++;
                if (node instanceof InternalNode) {
                    queue.addAll(((InternalNode) node).getChildren());
                }
            }
        }

        return cnt;
    }
}
