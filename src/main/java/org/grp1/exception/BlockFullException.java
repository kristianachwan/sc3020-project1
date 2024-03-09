package org.grp1.exception;

public class BlockFullException extends Exception {


    public BlockFullException() {
        super();
    }

    /**
     * Constructor to instantiate the exception by message
     *
     * @param message Message of the error
     */
    public BlockFullException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate the exception by message and cause
     *
     * @param message Message of the error
     * @param cause   Cause of the error in the form of Throwable
     */
    public BlockFullException(String message, Throwable cause) {
        super(message, cause);
    }

}
    