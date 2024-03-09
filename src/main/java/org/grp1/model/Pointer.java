package org.grp1.model;

public class Pointer {
    int blockIndex;
    int offset;

    public Pointer(int blockIndex, int offset) {
        this.blockIndex = blockIndex;
        this.offset = offset;
    }

    public int getBlockIndex() {
        return this.blockIndex;
    }

    public int getOffset() {
        return this.offset;
    }

}
