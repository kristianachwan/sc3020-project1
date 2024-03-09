package org.grp1.model;

import org.grp1.index.NodeChild;

import java.util.ArrayList;
import java.util.List;


public class Bucket implements NodeChild {
    private final List<Address> arr;
    private final int key;

    public Bucket(int key) {
        arr = new ArrayList<>();
        this.key = key;
    }

    public Bucket(int key, List<Address> arr) {
        this.arr = arr;
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void insertAddress(Address newAddress) {
        arr.add(newAddress);
    }

    public List<Address> getAddresses() {
        return this.arr;
    }
}
