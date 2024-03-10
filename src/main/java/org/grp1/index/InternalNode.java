package org.grp1.index;

import java.util.ArrayList;
import java.util.List;

public class InternalNode extends Node {
    /**
     * Represents the maximum number of keys that the internal node can contain
     */
    private final int maxNumOfKeys;
    /**
     * Represents the keys of the node
     */
    private List<Integer> keys;
    /**
     * Represents the children of the nodes
     */
    private List<Node> children;
    /**
     * Represents the parent of the node
     */
    private InternalNode parent;

    /**
     * Constructor to instantiate the node
     *
     * @param keys         The keys of the node
     * @param children     The children that this node points to
     * @param maxNumOfKeys The maximum number of keys that this node can contain
     */

    public InternalNode(List<Integer> keys, List<Node> children, int maxNumOfKeys) {
        this.keys = keys;
        this.children = children;
        this.maxNumOfKeys = maxNumOfKeys;
    }

    /**
     * The function to indicate whether the node is at full capacity
     *
     * @return The status of the node whether it is full or not
     */
    public boolean isFull() {
        return maxNumOfKeys == this.keys.size();
    }

    /**
     * The function to get the size of the node
     *
     * @return The size of the node
     */
    public int size() {
        return this.keys.size();
    }

    /**
     * The function to return all the keys of the node
     *
     * @return The keys of the node
     */
    public List<Integer> getKeys() {
        return this.keys;
    }

    /**
     * The function to return the children of the node
     *
     * @return The children of the node
     */
    public List<Node> getChildren() {
        // Intentionally return the children object instead of cloning it to edit the sentinel node
        return this.children;
    }

    /**
     * The function to clear the contents of the node
     */
    public void clear() {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    /**
     * The function helper to delete the entry based on the index
     *
     * @param index The index of the entry to be deleted
     */
    public void delete(int index) {
        if (index < 0 || index + 1 > this.children.size()) {
            throw new Error("Deleting invalid index");
        }
        // It will delete i-1-th and i-th key and record respectively
        keys.remove(index == 0 ? 0 : index - 1);
        children.remove(index);
    }

    /**
     * The function to set the parent of the node
     *
     * @param parent The node that points to this node
     */

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    /**
     * The function to set the children of the node
     *
     * @param keys     The keys that form range of keys for the children
     * @param children The nodes to point from this node
     */
    public void setChildren(ArrayList<Integer> keys, ArrayList<Node> children) {
        this.keys = keys;
        this.children = children;
    }

    /**
     * The function to return the minimum value of the key
     *
     * @return The minimum value of the key
     */
    public int getKey() {
        return this.children.get(0).getKey();
    }

    /**
     * The function to return the key by index
     *
     * @param index The index of the key
     * @return The key corresponding to the index
     */
    public int getKeyByIndex(int index) {
        return this.keys.get(index);
    }

    /**
     * The function to return the child based on the key
     *
     * @param key The key of the child
     * @return The child corresponding to the key
     */
    public Node getChild(int key) {
        return this.children.get(getChildIndex(key));
    }

    /**
     * The function to return the child based on the index
     *
     * @param index The index of the child
     * @return The child corresponding to the index
     */
    public Node getChildByIndex(int index) {
        return this.children.get(index);
    }

    /**
     * The function to return the child node based on index as NodeChild
     *
     * @param index The index of the child node
     * @return The child corresponding to the index in the NodeChild form
     */
    public NodeChild getChildAsNodeChild(int index) {
        return this.children.get(index);
    }

    /**
     * The function to return the child index by the key
     *
     * @param key The key of the child
     * @return The corresponding child of the node based on the key
     */

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


    /**
     * The function to insert the new child
     *
     * @param newChild The node corresponding to the new node to be inserted
     */
    public void insert(NodeChild newChild) {
        if (!(newChild instanceof Node newNode)) {
            throw new Error("Inserted a non-Node child");
        }
        if (isFull()) {
            throw new Error("Inserted a record in a full node");
        }

        int key = newNode.getKey();

        int newIndex = getChildIndex(key);

        if (newIndex == 0) {
            Node firstChild = this.children.get(0);
            int childKey;

            if (firstChild instanceof LeafNode leafChild) {
                childKey = leafChild.getKey();
            } else {
                childKey = firstChild.getKey();
            }

            keys.add(0, (childKey > key ? childKey : key));
            children.add((childKey > key ? 0 : 1), newNode);
        } else {
            keys.add(newIndex, key);
            children.add(newIndex + 1, newNode);
        }


    }

    /**
     * The function to split the children list
     *
     * @param x The boundary between the left and right list
     * @return The split list of nodes
     */
    public List<Node> splitChildrenList(int x) {
        // [0..x) and returns [x..n)
        List<Node> left = new ArrayList<Node>(children.subList(0, x));
        List<Node> right = new ArrayList<Node>(children.subList(x, children.size()));

        children = left;

        return right;
    }

    /**
     * The function to split the key list
     *
     * @param x The boundary between the left and right ist
     * @return The split list of nodes
     */
    public ArrayList<Integer> splitKeyList(int x) {
        // [0..x) and returns [x..n)
        ArrayList<Integer> left = new ArrayList<Integer>(keys.subList(0, x));
        ArrayList<Integer> right = new ArrayList<Integer>(keys.subList(x, keys.size()));

        keys = left;

        return right;
    }

    /**
     * The function to update the key
     *
     * @param index The corresponding index of the node to be updated
     * @return The status whether the new key is updated
     */
    public boolean updateKey(int index) {
        // Returns true if a new key is updated

        Node child = this.children.get(index);
        int newKey;

        if (child instanceof LeafNode leafChild) {
            newKey = leafChild.getKey();
        } else {
            InternalNode internalChild = (InternalNode) child;
            newKey = internalChild.getKey();
        }

        if (newKey != this.keys.get(index - 1)) {
            this.keys.set(index - 1, newKey);
            return true;
        }

        return false;
    }


    /**
     * The function to return the maximum number of keys
     *
     * @return The maximum number of keys
     */
    public int getMaxNumOfKeys() {
        return this.maxNumOfKeys;
    }

    /**
     * The function to return the minimum number of keys
     *
     * @return The minimum number of keys
     */
    public int getMinNumOfKeys() {
        return (this.maxNumOfKeys) / 2;
    }

}
