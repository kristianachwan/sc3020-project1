package org.grp1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Disk {
    private final int diskSize;
    private final int blockSize;
    private final int recordSize;
    private final List<Block> blocks;
    private String dataFilePath;

    public Disk(int diskSize, int blockSize, int recordSize, String dataFilePath) {
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
        this.blocks = getBlocksFromTSV(dataFilePath);
    }

    public List<Block> getBlocksFromTSV(String dataFilePath) {

        List<Block> blocks = new ArrayList<>();
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
    }

    public int getNumberOfRecordsInBlock() {
        return blockSize / recordSize;
    }

    public void printDiskInformation() {
        System.out.println("Number of records: " + this.getNumberOfRecords());
        System.out.println("Size of a record: " + recordSize);
        System.out.println("Number of records in a block: " + this.getNumberOfRecordsInBlock());
        System.out.println("Number of blocks: " + this.getNumberOfBlocks());
    }

    public int getNumberOfRecords() {
        int numberOfRecords = 0;
        for (Block block : this.blocks) {
            numberOfRecords += block.getNumberOfRecords();
        }

        return numberOfRecords;
    }

    public int getNumberOfBlocks() {
        return this.blocks.size();
    }

}
