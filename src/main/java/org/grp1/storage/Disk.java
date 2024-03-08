package org.grp1.storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.grp1.constant.ErrorMessage;
import org.grp1.exception.BlockFullException;
import org.grp1.exception.DiskFullException;
import org.grp1.model.Record;

public class Disk {

    private int numOfRecord;

    private final int diskSize;

    private final int blockSize;
    
    private final int recordSize;

    private Block[] blocks;

    public Disk(int diskSize, int blockSize, int recordSize) {
        numOfRecord = 0;
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
        this.blocks = new Block[diskSize/blockSize];
    }

    public void insertRecord(Record r) throws DiskFullException {

        // get block index to insert record
        int blockIndex = numOfRecord / getMaxNumberOfRecordsInBlock();

        if (blockIndex < 0 || blockIndex >= getNumberOfBlocks())
            throw new DiskFullException(ErrorMessage.DISK_FULL_MSG);

        Block blockToAddRecord = blocks[blockIndex];

        try {
            if (blockToAddRecord == null)
                blockToAddRecord = new Block(getMaxNumberOfRecordsInBlock());

            blockToAddRecord.insertRecord(r);

            numOfRecord++;
        } catch (BlockFullException e) {
            System.out.println(e.getMessage());
        }

    }

    private int getMaxNumberOfRecordsInBlock() {
        return blockSize / recordSize;
    }

    public void printDiskInformation() {
        //System.out.println("Disk Size: " + this.diskSize);
        //System.out.println("Used Space: " + this.numOfRecord*recordSize);
        //System.out.println("Free Space: " + (this.diskSize - (this.numOfRecord*recordSize)));
        System.out.println("Number of records: " + this.getNumberOfRecords());
        System.out.println("Size of a record: " + recordSize);
        System.out.println("Number of records in a block: " + this.getMaxNumberOfRecordsInBlock());
        System.out.println("Number of blocks: " + this.getNumberOfBlocks());
    }

    public int getNumberOfRecords() {
        return this.numOfRecord;
    }

    public int getNumberOfBlocks() {
        return this.blocks.length;
    }

    public Block getBlock(int index) {
        return blocks[index];
    }

}
