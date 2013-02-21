/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.scanner;

/**
 *
 * @author Daniel
 */
public class ScannerException extends RuntimeException {

    public ScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScannerException(String message) {
        super(message);
    }
}
