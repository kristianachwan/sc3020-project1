package org.grp1.storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.grp1.model.Record;

public class Disk {

    private int numOfRecord;

    private final int diskSize;
    private final int blockSize;
    private final int recordSize;
    private final List<Block<?>> blocks;

    public Disk(int diskSize, int blockSize, int recordSize) {
        numOfRecord = 0;
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
        this.blocks = new ArrayList<>();
    }

    /*public List<Block<?>> getBlocksFromTSV(String dataFilePath) {

        List<Block<?>> blocks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFilePath));
            reader.readLine();
            String line;

            do {
                int count = 0;
                ArrayList<Record> blockRecords = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] recordValues = line.split("\\s+");
                    blockRecords.add(new Record(recordValues[0], Float.parseFloat(recordValues[1]), Integer.parseInt(recordValues[2])));
                    count++;
                    if (count == this.getNumberOfRecordsInBlock()) {
                        break;
                    }
                }

                if (!blockRecords.isEmpty()) {
                    blocks.add(new Block(blockRecords));
                }

            } while (line != null);

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error while finding file");
        } catch (IOException e) {
            System.out.println("Error while doing IO");
        } catch (Exception e) {
            System.out.println("Error while reading TSV");
        }

        return blocks;
    }*/

    private int getMaxNumberOfRecordsInBlock() {
        return blockSize / recordSize;
    }

    public void printDiskInformation() {
        System.out.println("Number of records: " + this.getNumberOfRecords());
        System.out.println("Size of a record: " + recordSize);
        System.out.println("Number of records in a block: " + this.getMaxNumberOfRecordsInBlock());
        System.out.println("Number of blocks: " + this.getNumberOfBlocks());
    }

    public int getNumberOfRecords() {
        return this.numOfRecord;
    }

    public int getNumberOfBlocks() {
        return this.blocks.size();
    }

}
