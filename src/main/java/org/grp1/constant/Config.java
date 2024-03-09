package org.grp1.constant;

public class Config {

    public static final int MB = 1 << 20;
    public static final int DISK_SIZE = 100 * MB;
    public static final int BLOCK_SIZE = 200;
    public static final String DATA_FILE_PATH = "data.tsv";
    public static final int RECORD_SIZE = 17;
    public static final int NUMBER_OF_RECORDS_IN_BLOCK = BLOCK_SIZE / RECORD_SIZE;
    public static final int POINTER_SIZE = 8;
    public static final int KEY_SIZE = 4;
    public static final int N = (BLOCK_SIZE - POINTER_SIZE) / (KEY_SIZE + POINTER_SIZE);
    public static final int TREE_NODE_SIZE = 13;
}
