package org.grp1;

import java.util.List;

public class InternalNode extends Node {
    private List<Integer> keys;
    private List<Node> children;
    private InternalNode parent;

    public InternalNode(List<Integer> keys, List<Node> children) {
        this.keys = keys;
        this.children = children;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setChildren(List<Integer> keys, List<Node> children) {
        this.keys = keys;
        this.children = children;
    }

    public int getChildIndex(int key) {
        for (int i = 0; i < keys.size(); i++) {
            if (key < keys.get(i)) {
                return i;
            }
        }

        return children.size() - 1;
    }

}
