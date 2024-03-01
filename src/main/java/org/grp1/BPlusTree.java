package org.grp1;

import java.util.ArrayList;

public class BPlusTree {
    InternalNode sentinelNode = new InternalNode(new ArrayList<>(), new ArrayList<>());

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

    public ArrayList<Record> getRecordsByNumVotes(int numVotes) {
        Node node = getRoot();
        if (node == null) {
            return new ArrayList<>();
        }

        while (!(node instanceof LeafNode leafNode)) {
            InternalNode internalNode = (InternalNode) node;
            node = internalNode.getChild(numVotes);
        }

        ArrayList<Record> records = new ArrayList<>();
        boolean finished = false;
        while (leafNode != null && !finished) {
            ArrayList<Integer> keys = leafNode.getKeys();
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

    public ArrayList<Record> getRecordsByNumVotes(int lowerNumVotes, int higherNumVotes) {
        Node node = getRoot();
        if (node == null) {
            return new ArrayList<>();
        }

        while (!(node instanceof LeafNode leafNode)) {
            InternalNode internalNode = (InternalNode) node;
            node = internalNode.getChild(lowerNumVotes);
        }

        ArrayList<Record> records = new ArrayList<>();
        boolean finished = false;
        while (leafNode != null && !finished) {
            ArrayList<Integer> keys = leafNode.getKeys();
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
        ArrayList<Node> children = sentinelNode.getChildren();
        if (children.isEmpty()) {
            return null;
        }

        return children.get(0);
    }

}
