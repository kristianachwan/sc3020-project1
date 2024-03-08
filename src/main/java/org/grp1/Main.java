package org.grp1;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int MB = 1 << 20;
    private static final int DISK_SIZE = 100 * MB;
    private static final int BLOCK_SIZE = 200;
    private static final String DATA_FILE_PATH = "data.tsv";
    private static final int RECORD_SIZE = 17;
    private static Disk disk;

    private static BPlusTree index;

    public static void main(String[] args) {
        System.out.println("Setting up the disk...");
        disk = new Disk(DISK_SIZE, BLOCK_SIZE, RECORD_SIZE, DATA_FILE_PATH);

        index = new BPlusTree(3);
        runExperiment1();
        runExperiment2();
        runExperiment3();
        runExperiment4();
        runExperiment5();
    }

    public static void runExperiment1() {
        System.out.println("Running experiment 1");
        disk.printDiskInformation();
        System.out.println("Ending experiment 1");
    }

    public static void runExperiment2() {
        System.out.println("Running experiment 2");


        int cnt = 0;
        for (int bnum = 0; bnum < disk.getNumberOfBlocks(); bnum++) {

            Block block = disk.getBlock(bnum);
            for (int i = 0; i < block.getNumberOfRecords(); i++) {
                index.insertRecord(block.getRecord(i));
                cnt++;
            }
        }

        System.out.println("Ending experiment 2");
    }

    public static void runExperiment3() {
        System.out.println("Running experiment 3");
        System.out.println("Ending experiment 3");
    }

    public static void runExperiment4() {
        System.out.println("Running experiment 4");
        System.out.println("Ending experiment 4");
    }

    public static void runExperiment5() {
        System.out.println("Running experiment 5");
        System.out.println("Ending experiment 5");
    }
}