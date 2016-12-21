package com.aplool.mail.model;

import org.apache.commons.mail.DefaultAuthenticator;

import javax.mail.Authenticator;
import java.io.*;
import java.util.Properties;

/**
 * Created by leokao on 11/18/2016.
 */
public class MailHostConfig {
    private Properties mHostProperties = new Properties();
//    private String hostAddress = "localhost";
//    private int hostPort = 25;
//    private boolean needAuth = false;
//    private String userName = "user";
//    private String userPassword = "pass";
//    private boolean needSSL = false;

    public MailHostConfig() {
        mHostProperties = new Properties();
        mHostProperties.setProperty("hostAddress", "localhost");
        mHostProperties.setProperty("hostPort", "25");
        mHostProperties.setProperty("needAuth", "false");
        mHostProperties.setProperty("userName", "user");
        mHostProperties.setProperty("userPassword", "pass");
        mHostProperties.setProperty("needSSL", "false");
    }

    public MailHostConfig(String fileName) {
        InputStream fileInput = null;
        try {
            fileInput = new FileInputStream(fileName);
            mHostProperties.load(fileInput);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean saveToFile(String fileName) {
        OutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(fileName);
            mHostProperties.store(fileOutput,"");
            return true;
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (fileOutput != null) {
                try {
                    fileOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public String getHostAddress() {
        return mHostProperties.getProperty("hostAddress");
    }

    public void setHostAddress(String hostAddress) {
        mHostProperties.setProperty("hostAddress", hostAddress);
    }

    public int getHostPort() {
        return Integer.parseInt(mHostProperties.getProperty("hostPort"));
    }

    public void setHostPort(int hostPort) {
        mHostProperties.setProperty("hostPort", Integer.toString(hostPort));
    }

    public boolean isNeedAuth() {
        return Boolean.parseBoolean(mHostProperties.getProperty("needAuth"));
    }

    public void setNeedAuth(boolean needAuth) {
        mHostProperties.setProperty("needAuth", Boolean.toString(needAuth));
    }

    public String getUserName() {
        return mHostProperties.getProperty("userName");
    }

    public void setUserName(String userName) {
        mHostProperties.setProperty("userName", userName);
    }

    public String getUserPassword() {
        return mHostProperties.getProperty("userPassword");
    }

    public void setUserPassword(String userPassword) {
        mHostProperties.setProperty("userPassword", userPassword);
    }

    public boolean isNeedSSL() {
        return Boolean.parseBoolean(mHostProperties.getProperty("needSSL"));
    }

    public void setNeedSSL(boolean needSSL) {
        mHostProperties.setProperty("needSSL",Boolean.toString(needSSL));
    }

    public Authenticator getAuthenticator(){
        if(!this.isNeedAuth()) return null;
        return new DefaultAuthenticator(this.getUserName(), this.getUserPassword());

    }
}
