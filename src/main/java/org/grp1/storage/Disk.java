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

    /**
     * Represents the disk size
     */
    private final int diskSize;
    /**
     * Represents the block size
     */
    private final int blockSize;
    /**
     * Represents the record size
     */
    private final int recordSize;
    /**
     * Represents the block inside a disk
     */
    private final Block[] blocks;
    /**
     * Represents the access count of the disk
     */
    private int accessCount;
    /**
     * Represents the number of record inside this disk
     */
    private int numOfRecord;

    /**
     * The construcotr to instantiate the disk
     *
     * @param diskSize   The disk size
     * @param blockSize  The block size
     * @param recordSize The record size
     */

    public Disk(int diskSize, int blockSize, int recordSize) {
        this.numOfRecord = 0;
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
        this.blocks = new Block[diskSize / blockSize];
    }

    /**
     * The function to insert the record
     *
     * @param r The record to be inserted
     */
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

    /**
     * The function to get the record by a pointer
     *
     * @param pointer The pointer of the record
     * @return The record corresponding to the address
     */
    public Record getRecordByPointer(Address pointer) {
        Context.addSetInteger(pointer.getBlockIndex());
        try {
            return this.blocks[pointer.getBlockIndex()].getRecord(pointer.getOffset());
        } catch (Exception e) {
            System.out.println("Record could not be found in the block");
        }
        return null;
    }

    /**
     * The function to get the records by numVotes
     *
     * @param numVotes The number of votes for the records
     * @return The records that match this number of votes
     */
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

    /**
     * The function to get the records by range of number of votes
     *
     * @param lower The lower bound of number of votes for the records
     * @param upper The upper bound of number of votes for the records
     */
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

    /**
     * The function to delete the records by number of votes
     *
     * @param numVotes The number of votes
     */
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

    /**
     * The function to return the records inside the disk
     *
     * @return The records inside the disk
     */
    public List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        for (Block b : blocks) {
            if (b != null) {
                records.addAll(Arrays.asList(b.getRecords()));
            }
        }
        return records;
    }

    /**
     * The function to return the number of records inside a block
     *
     * @return The number of records inside a block
     */
    private int getMaxNumberOfRecordsInBlock() {
        return blockSize / recordSize;
    }

    /**
     * The function to print the disk information
     */
    public void printDiskInformation() {
        System.out.println("Number of records: " + this.getNumberOfRecords());
        System.out.println("Size of a record: " + recordSize);
        System.out.println("Number of records in a block: " + Config.NUMBER_OF_RECORDS_IN_BLOCK);
        System.out.println("Number of occupied blocks: " + this.getOccupiedBlock());
    }

    /**
     * The function to return the number of records inside a disk
     *
     * @return The number of records inside a disk
     */
    public int getNumberOfRecords() {
        return this.numOfRecord;
    }

    /**
     * The function to return the number of blocks inside a disk
     *
     * @return The number of blocks inside a disk
     */
    public int getNumberOfBlocks() {
        return this.blocks.length;
    }

    /**
     * The function to return the block based on the index
     *
     * @param index The index of a block
     * @return The block corresponding to the index
     */
    public Block getBlock(int index) {
        return blocks[index];
    }

    /**
     * The function to return the index of an occupied block
     *
     * @return The index of an occupied block
     */

    public int getOccupiedBlock() {
        int count = 0;
        for (Block b : blocks) {
            if (b == null) break;
            count++;
        }
        return count;
    }

    /**
     * The function to return the number of access count of a disk
     *
     * @return The access count of a disk
     */
    public int getAccessCount() {
        return this.accessCount;
    }

}
