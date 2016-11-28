package com.aplool.mail.utils;

import com.aplool.mail.customize.MyMultiPartEmail;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgent {
    private static final Logger mLogger = LoggerFactory.getLogger(MailAgent.class);
    private MailHostConfig mMailHostConfig = null;
    public static final String MAIL_HEADER_MESSAGE_ID = "Message-ID";
    public static final String MAIL_HEADER_SEND_DATE = "Date";
    public static final String MAIL_HEADER_RECEIVED = "Received";
    public static final String MAIL_HEADER_X_MAILER = "X-Mailer";
    public static final String MAIL_HEADER_X_PRIORITY = "X-Priority";
    public static final String MAIL_HEADER_X_MSMAIL_PRIORITY = "X-MSMail-Priority";
    public static final String MAIL_HEADER_ENCODE = "charset=UTF-8";


    public MailAgent(MailHostConfig mailHostConfig) {
        mMailHostConfig = mailHostConfig;
    }

    public boolean sendMail(MailItem mailItem) {
        boolean sendSuccess = false;
        Email email = this.createEmail(mailItem);

        try {
            email.send();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private Email createEmail(MailItem mailItem) {
        MyMultiPartEmail email = new MyMultiPartEmail();

        Map<String, String> mailHeaders = buildHeaders();
        email.setMessageId(mailHeaders.get(this.MAIL_HEADER_MESSAGE_ID));
        mLogger.info(this.MAIL_HEADER_MESSAGE_ID + ":" + email.getMessageId());

        email.setHeaders(mailHeaders);
        email.setCharset(EmailConstants.UTF_8);
        try {
            email.setHostName(mMailHostConfig.getHostAddress());
            email.setSmtpPort(mMailHostConfig.getHostPort());
            if (mMailHostConfig.isNeedAuth()) {
                email.setAuthenticator(new DefaultAuthenticator(mMailHostConfig.getUserName(), mMailHostConfig.getUserPassword()));
            }
            email.setSSLOnConnect(mMailHostConfig.isNeedSSL());
            email.setFrom(mailItem.from.mailAddress, mailItem.from.mailUser);
            email.addReplyTo(mailItem.from.mailAddress, mailItem.from.mailUser);
            for (MailAddress mailAddr : mailItem.to) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.cc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.bcc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            email.setSubject(mailItem.subject);
            email.addPart(mailItem.message, mailItem.contentType + ";" + this.MAIL_HEADER_ENCODE);
        } catch (Exception e) {
            e.getStackTrace();
            mLogger.error("Error", e);
        }
        return email;
    }

    private Map<String, String> buildHeaders() {
        Map<String,String> mailHeaders = new HashMap<String, String>();
        String sendDate = new Date().toString();
        mailHeaders.put(this.MAIL_HEADER_RECEIVED, "from " + this.getFromIP() + " by " + this.getProxyIP() + "; " + sendDate);
        mailHeaders.put(this.MAIL_HEADER_SEND_DATE, sendDate);
        mailHeaders.put(this.MAIL_HEADER_MESSAGE_ID, this.genMessageId());
        //mailHeaders.put(this.MAIL_HEADER_X_MAILER, "sendMail");
        mailHeaders.put(this.MAIL_HEADER_X_PRIORITY, "3");
        mailHeaders.put(this.MAIL_HEADER_X_MSMAIL_PRIORITY, "3");
        return mailHeaders;
    }

    private String getProxyIP() {
        return "127.0.0.1";
    }

    private String getFromIP() {
        return "127.0.0.1";
    }

    private String genMessageId() {
        return "<" + this.getRandomUser() + this.getRandomDomain() + ">";
    }

    private String getRandomUser() {
        return "longtai"+ Math.random();
    }

    private String getRandomDomain() {
        return "coretronic.com";
    }
}