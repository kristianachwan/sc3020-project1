package org.grp1.storage;

import org.grp1.constant.ErrorMessage;
import org.grp1.exception.BlockFullException;
import org.grp1.exception.InvalidIndexException;
import org.grp1.model.Record;

public class Block {
    /**
     * Represents the records inside of this block
     */
    private final Record[] records;

    /**
     * Represents the number of records inside of this block
     */
    private int numOfRecord;

    /**
     * The constructor to instantiate a block
     *
     * @param size
     */
    public Block(int size) {
        this.numOfRecord = 0;
        this.records = new Record[size];
    }

    /**
     * The function to get the number of records inside of this block
     *
     * @return The number of records inside of the block
     */
    public int getNumberOfRecords() {
        return numOfRecord;
    }

    /**
     * The function to return whether the block is empty or not
     *
     * @return The status of the block whether it is empty or not
     */
    public boolean isEmpty() {
        return numOfRecord == 0;
    }

    /**
     * The function to return whether the block is full or not
     *
     * @return The status of the block whether it is full or not
     */
    public boolean isFull() {
        return numOfRecord >= records.length;
    }

    /**
     * The function to get the record based on the index / offset
     *
     * @param index The index of the block
     * @return The corresponding record based on the index
     */
    public Record getRecord(int index) throws InvalidIndexException {
        if (isInvalidIndex(index))
            throw new InvalidIndexException(ErrorMessage.INVALID_INDEX_MSG);
        return records[index];
    }

    /**
     * The function to return all of the records inside of a block
     *
     * @return
     */
    public Record[] getRecords() {
        return records;
    }

    /**
     * The function to insert the record
     *
     * @param obj The record to be inserted
     */
    public void insertRecord(Record obj) throws BlockFullException {
        if (numOfRecord >= records.length)
            throw new BlockFullException(ErrorMessage.BLOCK_FULL_MSG);

        records[numOfRecord] = obj;
        this.numOfRecord += 1;
    }

    /**
     * The function to delete a record in a disk
     *
     * @param index The index of a record to be deleted
     */
    public void deleteRecord(int index) throws InvalidIndexException {

        if (isInvalidIndex(index))
            throw new InvalidIndexException(ErrorMessage.INVALID_INDEX_MSG);

        records[index] = null;

        for (int i = index + 1; i < records.length; i++) {
            records[i - 1] = records[i];
        }

        this.numOfRecord -= 1;

    }

    /**
     * The function to check whether the index is a valid index
     *
     * @param index The index of the record
     * @return The status of the index whether it is valid or not
     */
    protected boolean isInvalidIndex(int index) {
        return index < 0 || index >= records.length;
    }

}
