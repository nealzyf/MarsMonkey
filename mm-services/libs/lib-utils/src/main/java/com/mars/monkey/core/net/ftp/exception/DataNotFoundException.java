package com.mars.monkey.core.net.ftp.exception;

/**
 * Created on 2018/6/13.
 *
 * @author YouFeng.Zhu
 */
public class DataNotFoundException extends Exception {

    private static final long serialVersionUID = 1268973588723740635L;

    public DataNotFoundException() {
    }

    public DataNotFoundException(String specificErrorMessage) {
        //this.errorMessage = specificErrorMessage;
        super(specificErrorMessage);
    }
}