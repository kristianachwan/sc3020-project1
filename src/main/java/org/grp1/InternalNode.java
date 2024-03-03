package org.grp1;

import java.util.ArrayList;
import java.util.List;

public class InternalNode extends Node {
    private final int maxNumOfKeys;
    private List<Integer> keys;
    private List<Node> children;
    private InternalNode parent;

    public InternalNode(List<Integer> keys, List<Node> children, int maxNumOfKeys) {
        this.keys = keys;
        this.children = children;
        this.maxNumOfKeys = maxNumOfKeys;
    }

    public boolean isFull() {
        return maxNumOfKeys == this.keys.size();
    }

    public int size() {
        return this.keys.size();
    }

    public List<Integer> getKeys() {
        return this.keys;
    }

    public List<Node> getChildren() {
        return this.children;
    }


    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setChildren(List<Integer> keys, List<Node> children) {
        this.keys = keys;
        this.children = children;
    }

    public Node getChild(int key) {
        return this.children.get(getChildIndex(key));
    }

    public int getChildIndex(int key) {
        for (int i = 0; i < keys.size(); i++) {
            if (key < keys.get(i)) {
                return i;
            }
        }

        return children.size() - 1;
    }

    public void insertNode(Node newNode, int key) {
        if (isFull()) {
            throw new Error("Inserted a record in a full node");
        }

        int newIndex = getChildIndex(key);

        keys.add(newIndex, key);
        children.add(newIndex, newNode);
    }

    public List<Node> splitChildrenList(int x) {
        // [0..x) and returns [x..n)
        List<Node> left = new ArrayList<Node>(children.subList(0, x));
        List<Node> right = new ArrayList<Node>(children.subList(x, children.size()));

        children = left;

        return right;
    }

    public List<Integer> splitKeyList(int x) {
        // [0..x) and returns [x..n)
        List<Integer> left = new ArrayList<Integer>(keys.subList(0, x));
        List<Integer> right = new ArrayList<Integer>(keys.subList(x, keys.size()));

        keys = left;

        return right;
    }

}
