package org.grp1.exception;

public class LeafFullException extends Exception {


    public LeafFullException() {
        super();
    }

    /**
     * Constructor to instantiate the exception by message
     *
     * @param message Message of the error
     */
    public LeafFullException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate the exception by message and cause
     *
     * @param message Message of the error
     * @param cause   Cause of the error in the form of Throwable
     */
    public LeafFullException(String message, Throwable cause) {
        super(message, cause);
    }

}
    