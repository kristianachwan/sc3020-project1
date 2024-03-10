package org.grp1.model;

public class Address {
    /**
     * The index inside blocks in a disk
     */
    int blockIndex;
    /**
     * The offset of a record inside a block
     */
    int offset;

    /**
     * The constructor of the address
     *
     * @param blockIndex
     * @param offset
     */
    public Address(int blockIndex, int offset) {
        this.blockIndex = blockIndex;
        this.offset = offset;
    }

    /**
     * The function to return the block index
     *
     * @return The block index
     */
    public int getBlockIndex() {
        return this.blockIndex;
    }

    /**
     * The function to return the record offset inside of a block
     *
     * @return The record offset
     */
    public int getOffset() {
        return this.offset;
    }

}
