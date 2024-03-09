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

import java.util.List;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Disk disk;
    private static BPlusTree index;
    private static Context context;

    public static void main(String[] args) {
        System.out.println("Setting up the disk...");
        disk = new Disk(Config.DISK_SIZE, Config.BLOCK_SIZE, Config.RECORD_SIZE);
        index = new BPlusTree(Config.TREE_NODE_SIZE, disk);
        context = new Context();
        runExperiment1();
        runExperiment2();
        runExperiment3();
        runExperiment4();
        runExperiment5();
    }

    public static void runExperiment1() {
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
                }
            }
        } catch (LeafFullException e) {
            System.out.println(e.getMessage());
        } catch (InvalidIndexException e) {
            System.out.println(e.getMessage());
        } catch (Error e) {
            System.out.println(e.getMessage());
        }

        System.out.println("The parameter n of the B+ tree: " + index.getMaxKeyNumber());
        System.out.println("The number of nodes of the B+ tree: " + BPlusTree.numNodes);
        System.out.println("The number of levels of the B+ tree: " + BPlusTree.numLevels);
        index.printRootKeys();
        System.out.println("----------Ending experiment 2----------\n\n");
    }


    public static void runExperiment3() {
        System.out.println("----------Running experiment 3----------");
        // calculate time for linear search
        long startTimeLinear = System.nanoTime();
        List<Record> recordsDisk = disk.getRecordsByNumVotes(500);
        long endTimeLinear = System.nanoTime();
        long linearSearchTime = (endTimeLinear - startTimeLinear);

        // calculate time for index search
        long startTimeIndex = System.nanoTime();
        List<Record> votes500 = index.getRecordsByNumVotes(500);
        long endTimeIndex = System.nanoTime();
        long indexSearchTime = (endTimeIndex - startTimeIndex);

        // calculate avg of 'averageRatings'
        double totalAvgRating = 0;
        for (Record record : votes500) {
            totalAvgRating += record.getAverageRating();
        }
        double averageAvgRating = totalAvgRating / votes500.size();
        averageAvgRating = Double.parseDouble(String.format("%.2f", averageAvgRating));

        System.out.println("Index Node Access Count: " + BPlusTree.indexNodeAccess);
        System.out.println("Data Block Access Count: " + BPlusTree.dataBlockAccess);
        System.out.println("Average Rating: " + averageAvgRating);
        System.out.println("Index Search Time (ns): " + indexSearchTime);
        System.out.println("Linear Search Time (ns):" + linearSearchTime);
        System.out.println("Linear Search Block Access Count: " + disk.getAccessCount());
        index.resetAccessCount();
        System.out.println("----------Ending experiment 3----------\n\n");
    }

    public static void runExperiment4() {
        System.out.println("----------Running experiment 4----------");

        context.startTimer();
        List<Record> recordsDisk = disk.getRecordsByNumVotes(30000, 50000);
        context.endTimer();
        long linearSearchTime = context.getElapsedTime(TimeUnit.NANOSECONDS);

        context.startTimer();
        List<Record> recordsIndex = index.getRecordsByNumVotes(30000, 50000);
        context.endTimer();
        long indexSearchTime = context.getElapsedTime(TimeUnit.NANOSECONDS);

        double rating = 0;
        for (Record r : recordsIndex) {
            rating += r.getAverageRating();
        }
        double avgRating = rating / recordsIndex.size();
        avgRating = Double.parseDouble(String.format("%.2f", avgRating));

        System.out.println("Index Node Access Count: " + BPlusTree.indexNodeAccess);
        System.out.println("Data Block Access Count: " + BPlusTree.dataBlockAccess);
        System.out.println("Average Rating: " + avgRating);
        System.out.println("Index Search Time (ns): " + indexSearchTime);
        System.out.println("Linear Search Time (ns): " + linearSearchTime);
        System.out.println("Linear Search Block Access Count: " + disk.getAccessCount());

        System.out.println("----------Ending experiment 4----------\n\n");
    }

    public static void runExperiment5() {
        System.out.println("----------Running experiment 5----------");
        context.startTimer();
        try {
            //index.deleteRecord(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        context.endTimer();
        long indexDeleteTime = context.getElapsedTime(TimeUnit.NANOSECONDS);
        System.out.println(index.getRecordsByNumVotes(1000).size());
        System.out.println("Index Delete Time (ns): " + indexDeleteTime);

        /*context.startTimer();
        try {
            context.startTimer();
            disk.deleteRecordsByNumVotes(1000);
            context.endTimer();
            long linearDeleteTime = context.getElapsedTime(TimeUnit.NANOSECONDS);

            System.out.println("Linear Delete Time (ns): " + linearDeleteTime);
            System.out.println("Linear Delete Block Access Count: " + disk.getAccessCount());
            System.out.println("Number of Records after Delete: " + disk.getNumberOfRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        System.out.println("----------Ending experiment 5----------\n\n");
    }
}