package org.grp1;

public class Disk {
    private int diskSize;
    private int blockSize;
    private int recordSize;

    public Disk(int diskSize, int blockSize, int recordSize) {
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
    }

}
