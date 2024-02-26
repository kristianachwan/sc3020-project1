package org.grp1;

public class Disk {
    private final int diskSize;
    private final int blockSize;
    private final int recordSize;

    public Disk(int diskSize, int blockSize, int recordSize) {
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
    }

}
