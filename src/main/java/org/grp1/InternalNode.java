package org.grp1;

import java.util.ArrayList;

public class InternalNode extends Node {
    private ArrayList<Integer> keys;
    private ArrayList<Node> children;
    private InternalNode parent;

    public InternalNode(ArrayList<Integer> keys, ArrayList<Node> children) {
        this.keys = keys;
        this.children = children;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setChildren(ArrayList<Integer>, ArrayList<Node> children) {
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
