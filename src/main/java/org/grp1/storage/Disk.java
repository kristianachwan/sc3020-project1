package org.grp1.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import org.grp1.constant.ErrorMessage;
import org.grp1.exception.BlockFullException;
import org.grp1.exception.DiskFullException;
import org.grp1.model.Record;
import org.grp1.util.Context;

public class Disk {

    private int accessCount;

    private int numOfRecord;

    private final int diskSize;

    private final int blockSize;
    
    private final int recordSize;

    private Block[] blocks;

    public Disk(int diskSize, int blockSize, int recordSize) {
        this.numOfRecord = 0;
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

        try {

            if (blocks[blockIndex] == null)
                blocks[blockIndex] = new Block(getMaxNumberOfRecordsInBlock());

            blocks[blockIndex].insertRecord(r);

            numOfRecord++;
        } catch (BlockFullException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<Record> getRecordsByNumVotes(int votes) {
        accessCount = 0;
        List<Record> records = new ArrayList<>();
        for (Block b : blocks) {

            if (b == null) break; 

            accessCount++;
            for (Record r : b.getRecords()) {
                if (r != null && r.getNumVotes() == votes) records.add(r);
            }
        }
        return records;
    }

    public List<Record> getRecordsByNumVotes(int lower, int upper) {
        accessCount = 0;
        List<Record> records = new ArrayList<>();
        for (Block b : blocks) {
            if (b == null) break; 

            accessCount++;
            for (Record r : b.getRecords()) {
                if (r != null && r.getNumVotes() >= lower && r.getNumVotes() <= upper) records.add(r);
            }
            
        }
        return records;
    }

    public List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        for (Block b : blocks) {
            if (b != null) {
                records.addAll(Arrays.asList(b.getRecords()));
            }
        }
        return records;
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

    public int getAccessCount() {
        return this.accessCount;
    }

}
