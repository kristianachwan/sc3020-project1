package org.grp1;

import org.grp1.constant.Config;
import org.grp1.exception.InvalidIndexException;
import org.grp1.exception.LeafFullException;
import org.grp1.index.BPlusTree;
import org.grp1.model.Address;
import org.grp1.model.Record;
import org.grp1.storage.Block;
import org.grp1.storage.Disk;
import org.grp1.util.Context;
import org.grp1.util.RecordParser;
import org.grp1.util.TSVReader;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final HashSet<Integer> hashSet = new HashSet<>();
    private static Disk disk;
    private static BPlusTree index;

    public static void main(String[] args) {
        System.out.println("Setting up the disk...");
        disk = new Disk(Config.DISK_SIZE, Config.BLOCK_SIZE, Config.RECORD_SIZE);
        index = new BPlusTree(Config.N, disk);
        runExperiment1();
        runExperiment2();
        //runExperiment3();
        //runExperiment4();
        //runExperiment5();
    }

    public static void runExperiment1() {
        Context.reset();
        System.out.println("----------Running experiment 1----------");
        try {
            List<String> listOfRecordStr = TSVReader.ReadTSVFile(Config.DATA_FILE_PATH);
            for (String recordStr : listOfRecordStr) {
                Record newRecord = RecordParser.ParseRecordStr(recordStr);
                disk.insertRecord(newRecord);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        disk.printDiskInformation();
        System.out.println("----------Ending experiment 1----------\n\n");
    }

    public static void runExperiment2() {
        Context.reset();
        System.out.println("----------Running experiment 2----------");
        try {
            for (int i = 0; i < disk.getOccupiedBlock(); i++) {

                Block block = disk.getBlock(i);
                int numOfRecords = block.getNumberOfRecords();
                for (int j = 0; j < numOfRecords; j++) {
                    //System.out.printf("%d %d\n", i, j);
                    Address addr = new Address(i, j);
                    int key = block.getRecord(j).getNumVotes();


                    index.insertAddress(addr, key);

                    hashSet.add(key);
                }
            }
        } catch (LeafFullException e) {
            System.out.println(e.getMessage());
        } catch (InvalidIndexException e) {
            System.out.println(e.getMessage());
        } catch (Error e) {
            System.out.println(e.getMessage());
        }
        int cnta = 0;
        for (int i : hashSet) {
            try {
                if (i == 5027) {
                    System.out.println("Test");
                }

                index.deleteRecord(i);
            } catch (Exception e) {
                System.out.println(i);
                System.out.println(e.getMessage());
            }

        }
        int cntx = 0;
        System.out.println("The parameter n of the B+ tree: " + index.getMaxKeyNumber());
        System.out.println("The number of nodes of the B+ tree: " + index.calculateNodes());
        System.out.println("The number of levels of the B+ tree: " + index.calculateNumLevels());
        //index.printRootKeys();
        System.out.println("----------Ending experiment 2----------\n\n");
    }


    public static void runExperiment3() {
        Context.reset();
        System.out.println("----------Running experiment 3----------");
        // calculate time for linear search
        long startTimeLinear = System.nanoTime();
        List<Record> recordsDisk = disk.getRecordsByNumVotes(500);
        long endTimeLinear = System.nanoTime();
        double linearSearchTime = ((double) endTimeLinear - startTimeLinear) / 1000.0;

        // calculate time for index search
        long startTimeIndex = System.nanoTime();
        List<Record> votes500 = index.getRecordsByNumVotes(500);
        long endTimeIndex = System.nanoTime();
        double indexSearchTime = ((double) endTimeIndex - startTimeIndex) / 1000.0;

        // calculate avg of 'averageRatings'
        double totalAvgRating = 0;
        for (Record record : votes500) {
            totalAvgRating += record.getAverageRating();
        }
        double averageAvgRating = totalAvgRating / votes500.size();
        averageAvgRating = Double.parseDouble(String.format("%.2f", averageAvgRating));

        System.out.println("Index Node Access Count: " + Context.getCount());
        System.out.println("Data Block Access Count: " + Context.getSetCount());
        System.out.println("Average Rating: " + averageAvgRating);
        System.out.println("Index Search Time (μs): " + indexSearchTime);
        System.out.println("Linear Search Time (μs):" + linearSearchTime);
        System.out.println("Linear Search Block Access Count: " + disk.getAccessCount());
        System.out.println("----------Ending experiment 3----------\n\n");
    }

    public static void runExperiment4() {
        Context.reset();
        System.out.println("----------Running experiment 4----------");
        Context.startTimer();
        List<Record> recordsDisk = disk.getRecordsByNumVotes(30000, 40000);
        Context.endTimer();
        double linearSearchTime = ((double) Context.getElapsedTime(TimeUnit.NANOSECONDS)) / 1000.0;

        Context.startTimer();
        List<Record> recordsIndex = index.getRecordsByNumVotes(30000, 40000);
        Context.endTimer();
        double indexSearchTime = ((double) Context.getElapsedTime(TimeUnit.NANOSECONDS)) / 1000.0;

        double rating = 0;
        for (Record r : recordsIndex) {
            rating += r.getAverageRating();
        }
        double avgRating = rating / recordsIndex.size();
        avgRating = Double.parseDouble(String.format("%.2f", avgRating));

        System.out.println("Index Node Access Count: " + Context.getCount());
        System.out.println("Data Block Access Count: " + Context.getSetCount());
        System.out.println("Average Rating: " + avgRating);
        System.out.println("Index Search Time (μs): " + indexSearchTime);
        System.out.println("Linear Search Time (μs): " + linearSearchTime);
        System.out.println("Linear Search Block Access Count: " + disk.getAccessCount());

        System.out.println("----------Ending experiment 4----------\n\n");
    }

    public static void runExperiment5() {
        System.out.println("----------Running experiment 5----------");
        Context.reset();
        try {
            Context.startTimer();
            disk.deleteRecordsByNumVotes(1000);
            Context.endTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double linearDeleteTime = ((double) Context.getElapsedTime(TimeUnit.NANOSECONDS)) / 1000.0;

        Context.reset();
        try {
            Context.startTimer();
            index.deleteRecord(1000);
            Context.endTimer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        double indexDeleteTime = ((double) Context.getElapsedTime(TimeUnit.NANOSECONDS)) / 1000.0;
        System.out.println("The number of nodes of the B+ tree: " + index.calculateNodes());
        System.out.println("The number of levels of the B+ tree: " + index.calculateNumLevels());
        index.printRootKeys();
        System.out.println("Index Delete Time (μs): " + indexDeleteTime);
        System.out.println("Linear Delete Time (μs): " + linearDeleteTime);
        System.out.println("Linear Delete Block Access Count: " + disk.getAccessCount());
        System.out.println("----------Ending experiment 5----------\n\n");
    }
}