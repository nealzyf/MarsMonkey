package com.mars.monkey.component.common.exception;

/**
 * Created on 4/28/2019.
 *
 * @author YouFeng Zhu
 */
import java.io.Serializable;

/**
 * Error Definition. <br>
 * Holds information about a classified error to be used in an exception. <br>
 * Each classification holds a unique error code as well as a description for the error.
 *
 */
public final class ClassifiedError implements Serializable {
    private static final long serialVersionUID = 4332345654095422817L;

    private final int errorCode;
    private final String errorDescription;

    ClassifiedError(int errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    /**
     * Returns the error code.
     *
     * @return The error code
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the error description.
     *
     * @return The error description
     */
    public String getErrorDescription() {
        return this.errorDescription;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return errorCode + errorDescription.hashCode();
    }

    /**
     * Compares if the provided <code>ClassifiedError</code> is equal to this object. The comparison is made on the error code and the error description
     * attributes.
     *
     * @param error
     * @return
     */
    public boolean equals(ClassifiedError error) {
        return (error.errorCode == this.errorCode && error.errorDescription.equals(errorDescription));
    }

    /**
     * Returns a string of the format <code>{@link #getErrorCode() errorCode}#{@link #getErrorDescription() errorDescription}</code>
     */
    @Override
    public String toString() {
        return errorCode + "#" + errorDescription;
    }

}
