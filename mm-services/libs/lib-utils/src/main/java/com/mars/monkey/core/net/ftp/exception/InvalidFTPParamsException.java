package com.mars.monkey.core.net.ftp.exception;

/**
 * Created on 2018/6/13.
 *
 * @author YouFeng.Zhu
 */
public class InvalidFTPParamsException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -2231046144409246721L;

    /**
     *
     */
    public InvalidFTPParamsException() {
    }

    /**
     * @param message
     */
    public InvalidFTPParamsException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidFTPParamsException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidFTPParamsException(String message, Throwable cause) {
        super(message, cause);
    }

}
