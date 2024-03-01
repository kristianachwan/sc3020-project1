package org.grp1;

import java.util.ArrayList;

public class InternalNode extends Node {
    private ArrayList<Integer> keys;
    private ArrayList<Node> children;
    private InternalNode parent;
    private int maxNumOfKeys;

    public InternalNode(ArrayList<Integer> keys, ArrayList<Node> children) {
        this.keys = keys;
        this.children = children;
    }

    private boolean isFull() {
        return maxNumOfKeys == children.size();
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setChildren(ArrayList<Integer> keys, ArrayList<Node> children) {
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
