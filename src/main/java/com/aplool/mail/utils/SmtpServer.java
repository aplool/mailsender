package com.aplool.mail.utils;

import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.EmailConstants;
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
        try {
            InetAddress inetAddress = InetAddress.getByName(mMailHostConfig.getHostAddress());
            return inetAddress.isReachable(mTimeout);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkSmtp(boolean debugFlag, boolean testSend) {
        boolean testResult = false;
        if (this.testReachable()) {
            testResult = checkSmtpByIP(this.getServerIP(), debugFlag);
            if ((testResult) && (testSend)) {
                testResult = testSendMail();
            }
        }
        return testResult;
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
            } else {
                smtpTransport.connect();
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

    public boolean testSendMail() {
        boolean sendResult = false;
        MailAgent mailAgent = new MailAgent(mMailHostConfig);

        MailItem mailItem = new MailItem();
        MailAddress fromEmail = new MailAddress("admin@mail.com", "Mail Admin");
        mailItem.from = fromEmail;
        MailAddress toEmail = new MailAddress("admino@mail.com", "Mail Admin");
        mailItem.addTo(toEmail);
        mailItem.subject = "TestMail 測試郵件";
        mailItem.contentType = EmailConstants.TEXT_HTML;
        mailItem.message = "This is Test Mail 這是測試郵件";
        sendResult = mailAgent.sendMail(mailItem);
        return sendResult;
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