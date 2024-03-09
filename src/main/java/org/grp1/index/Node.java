package org.grp1.index;

import org.grp1.exception.LeafFullException;

public abstract class Node implements NodeChild {

    abstract public boolean isFull();

    abstract public int size(); // keys size

    abstract public void insert(NodeChild nodeChild) throws LeafFullException;

    abstract public void setParent(InternalNode node);

    abstract public void delete(int index);

    abstract public int getKeyByIndex(int index);

    abstract public NodeChild getChildAsNodeChild(int index);

}
