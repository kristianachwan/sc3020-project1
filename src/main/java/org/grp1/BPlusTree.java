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

    public Node getRoot() {
        ArrayList<Node> children = sentinelNode.getChildren();
        if (children.isEmpty()) {
            return null;
        }

        return children.get(0);
    }


}
