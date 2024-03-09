package org.grp1;

import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int MB = 1 << 20;
    private static final int DISK_SIZE = 100 * MB;
    private static final int BLOCK_SIZE = 200;
    private static final String DATA_FILE_PATH = "data.tsv";
    private static final int RECORD_SIZE = 17;
    private static Disk disk;
    private static BPlusTree bPlusTree;

    public static void main(String[] args) {
        System.out.println("Setting up the disk...");
        disk = new Disk(DISK_SIZE, BLOCK_SIZE, RECORD_SIZE, DATA_FILE_PATH);
        runExperiment1();
        runExperiment2();
        runExperiment3();
        runExperiment4();
        runExperiment5();
    }

    public static void runExperiment1() {
        System.out.println("----------Running experiment 1----------");
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
        long startTime = System.nanoTime();
        ArrayList<Record> Votes500 = bPlusTree.getRecordsByNumVotes(500);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //in milliseconds.
//        System.out.println(Votes500);
        System.out.println("2. The number of data blocks the process accesses: ");
        System.out.println("3. The average of 'averageRatings' of the records that are returned: ");
        System.out.println("4. The running time of the retrieval process: " + duration + " nanoseconds");
        // Run a linear search on the leaf node level.
        System.out.println("5. the number of data blocks that would be accessed by a brute-force linear scan method: ");
        System.out.println("----------Ending experiment 3----------\n\n");
    }

    public static void runExperiment4() {
        System.out.println("----------Running experiment 4----------");
        System.out.println("----------Ending experiment 4----------\n\n");
    }

    public static void runExperiment5() {
        System.out.println("----------Running experiment 5----------");
        System.out.println("----------Ending experiment 5----------\n\n");
    }
}