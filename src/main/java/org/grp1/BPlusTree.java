package org.grp1;

import java.util.ArrayList;

public class BPlusTree {
    InternalNode sentinelNode = new InternalNode(new ArrayList<>(), new ArrayList<>());

    private int getNodeFirstKey(Node node) {
        if (node instanceof LeafNode) {
            return ((LeafNode) node).getRecord(0).getNumVotes();
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
        ArrayList<Node> children = sentinelNode.getChildren();
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
                // Split and returns a new leaf node
                // Why n/2? upper bound((n+1)/2) - 1 = (n+1 + 1)//2 - 1 = n//2

                int idx = leafNode.getRecordIndexLowerBound(newRecord.getNumVotes());

                // New record belongs to the original index
                // Then we shall remove the last (n+1)//2 elements
                ArrayList<Record> newRecordList = leafNode.splitRecordList(leafNode.size() / 2 + (idx <= leafNode.size() / 2 ? 1 : 0));
                ArrayList<Integer> newKeyList = leafNode.splitKeyList(leafNode.size() / 2 + (idx <= leafNode.size() / 2 ? 1 : 0));

                // yo ndak tau parentnya ya bikin null dulu ngentot
                LeafNode newLeafNode = new LeafNode(leafNode, leafNode.getNext(), null, newKeyList, newRecordList);

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
                    // must insert new node at childIndex + 1
                    // assuming n + 1 childs
                    // then we must split child with size upb ((n+2)/2) => n+3 // 2 => n+1//2 + 1
                    // [0..n+1//2 + 1) and [n+1//2 + 1..n+2)

                    ArrayList<Node> newNodeList;
                    ArrayList<Integer> newKeyList;
                    InternalNode newSiblingInternalNode;

                    if (childIndex + 1 <= (internalNode.size() + 1) / 2) {
                        // split at n + 1//2 because newNode belongs to left
                        newNodeList = internalNode.splitChildrenList((internalNode.size() + 1) / 2);
                        newKeyList = internalNode.splitKeyList((internalNode.size() + 1) / 2 - 1);

                        newKeyList.remove(0);

                        newSiblingInternalNode = new InternalNode(newKeyList, newNodeList);

                        // modify old internal node
                        internalNode.insertNode(newNode, newNodeKey);


                    } else {
                        // split at n + 1//2 + 1 because newNode belongs to right
                        newNodeList = internalNode.splitChildrenList((internalNode.size() + 1) / 2 + 1);
                        newKeyList = internalNode.splitKeyList((internalNode.size() + 1) / 2);

                        newKeyList.remove(0);

                        newSiblingInternalNode = new InternalNode(newKeyList, newNodeList);

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
            LeafNode newNode = new LeafNode(null, null, sentinelNode);
            ArrayList<Integer> sentinelKeyList = new ArrayList<Integer>();
            ArrayList<Node> sentinelNodeList = new ArrayList<Node>();


            sentinelKeyList.add(Integer.valueOf(69));
            sentinelNodeList.add(newNode);


            sentinelNode.setChildren(sentinelKeyList, sentinelNodeList);

            root = newNode;
        }

        Node newNode = recursiveInsertNode(root, newRecord);

        if (newNode != null) {
            // Root node is split into two
            ArrayList<Node> nodeList = new ArrayList<Node>();
            ArrayList<Integer> keyList = new ArrayList<Integer>();

            nodeList.add(root);
            nodeList.add(newNode);

            keyList.add(getNodeFirstKey(root));
            keyList.add(getNodeFirstKey(newNode));

            InternalNode newRoot = new InternalNode(keyList, nodeList);

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

}
