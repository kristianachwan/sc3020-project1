package disk;

import org.grp1.constant.ErrorMessage;
import org.grp1.exception.BlockFullException;
import org.grp1.exception.InvalidIndexException;

public class Block<T> {

    private int numOfRecord;
    private T[] records;

    public Block(int size) {
        this.numOfRecord = 0;
        // wrapper for creating an array of generic since Java does not support it
        this.records = (T[]) new Object[size];
    }

    public int getNumberOfRecords() {
        return numOfRecord;
    }

    public boolean isEmpty() {
        return numOfRecord == 0;
    }

    public boolean isFull() {
        return numOfRecord >= records.length;
    }

    public T getRecord(int index) throws InvalidIndexException {
        if (isInvalidIndex(index))
            throw new InvalidIndexException(ErrorMessage.INVALID_INDEX_MSG);
        
        return records[index];
    }

    public T[] getRecords() {
        return records;
    }

    public void insertRecord(T obj) throws BlockFullException {
        if (numOfRecord >= records.length)
            throw new BlockFullException(ErrorMessage.BLOCk_FULL_MSG);

        records[numOfRecord] = obj;
        this.numOfRecord += 1;
    }

    public void deleteRecord(int index) throws InvalidIndexException {

        if (isInvalidIndex(index))
            throw new InvalidIndexException(ErrorMessage.INVALID_INDEX_MSG);

        records[index] = null;

        for (int i = index+1; i < records.length; i++) {
            records[index-1] = records[index];
        }

        this.numOfRecord -= 1;

    }

    protected boolean isInvalidIndex(int index) {
        return index < 0 && index < records.length;
    }

}
