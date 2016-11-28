package com.aplool.mail.utils;

import com.aplool.mail.model.MailHostConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by leokao on 11/22/2016.
 */
public class SmtpServer {
    private static final Logger mLogger = LoggerFactory.getLogger(SmtpServer.class);

    private MailHostConfig mMailHostConfig = new MailHostConfig();
    private int mTimeout = 1000;

    public SmtpServer(String serverAddress) {
        mMailHostConfig.setHostAddress(serverAddress);
        mMailHostConfig.setHostPort(25);
        mTimeout = 1000;
    }

    public SmtpServer(MailHostConfig mailHostConfig) {
        mMailHostConfig = mailHostConfig;
        mTimeout = 1000;
    }

    public boolean testReachable() {
        boolean result = false;
        try {
            InetAddress inetAddress = InetAddress.getByName(mMailHostConfig.getHostAddress());
            return result = inetAddress.isReachable(mTimeout);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkSmtp(boolean debugFlag) {
        return checkSmtpByIP(this.getServerIP(), debugFlag);
    }

    public boolean checkSmtpByIP(String serverIP, boolean debugFlag) {
        boolean isConnected = false;
        Transport smtpTransport = null;
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", serverIP);
        props.setProperty("mail.debug", Boolean.toString(debugFlag));
        Session smtpSession = Session.getInstance(props);
        try {
            smtpTransport = smtpSession.getTransport();
            if (mMailHostConfig.isNeedAuth()) {
                smtpTransport.connect(mMailHostConfig.getUserName(), mMailHostConfig.getUserPassword());
                mLogger.info("SMTP Need Auth");
            } else {
                smtpTransport.connect();
                mLogger.info("SMTP NOT Need Auth");
            }
            isConnected = smtpTransport.isConnected();
            smtpTransport.close();
            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (smtpTransport != null) {
                try {
                    smtpTransport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getServerAddress() {
        return mMailHostConfig.getHostAddress();
    }

    public void setServerAddress(String serverAddress) {
        mMailHostConfig.setHostAddress(serverAddress);
    }

    public String getServerIP() {
        String serverIP = null;
        try {
            InetAddress inetAddress = InetAddress.getByName(mMailHostConfig.getHostAddress());
            serverIP = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return serverIP;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public void setTimeout(int mTimeout) {
        this.mTimeout = mTimeout;
    }

    public int getServerPort() {
        return mMailHostConfig.getHostPort();
    }

    public void setServerPort(int hostPort) {
        mMailHostConfig.setHostPort(hostPort);
    }

    public MailHostConfig getMailHostConfig() {
        return mMailHostConfig;
    }

    public void setMailHostConfig(MailHostConfig mailHostConfig) {
        mMailHostConfig = mailHostConfig;
    }
}
