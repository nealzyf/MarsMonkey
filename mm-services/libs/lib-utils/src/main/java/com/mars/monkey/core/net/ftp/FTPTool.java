package com.mars.monkey.core.net.ftp;

import com.mars.monkey.core.net.ftp.exception.FTPAuthenticationException;
import com.mars.monkey.core.net.ftp.exception.InvalidFTPParamsException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018/6/13.
 *
 * @author YouFeng.Zhu
 */
public class FTPTool {

    private final static Logger log = LoggerFactory.getLogger(FTPTool.class);

    private String FTP_SERVER;
    private String FTP_SERVER_PORT;
    private String FTP_SERVER_USER_NAME;
    private String FTP_SERVER_PASSWORD;
    private String FTP_SERVER_REMOTE_PATH;
    private FTPClient ftp;

    public void init(String host, String port, String username, String password) {
        this.FTP_SERVER = host;
        this.FTP_SERVER_PORT = port;
        this.FTP_SERVER_USER_NAME = username;
        this.FTP_SERVER_PASSWORD = password;
        this.FTP_SERVER_REMOTE_PATH = "";
    }

    /**
     * Validates if all the required params to connect to FTP server are not
     * empty
     *
     * @throws InvalidFTPParamsException
     */
    private void validate() throws InvalidFTPParamsException {
        if (StringUtils.isEmpty(FTP_SERVER) || StringUtils.isEmpty(FTP_SERVER_USER_NAME) || StringUtils.isEmpty(FTP_SERVER_PASSWORD)) {
            throw new InvalidFTPParamsException("Invalid FTP Connection Params");
        }
    }

    /**
     * This method connects to the specified FTP Server:Port details in the
     * configuration file
     *
     * @throws InvalidFTPParamsException
     */
    private void connect() throws InvalidFTPParamsException {
        validate();
        ftp = new FTPClient();
        try {
            int reply;
            if (NumberUtils.isNumber(FTP_SERVER_PORT) && Integer.parseInt(FTP_SERVER_PORT) > 0) {
                ftp.connect(FTP_SERVER, Integer.parseInt(FTP_SERVER_PORT));
            } else {
                ftp.connect(FTP_SERVER);
            }
            System.out.println("Connected to " + FTP_SERVER + " on " + ftp.getRemotePort());

            // After connection attempt, you should check the reply code to
            // verify success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    log.error(e.getMessage(), e);
                }
            }
            System.err.println("Could not connect to FTP server.");
            e.printStackTrace();
        }
    }

    /**
     * Performs the login operation for the provided user credentials
     *
     * @throws FTPAuthenticationException
     */
    private void login() throws FTPAuthenticationException {
        try {
            if (StringUtils.isNotEmpty(FTP_SERVER_USER_NAME) && StringUtils.isNotEmpty(FTP_SERVER_PASSWORD)) {
                if (!ftp.login(FTP_SERVER_USER_NAME, FTP_SERVER_PASSWORD)) {
                    ftp.logout();
                    throw new FTPAuthenticationException("Credentials failed to Authenticate on the FTP server");
                }
                System.out.println("Remote system is " + ftp.getSystemType());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Uploads a given file to the connected FTP Server
     *
     * @param binaryTransfer
     * @param localFile
     * @return remote FTP location of the file
     */
    public String uploadFile(boolean binaryTransfer, File localFile) {
        String remote = null;
        boolean uploadComplete = false;
        try {
            connect();
            login();

            if (binaryTransfer)
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.setUseEPSVwithIPv4(false);

            InputStream input = new FileInputStream(localFile);

            remote = FTP_SERVER_REMOTE_PATH + localFile.getName();
            uploadComplete = ftp.storeFile(remote, input);

            input.close();
        } catch (InvalidFTPParamsException e) {
            log.error("Invalid FTP Params to connect");
        } catch (FTPAuthenticationException e) {
            log.error(e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("Not able to load/read the localFile");
        } catch (IOException e) {
            log.error("IOException during FTP upload process");
        } finally {
            if (!uploadComplete)
                remote = null;
        }

        return remote;
    }

    public InputStream downloadFileStream(boolean binaryTransfer, String filename, String location) {
        InputStream inputStream = null;
        try {
            String remote = ((StringUtils.isNotEmpty(location)) ? location : FTP_SERVER_REMOTE_PATH) + filename;
            connect();
            login();

            if (binaryTransfer)
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.setUseEPSVwithIPv4(false);

            inputStream = ftp.retrieveFileStream(remote);
        } catch (InvalidFTPParamsException e) {
            log.error("Invalid FTP Params to connect", e);
        } catch (FTPAuthenticationException e) {
            log.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            log.error("Not able to load/read the localFile", e);
        } catch (IOException e) {
            log.error("IOException during FTP download process", e);

        }

        return inputStream;
    }

    public InputStream downloadFile(boolean binaryTransfer, String filename) {
        return downloadFileStream(binaryTransfer, filename, null);
    }

    public boolean deleteFile(String filename) {
        return deleteFile(filename, null);
    }

    public boolean deleteFile(String filename, String location) {
        String remote = null;
        boolean ret = false;
        try {
            remote = ((StringUtils.isNotEmpty(location)) ? location : FTP_SERVER_REMOTE_PATH) + filename;
            connect();
            login();

            ret = ftp.deleteFile(remote);
        } catch (InvalidFTPParamsException e) {
            log.info("No FTP Params to connect");
        } catch (FTPAuthenticationException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error("IOException during FTP delete process for " + remote);
        }
        return ret;
    }
}
