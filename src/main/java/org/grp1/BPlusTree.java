package org.grp1;

import java.util.ArrayList;

public class BPlusTree {
    InternalNode sentinelNode = new InternalNode(new ArrayList<>(), new ArrayList<>());

    public Node getRoot() {
        ArrayList<Node> children = sentinelNode.getChildren();
        if (children.isEmpty()) {
            return null;
        }

        return children.get(0);
    }


}
