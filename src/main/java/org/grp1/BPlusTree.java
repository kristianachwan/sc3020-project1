package org.grp1;

import org.grp1.model.Record;
import java.util.ArrayList;
import java.util.List;

public class BPlusTree {
    private final int maxKeyNumber;
    private final InternalNode sentinelNode;

    public BPlusTree(int maxKeyNumber) {
        this.maxKeyNumber = maxKeyNumber;
        this.sentinelNode = new InternalNode(new ArrayList<>(), new ArrayList<>(), this.maxKeyNumber);
    }

    private int getNodeFirstKey(Node node) {
        if (node instanceof LeafNode) {
            return ((LeafNode) node).getRecordByIndex(0).getNumVotes();
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

    public Node getRoot() {
        List<Node> children = sentinelNode.getChildren();
        if (children.isEmpty()) {
            return null;
        }

        return children.get(0);
    }

    private Node recursiveInsertNode(Node node, Record newRecord) {
        // Returns null if there is no need to change the node

        if (node instanceof LeafNode leafNode) {
            // It is the leafNode a.k.a. the base case
            if (leafNode.isFull()) {


                int idx = leafNode.getRecordIndexLowerBound(newRecord.getNumVotes());

                // n + 1
                // if split at (n+2)//2 => n//2 + 1
                List<Record> newRecordList = leafNode.splitRecordList((leafNode.size() + 2) / 2 + (idx <= leafNode.size() / 2 ? -1 : 0));
                List<Integer> newKeyList = leafNode.splitKeyList((leafNode.size() + 2) / 2 + (idx <= leafNode.size() / 2 ? -1 : 0));

                LeafNode newLeafNode = new LeafNode(leafNode, leafNode.getNext(), null, newKeyList, newRecordList, this.maxKeyNumber);

                // Modify the old leafnode
                if (leafNode.getNext() != null) {
                    leafNode.getNext().setPrevious(newLeafNode);
                }
                leafNode.setNext(newLeafNode);

                if (idx <= leafNode.size() / 2) {
                    leafNode.insertRecord(newRecord);
                } else {
                    newLeafNode.insertRecord(newRecord);
                }

                return newLeafNode;
            } else {
                // Find correct index and returns null
                leafNode.insertRecord(newRecord);
            }
        } else {
            // recursive case
            InternalNode internalNode = (InternalNode) node;

            int childIndex = internalNode.getChildIndex(newRecord.getNumVotes());
            Node child = internalNode.getChild(childIndex);

            Node newNode = recursiveInsertNode(child, newRecord);

            if (newNode != null) {
                int newNodeKey = getNodeFirstKey(newNode);
                if (internalNode.isFull()) {

                    List<Node> newNodeList;
                    List<Integer> newKeyList;
                    InternalNode newSiblingInternalNode;

                    newNodeList = internalNode.splitChildrenList((internalNode.size() + 2) / 2 + (childIndex + 1 <= (internalNode.size() + 2) / 2 ? -1 : 0));
                    newKeyList = internalNode.splitKeyList((internalNode.size() + 2) / 2 - 1 + (childIndex + 1 <= (internalNode.size() + 2) / 2 ? -1 : 0));

                    newKeyList.remove(0);

                    newSiblingInternalNode = new InternalNode(newKeyList, newNodeList, this.maxKeyNumber);

                    if (childIndex + 1 <= (internalNode.size() + 2) / 2) {
                        internalNode.insertNode(newNode, newNodeKey);
                    } else {
                        newSiblingInternalNode.insertNode(newNode, newNodeKey);
                    }
                    return newSiblingInternalNode;
                } else {
                    internalNode.insertNode(newNode, newNodeKey);
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

    public void insertRecord(Record newRecord) {
        Node root = getRoot();


        if (root == null) {
            // Creates a leaf node
            LeafNode newNode = new LeafNode(null, null, sentinelNode, this.maxKeyNumber);
            List<Integer> sentinelKeyList = new ArrayList<Integer>();
            List<Node> sentinelNodeList = new ArrayList<Node>();


            sentinelKeyList.add(0);
            sentinelNodeList.add(newNode);


            sentinelNode.setChildren(sentinelKeyList, sentinelNodeList);

            root = newNode;
        }

        Node newNode = recursiveInsertNode(root, newRecord);

        if (newNode != null) {
            // Root node is split into two
            List<Node> nodeList = new ArrayList<Node>();
            List<Integer> keyList = new ArrayList<Integer>();

            nodeList.add(root);
            nodeList.add(newNode);

            keyList.add(getNodeFirstKey(root));
            keyList.add(getNodeFirstKey(newNode));

            InternalNode newRoot = new InternalNode(keyList, nodeList, this.maxKeyNumber);

            root.setParent(newRoot);
            newNode.setParent(newRoot);

            root = newRoot;

            // Create new sentinel

            List<Node> sentinelNodeList = new ArrayList<Node>();
            List<Integer> sentinelKeyList = new ArrayList<Integer>();

            sentinelNodeList.add(root);
            sentinelKeyList.add(0);

            this.sentinelNode.setChildren(sentinelKeyList, sentinelNodeList);
        }


    }

}
