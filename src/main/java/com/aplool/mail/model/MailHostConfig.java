package com.aplool.mail.model;

/**
 * Created by leokao on 11/18/2016.
 */
public class MailHostConfig {
    private String hostAddress = "localhost";
    private int hostPort = 25;
    private boolean needAuth = false;
    private String userName = "user";
    private String userPassword = "pass";
    private boolean needSSL = false;

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public boolean isNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isNeedSSL() {
        return needSSL;
    }

    public void setNeedSSL(boolean needSSL) {
        this.needSSL = needSSL;
    }
}
