package com.mars.monkey.core.net.ftp.exception;

/**
 * Created on 2018/6/13.
 *
 * @author YouFeng.Zhu
 */
public class FTPAuthenticationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 6864463970227476719L;

    /**
     *
     */
    public FTPAuthenticationException() {
    }

    /**
     * @param message
     */
    public FTPAuthenticationException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public FTPAuthenticationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public FTPAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}