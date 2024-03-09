package org.grp1.storage;

import org.grp1.constant.Config;
import org.grp1.constant.ErrorMessage;
import org.grp1.exception.BlockFullException;
import org.grp1.exception.DiskFullException;
import org.grp1.exception.InvalidIndexException;
import org.grp1.model.Address;
import org.grp1.model.Record;
import org.grp1.util.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Disk {

    private final int diskSize;
    private final int blockSize;
    private final int recordSize;
    private final Block[] blocks;
    private int accessCount;
    private int numOfRecord;

    public Disk(int diskSize, int blockSize, int recordSize) {
        this.numOfRecord = 0;
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
        this.blocks = new Block[diskSize / blockSize];
    }

    public void insertRecord(Record r) throws DiskFullException {

        // get block index to insert record
        int blockIndex = numOfRecord / Config.NUMBER_OF_RECORDS_IN_BLOCK;

        if (blockIndex < 0 || blockIndex >= getNumberOfBlocks())
            throw new DiskFullException(ErrorMessage.DISK_FULL_MSG);

        try {

            if (blocks[blockIndex] == null)
                blocks[blockIndex] = new Block(Config.NUMBER_OF_RECORDS_IN_BLOCK);

            blocks[blockIndex].insertRecord(r);

            numOfRecord++;
        } catch (BlockFullException e) {
            System.out.println(e.getMessage());
        }

    }

    public Record getRecordByPointer(Address pointer) {
        Context.addSetInteger(pointer.getBlockIndex());
        try {
            return this.blocks[pointer.getBlockIndex()].getRecord(pointer.getOffset());
        } catch (Exception e) {
            System.out.println("Record could not be found in the block");
        }
        return null;
    }

    public List<Record> getRecordsByNumVotes(int numVotes) {
        accessCount = 0;
        List<Record> records = new ArrayList<>();
        for (Block b : blocks) {

            if (b == null) break;

            accessCount++;
            for (Record r : b.getRecords()) {
                if (r != null && r.getNumVotes() == numVotes) records.add(r);
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

    public void deleteRecordsByNumVotes(int numVotes) throws InvalidIndexException {
        accessCount = 0;
        try {
            for (Block b : blocks) {
                if (b == null) break;

                accessCount++;
                for (int i = 0; i < b.getRecords().length; i++) {
                    if (b.getRecord(i) != null && b.getRecord(i).getNumVotes() == numVotes) {
                        b.deleteRecord(i);
                        numOfRecord--;
                    }
                }
            }
        } catch (InvalidIndexException e) {
            throw new InvalidIndexException(ErrorMessage.INVALID_INDEX_MSG);
        }
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
        System.out.println("Number of records in a block: " + Config.NUMBER_OF_RECORDS_IN_BLOCK);
        System.out.println("Number of occupied blocks: " + this.getOccupiedBlock());
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

    public int getOccupiedBlock() {
        int count = 0;
        for (Block b : blocks) {
            if (b == null) break;
            count++;
        }
        return count;
    }

    public int getAccessCount() {
        return this.accessCount;
    }

}
