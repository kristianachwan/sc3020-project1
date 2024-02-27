package org.grp1;

public class InternalNode extends Node {
    int[] keys;
    private Node[] children;
    private InternalNode parent;

    public InternalNode(int[] keys, Node[] children) {
        this.keys = keys;
        this.children = children;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setChildren(int[] keys, Node[] children) {
        this.keys = keys;
        this.children = children;
    }

    public int getChildIndex(int key) {
        for (int i = 0; i < keys.length; i++) {
            if (key < keys[i]) {
                return i;
            }
        }

        return keys.length;
    }

}
