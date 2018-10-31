package com.everwhimsical.parser.exceptions;

/**
 * The base class for all exceptions thrown by Entity Parser.
 */
public class ParserException extends RuntimeException {

    /**
     * Exception to be thrown with throwable
     *
     * @param throwable Throwable object.
     */
    public ParserException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Exception to be thrown with message
     *
     * @param message Descriptive message about the error
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Exception to be thrown with message and throwable
     *
     * @param message   Descriptive message about the error
     * @param throwable Throwable object.
     */
    public ParserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
