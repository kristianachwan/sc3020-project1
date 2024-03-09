package org.grp1.index;

import org.grp1.exception.LeafFullException;
import org.grp1.model.Record;
import java.util.ArrayList;
import java.util.List;

public class BPlusTree {

    public static int indexNodeAccess = 0;
    public static int dataBlockAccess = 0;
    public static int numNodes = 0;
    public static int numLevels = 0;
    private final int maxKeyNumber;
    private final InternalNode sentinelNode;


    public BPlusTree(int maxKeyNumber) {
        this.maxKeyNumber = maxKeyNumber;
        this.sentinelNode = new InternalNode(new ArrayList<>(), new ArrayList<>(), this.maxKeyNumber);
    }

    public void resetAccessCount() {
        indexNodeAccess = 0;
        dataBlockAccess = 0;
    }

    private int getNodeFirstKey(Node node) {
        if (node instanceof LeafNode) {
            return ((LeafNode) node).getKeys().get(0);
        } else {
            return ((InternalNode) node).getKeys().get(0);
        }
    }

    public Record getRecordByNumVotes(int numVotes) {
        Node node = getRoot();
        if (node == null) {
            return null;
        }


        while (!(node instanceof LeafNode)) {
            InternalNode internalNode = (InternalNode) node;
            node = internalNode.getChild(numVotes);
        }

        return ((LeafNode) node).getRecord(numVotes);
    }

    public List<Record> getRecordsByNumVotes(int numVotes) {
        Node node = getRoot();
        if (node == null) {
            return new ArrayList<>();
        }

        while (!(node instanceof LeafNode leafNode)) {
            InternalNode internalNode = (InternalNode) node;
            indexNodeAccess++;
            node = internalNode.getChild(numVotes);
        }

        List<Record> records = new ArrayList<>();
        boolean finished = false;
        while (leafNode != null && !finished) {
            dataBlockAccess++;
            List<Integer> keys = leafNode.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                if (numVotes == keys.get(i)) {
                    records.add(leafNode.getRecordByIndex(i));
                } else if (numVotes < keys.get(i)) {
                    finished = true;
                    break;
                }
            }
            leafNode = leafNode.getNext();
        }

        return records;
    }

    public List<Record> getRecordsByNumVotes(int lowerNumVotes, int higherNumVotes) {
        resetAccessCount();
        Node node = getRoot();
        if (node == null) {
            return new ArrayList<>();
        }

        while (!(node instanceof LeafNode leafNode)) {
            InternalNode internalNode = (InternalNode) node;
            indexNodeAccess++;
            node = internalNode.getChild(lowerNumVotes);
        }

        List<Record> records = new ArrayList<>();
        boolean finished = false;
        while (leafNode != null && !finished) {
            dataBlockAccess++;
            List<Integer> keys = leafNode.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) >= lowerNumVotes && keys.get(i) <= higherNumVotes) {
                    records.add(leafNode.getRecordByIndex(i));
                } else if (higherNumVotes < keys.get(i)) {
                    finished = true;
                    break;
                }
            }
            leafNode = leafNode.getNext();
        }

        return records;
    }

    public Node getRoot() {
        List<Node> children = sentinelNode.getChildren();
        if (children.isEmpty()) {
            return null;
        }

        return children.get(0);
    }

    private boolean recursiveDeleteNode(Node node, int numVotes) throws LeafFullException {
        // Returning true = accessed node has been modified/keys deleted
        /*
         * Main idea:
         * - Recursively find the correct node that contains the record to be deleted
         * - If the node is a leaf node, delete the record
         *
         * - If it is an internal node:
         * - Find the correct child node that contains the record to be deleted
         * - Call the function
         * - Check the size of the child node:
         * - min_size = maxKeyNumber // 2 for internal nodes, (maxKeyNumber + 1) // 2
         * for leaf nodes
         * - Borrow a pointer from left or right sibling (if size > min_size)
         * - If not possible, attempt to merge with left sibling/right sibling:
         */
        if (node instanceof LeafNode leafNode) {

            int idx = leafNode.getRecordIndex(numVotes);

            if (idx == -1) return false;

            leafNode.delete(idx);

            return true;
        } else {
            InternalNode internalNode = (InternalNode) node;
            int index = internalNode.getChildIndex(numVotes);
            Node childNode = internalNode.getChildByIndex(index);

            int minimumNoOfChild = childNode instanceof LeafNode ? maxKeyNumber / 2 : (maxKeyNumber + 1) / 2;

            if (!recursiveDeleteNode(childNode, numVotes)) {
                return false;
            }

            // Checks if the recursive-ed child is above the legal minimum amount of children
            if (childNode.size() >= minimumNoOfChild) {
                return false;
            }

            // Borrows from the left sibling node;
            Node siblingChild = null;
            boolean isLeftSibling = true;

            if (index > 0 && (
                    internalNode.getChildByIndex(index - 1).size() > maxKeyNumber / 2 + 1 ||
                            internalNode.getChildByIndex(index - 1) instanceof InternalNode && internalNode.getChildByIndex(index - 1).size() > maxKeyNumber / 2
            )) {
                // Single node access here
                siblingChild = internalNode.getChildByIndex(index - 1);
            } else if (index < internalNode.size() - 1 && (
                    internalNode.getChildByIndex(index + 1).size() > maxKeyNumber / 2 + 1 ||
                            internalNode.getChildByIndex(index + 1) instanceof InternalNode && internalNode.getChildByIndex(index - 1).size() > maxKeyNumber / 2
            )) {
                // Double node access here
                siblingChild = internalNode.getChildByIndex(index + 1);
                isLeftSibling = false;
            }

            if (siblingChild != null) {
                int indexToBeDeleted = (isLeftSibling ? siblingChild.size() - 1 : 0);

                NodeChild child = siblingChild.getChildAsNodeChild(indexToBeDeleted + (siblingChild instanceof LeafNode ? 0 : 1));

                siblingChild.delete(indexToBeDeleted);

                // Adding to the current node
                childNode.insert(child);

                return false;
            }

            // If neither nodes can borrow, then it should be able to merge with either left node or right node
            // This child node will be deleted
            // but do also check if it is the first or the last index
            // No change in number of nodes accessed (already two)

            if (index > 0) {
                siblingChild = internalNode.getChildByIndex(index - 1);
            } else if (index < internalNode.size()) {
                siblingChild = internalNode.getChildByIndex(index + 1);
            }

            if (siblingChild != null) {
                if (childNode instanceof LeafNode childLeafNode) {

                    for (int i = 0; i < index; i++) {
                        siblingChild.insert(childLeafNode.getRecord(i));
                    }

                    for (int i = index + 1; i < childLeafNode.size(); i++) {
                        siblingChild.insert(childLeafNode.getRecord((i)));
                    }
                } else {
                    InternalNode childInternalNode = (InternalNode) childNode;

                    for (int i = 0; i < index; i++) {
                        siblingChild.insert(childInternalNode.getChild(i));
                    }

                    for (int i = index + 1; i <= childInternalNode.size(); i++) {
                        siblingChild.insert(childInternalNode.getChild((i)));
                    }
                }

                internalNode.delete(index);

                return true;
            } else {
                throw new Error("No sibling is found ?");
            }
        }
    }

    private Node recursiveInsertNode(Node node, Record newRecord) throws LeafFullException {
        // Returns null if there is no need to change the node

        if (node instanceof LeafNode leafNode) {
            // It is the leafNode a.k.a. the base case
            if (leafNode.isFull()) {

                int idx = leafNode.getRecordIndexLowerBound(newRecord.getNumVotes());

                // n + 1
                // if split at (n+2)//2 => n//2 + 1
                List<Record> newRecordList = leafNode
                        .splitRecordList((leafNode.size() + 2) / 2 + (idx <= leafNode.size() / 2 ? -1 : 0));
                List<Integer> newKeyList = leafNode
                        .splitKeyList((leafNode.size() + 2) / 2 + (idx <= leafNode.size() / 2 ? -1 : 0));

                LeafNode newLeafNode = new LeafNode(leafNode, leafNode.getNext(), null, newKeyList, newRecordList,
                        this.maxKeyNumber);
                BPlusTree.numNodes++;

                // Modify the old leafnode
                if (leafNode.getNext() != null) {
                    leafNode.getNext().setPrevious(newLeafNode);
                }
                leafNode.setNext(newLeafNode);

                if (idx <= leafNode.size() / 2) {
                    leafNode.insert(newRecord);
                } else {
                    newLeafNode.insert(newRecord);
                }

                return newLeafNode;
            } else {
                // Find correct index and returns null
                leafNode.insert(newRecord);
            }
        } else {
            // recursive case
            InternalNode internalNode = (InternalNode) node;

            int childIndex = internalNode.getChildIndex(newRecord.getNumVotes());
            Node child = internalNode.getChildByIndex(childIndex);

            Node newNode = recursiveInsertNode(child, newRecord);
            // Update child's key
            if (childIndex > 0) internalNode.updateKey(childIndex);

            if (newNode != null) {
                if (internalNode.isFull()) {

                    List<Node> newNodeList;
                    List<Integer> newKeyList;
                    InternalNode newSiblingInternalNode;

                    newNodeList = internalNode.splitChildrenList(
                            (internalNode.size() + 2) / 2 + (childIndex + 1 <= (internalNode.size() + 2) / 2 ? -1 : 0));
                    newKeyList = internalNode.splitKeyList((internalNode.size() + 2) / 2 - 1
                            + (childIndex + 1 <= (internalNode.size() + 2) / 2 ? -1 : 0));

                    newKeyList.remove(0);

                    newSiblingInternalNode = new InternalNode(newKeyList, newNodeList, this.maxKeyNumber);
                    BPlusTree.numNodes++;

                    if (childIndex + 1 <= (internalNode.size() + 2) / 2) {
                        internalNode.insert(newNode);
                    } else {
                        newSiblingInternalNode.insert(newNode);
                    }
                    return newSiblingInternalNode;
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

    public void insertRecord(Record newRecord) throws LeafFullException {
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
            BPlusTree.numNodes++;
        }

        Node newNode = recursiveInsertNode(root, newRecord);

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
            BPlusTree.numNodes++;

            // Create new sentinel

            ArrayList<Node> sentinelNodeList = new ArrayList<Node>();
            ArrayList<Integer> sentinelKeyList = new ArrayList<Integer>();

            sentinelNodeList.add(root);
            sentinelKeyList.add(0);

            this.sentinelNode.setChildren(sentinelKeyList, sentinelNodeList);
        }
    }

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

    public int getMaxKeyNumber() {
        return maxKeyNumber; // it's better to have calculations here instead?
    }

    public void printRootKeys() {
        InternalNode root = (InternalNode) getRoot();
        System.out.println("Root Node Keys: " + root.getKeys());
    }

    public int calculateNumLevels() {
        Node root = getRoot();
        if (root == null) {
            return 0;
        }

        int height = 1;
        Node current = root;

        while (!(current instanceof LeafNode)) {
            InternalNode internalNode = (InternalNode) current;
            current = internalNode.getChild(0);
            height++;
        }

        return height;
    }


}
