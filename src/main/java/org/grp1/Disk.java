package org.grp1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Disk {
    private final int diskSize;
    private final int blockSize;
    private final int recordSize;
    private final ArrayList<Block> blocks;
    private String dataFilePath;

    public Disk(int diskSize, int blockSize, int recordSize, String dataFilePath) {
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordSize = recordSize;
        this.blocks = getBlocksFromTSV(dataFilePath);
    }

    public ArrayList<Block> getBlocksFromTSV(String dataFilePath) {
        ArrayList<Record> records = getRecordsFromTSV(dataFilePath);
        ArrayList<Block> blocks = new ArrayList<>();
        return blocks;
    }

    public ArrayList<Record> getRecordsFromTSV(String dataFilePath) {
        ArrayList<Record> records = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFilePath));
            reader.readLine();

            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                String[] recordValues = line.split("\\s+");
                records.add(new Record(recordValues[0], Float.parseFloat(recordValues[1]), Integer.parseInt(recordValues[2])));
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error while finding file");
        } catch (IOException e) {
            System.out.println("Error while doing IO");
        } catch (Exception e) {
            System.out.println("Error while reading TSV");
        }

        return records;
    }


}
