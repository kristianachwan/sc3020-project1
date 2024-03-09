package org.grp1.exception;

public class InvalidIndexException extends Exception {


    public InvalidIndexException() {
        super();
    }

    /**
     * Constructor to instantiate the exception by message
     *
     * @param message Message of the error
     */
    public InvalidIndexException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate the exception by message and cause
     *
     * @param message Message of the error
     * @param cause   Cause of the error in the form of Throwable
     */
    public InvalidIndexException(String message, Throwable cause) {
        super(message, cause);
    }

}
    