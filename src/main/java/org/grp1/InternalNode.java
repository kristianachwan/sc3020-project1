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
        // Intentionally return the children object instead of cloning it to edit the sentinel node
        return this.children;
    }

    public void delete(int index) {
        if (index < 0 || index + 1 > this.children.size()) {
            throw new Error("Deleting invalid index");
        }
        // It will delete i-1-th and i-th key and record respectively
        keys.remove(index == 0 ? 0 : index - 1);
        children.remove(index);
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public void setChildren(ArrayList<Integer> keys, ArrayList<Node> children) {
        this.keys = keys;
        this.children = children;
    }

    public int getKey() {
        return this.keys.get(0);
    }

    public int getKeyByIndex(int index) {
        return this.keys.get(index);
    }

    public Node getChild(int key) {
        return this.children.get(getChildIndex(key));
    }

    public Node getChildByIndex(int index) {
        return this.children.get(index);
    }

    public NodeChild getChildAsNodeChild(int index) {
        return this.children.get(index);
    }

    public int getChildIndex(int key) {
        for (int i = 0; i < keys.size(); i++) {
            if (key < keys.get(i)) {
                return i;
            }
        }

        return children.size() - 1;
    }


    public void insert(NodeChild newChild) {
        if (!(newChild instanceof Node newNode)) {
            throw new Error("Inserted a non-Node child");
        }
        if (isFull()) {
            throw new Error("Inserted a record in a full node");
        }

        int key = newNode.getKey();

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

}
