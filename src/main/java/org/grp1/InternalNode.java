package org.grp1;

import java.util.ArrayList;

public class InternalNode extends Node {
    private final int maxNumOfKeys;
    private ArrayList<Integer> keys;
    private ArrayList<Node> children;
    private InternalNode parent;

    public InternalNode(ArrayList<Integer> keys, ArrayList<Node> children, int maxNumOfKeys) {
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

    public ArrayList<Integer> getKeys() {
        return this.keys;
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

    public Node getChild(int key) {
        return this.children.get(getChildIndex(key));
    }

    public int getChildIndex(int key) {
        for (int i = 0; i < keys.size(); i++) {
            if (key < keys.get(i)) {
                return i;
            }
            if (key == keys.get(i)) {
                return i + 1;
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

    public ArrayList<Node> splitChildrenList(int x) {
        // [0..x) and returns [x..n)
        ArrayList<Node> left = new ArrayList<Node>(children.subList(0, x));
        ArrayList<Node> right = new ArrayList<Node>(children.subList(x, children.size()));

        children = left;

        return right;
    }

    public ArrayList<Integer> splitKeyList(int x) {
        // [0..x) and returns [x..n)
        ArrayList<Integer> left = new ArrayList<Integer>(keys.subList(0, x));
        ArrayList<Integer> right = new ArrayList<Integer>(keys.subList(x, keys.size()));

        keys = left;

        return right;
    }

    public boolean updateKey(int index) {
        // Returns true if a new key is updated

        Node child = this.children.get(index);
        int newKey;

        if (child instanceof LeafNode leafChild) {
            newKey = leafChild.getKeys().get(0);
        } else {
            InternalNode internalChild = (InternalNode) child;
            newKey = internalChild.getKeys().get(0);
        }

        if (newKey != this.keys.get(index)) {
            this.keys.set(index, newKey);
            return true;
        }

        return false;
    }

    public int getMaxNumOfKeys() {
        return this.maxNumOfKeys;
    }

    public int getMinNumOfKeys() {
        return (this.maxNumOfKeys) / 2;
    }


}
