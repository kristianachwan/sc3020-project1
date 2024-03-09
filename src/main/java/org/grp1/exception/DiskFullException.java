package org.grp1.exception;

public class DiskFullException extends Exception {

    public DiskFullException() {
        super();
    }

    /**
     * Constructor to instantiate the exception by message
     *
     * @param message Message of the error
     */
    public DiskFullException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate the exception by message and cause
     *
     * @param message Message of the error
     * @param cause   Cause of the error in the form of Throwable
     */
    public DiskFullException(String message, Throwable cause) {
        super(message, cause);
    }

}
    