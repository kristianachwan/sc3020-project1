package org.grp1;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.grp1.constant.Config;
import org.grp1.exception.LeafFullException;
import org.grp1.model.Record;
import org.grp1.storage.Disk;
import org.grp1.util.Context;
import org.grp1.util.RecordParser;
import org.grp1.util.TSVReader;
import org.grp1.index.BPlusTree;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Disk disk;
    private static BPlusTree index;
    private static Context context;

    public static void main(String[] args) {
        System.out.println("Setting up the disk...");
        disk = new Disk(Config.DISK_SIZE, Config.BLOCK_SIZE, Config.RECORD_SIZE);
        index = new BPlusTree(Config.TREE_NODE_SIZE);
        context = new Context();
        runExperiment1();
        runExperiment2();
        runExperiment3();
        runExperiment4();
        runExperiment5();
    }

    public static void runExperiment1() {
        System.out.println("Running experiment 1");
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
        // Instantiate new B+ tree
        bPlusTree = new BPlusTree(13);
        // Populate the B+ tree
        for (int i = 0; i < disk.getNumberOfBlocks(); i++) {
            Block block = disk.getBlock(i);
            for (Record record : block.getRecords()) {
                bPlusTree.insertRecord(record);
            }
        }
        System.out.println("The parameter n of the B+ tree: " + bPlusTree.getMaxKeyNumber());
        System.out.println("The number of nodes of the B+ tree: " + bPlusTree.getNodeCount());
        System.out.println("The number of levels of the B+ tree: " + bPlusTree.getNumberOfLevels());
        bPlusTree.printRootKeys();

        System.out.println("----------Ending experiment 2----------\n\n");
    }

    public static void runExperiment3() {
        System.out.println("----------Running experiment 3----------");
        ArrayList<Record> Votes500 = bPlusTree.getRecordsByNumVotes(500);
        System.out.println(Votes500);
        System.out.println("The number of index nodes the process accesses: ");
        System.out.println("The number of data blocks the process accesses: ");
        System.out.println("The average of 'averageRatings' of the records that are returned: ");
        System.out.println("The running time of the retrieval process: ");
        System.out.println("the number of data blocks that would be accessed by a brute-force linear scan method: ");
        System.out.println("----------Ending experiment 3----------\n\n");
    }

    public static void runExperiment4() {
        System.out.println("Running experiment 4");

        List<Record> records;

        context.startTimer();
        records = disk.getRecordsByNumVotes(30000, 50000);
        context.endTimer();
        long linearSearchTime = context.getElapsedTime(TimeUnit.NANOSECONDS);

        context.startTimer();
        records = index.getRecordsByNumVotes(30000, 50000);
        context.endTimer();
        long indexSearchTime = context.getElapsedTime(TimeUnit.NANOSECONDS);

        long rating = 0;
        for (Record r : records) {
            rating += r.getAverageRating();
        }
        long avgRating = rating / records.size();

        System.out.println("Index Node Access Count: " + BPlusTree.indexNodeAccess);
        System.out.println("Data Block Access Count: " + BPlusTree.dataBlockAccess);
        System.out.println("Average Rating: " + avgRating);
        System.out.println("Index Search Time (ns): " + indexSearchTime);
        System.out.println("Linear Search Time (ns): " + linearSearchTime);
        System.out.println("Linear Search Block Access Count: " + disk.getAccessCount());

        System.out.println("Ending experiment 4");
    }

    public static void runExperiment5() {
        System.out.println("----------Running experiment 5----------");
        System.out.println("----------Ending experiment 5----------\n\n");
    }
}