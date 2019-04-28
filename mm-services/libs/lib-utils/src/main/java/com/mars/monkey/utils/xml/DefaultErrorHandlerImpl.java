package com.mars.monkey.utils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This default implemententation of the {@link ErrorHandler} does nothing. The
 * error handler is used by the XML parser to report any errors that have
 * ocurred during parsing. <br>
 * All errors and warnings will simply be logged.
 */
public class DefaultErrorHandlerImpl implements ErrorHandler {
    /**
     * logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultErrorHandlerImpl.class);

    /**
     * This flag is raised for a errors or a fatal error.
     */
    private boolean errors = false;

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException exception) throws SAXException {
        logError("Warning-", exception);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException exception) throws SAXException {
        this.errors = true;
        logError("Error-", exception);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        this.errors = true;
        logError("Fatal-", exception);
    }

    /**
     * The errors flag is raised if this error handler has received either a
     * error or a fatal error.
     *
     * @return true if errors have ocurred, false otherwise
     */
    public boolean hadErrors() {
        return this.errors;
    }

    /**
     * Prints the received error.
     *
     * @param ex
     *         The received error
     */
    private void logError(String message, SAXParseException ex) {
        if (!LOGGER.isDebugEnabled()) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(message);
        sb.append("line [");
        sb.append(ex.getLineNumber());
        sb.append("] column [");
        sb.append(ex.getColumnNumber());
        sb.append("] : ");
        sb.append(ex.getMessage());
        sb.append("\n");

        LOGGER.debug(sb.toString());
    }
}