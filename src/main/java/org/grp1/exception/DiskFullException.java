package org.grp1.exception;

public class DiskFullException extends Exception {

    public DiskFullException() {
        super();
    }

    public DiskFullException(String message) {
        super(message);
    }

    public DiskFullException(String message, Throwable cause) {
        super(message, cause);
    }

}
    