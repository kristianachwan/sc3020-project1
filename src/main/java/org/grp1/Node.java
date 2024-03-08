package org.grp1;

public abstract class Node {

    abstract public boolean isFull();

    abstract public void setParent(InternalNode node);

    abstract public int getMaxNumOfKeys();

    abstract public int getMinNumOfKeys();

}
