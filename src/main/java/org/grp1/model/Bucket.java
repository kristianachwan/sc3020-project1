package org.grp1.model;

import org.grp1.index.NodeChild;

import java.util.ArrayList;
import java.util.List;


public class Bucket implements NodeChild {
    /**
     * The array of address inside of this bucket
     */
    private final List<Address> arr;
    /**
     * The key corresponding to this bucket
     */
    private final int key;

    /**
     * The constructor to instantiate the bucket object
     *
     * @param key The key of the bucket
     */
    public Bucket(int key) {
        arr = new ArrayList<>();
        this.key = key;
    }

    /**
     * The constructor to instantiate the bucket object
     *
     * @param key The key of the bucket
     * @param arr The list of address
     */
    public Bucket(int key, List<Address> arr) {
        this.arr = arr;
        this.key = key;
    }

    /**
     * The function to return the key of this bucket
     *
     * @return The key of the bucket
     */
    public int getKey() {
        return this.key;
    }

    /**
     * The function to insert the address inside bucket
     *
     * @param newAddress The address to be inserted inside of this bucket
     */

    public void insertAddress(Address newAddress) {
        arr.add(newAddress);
    }

    /**
     * The function to return the addressed inside of this bucket
     *
     * @return The list of addresses of the bucket
     */
    public List<Address> getAddresses() {
        return this.arr;
    }
}
