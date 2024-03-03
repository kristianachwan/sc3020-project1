package org.grp1;

import java.util.List;

import org.grp1.constant.Config;
import org.grp1.model.Record;
import org.grp1.storage.Disk;
import org.grp1.util.RecordParser;
import org.grp1.util.TSVReader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Disk disk;

    public static void main(String[] args) {
        System.out.println("Setting up the disk...");
        disk = new Disk(Config.DISK_SIZE, Config.BLOCK_SIZE, Config.RECORD_SIZE);
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
        System.out.println("Ending experiment 1");
    }

    public static void runExperiment2() {
        System.out.println("Running experiment 2");
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