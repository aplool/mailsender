package com.aplool.mail.utils;

import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgent {
    private static final Logger logger = LoggerFactory.getLogger(MailAgent.class);
    private MailHostConfig mailHostConfig = null;

    public MailAgent(MailHostConfig mailHostConfig) {
        this.mailHostConfig = mailHostConfig;
    }

    public void sendMail(MailItem mailItem) {
        Email email = createEmail(mailHostConfig, mailItem);

        try {
            email.send();
        }catch (Exception mex) {
            mex.printStackTrace();
        }
    }


    private Email createEmail(MailHostConfig mailHostConfig, MailItem mailItem) {
        String messageId = "longtai" + Math.random() + "@coretronic.com";
        String sendDate = new Date().toString();

        MultiPartEmail email = new MultiPartEmail();
//        email.setMessageId(messageId);
//        logger.info("Message-ID:" + email.getMessageId());

        Map<String,String> mailHeaders = new HashMap<String, String>();
        logger.info("Send Date:" + sendDate);
        mailHeaders.put("Received", "from 192.168.1.1 by 192.168.1.2;" + sendDate);
        mailHeaders.put("Date" , sendDate);

        email.setHeaders(mailHeaders);
        email.setCharset(EmailConstants.UTF_8);
        try {
            email.setHostName(mailHostConfig.getHostAddress());
            email.setSmtpPort(mailHostConfig.getHostPort());
            if (mailHostConfig.isNeedAuth()) {
                email.setAuthenticator(new DefaultAuthenticator(mailHostConfig.getUserName(), mailHostConfig.getUserPassword()));
            }
            email.setSSLOnConnect(mailHostConfig.isNeedSSL());
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
            email.addPart(mailItem.message, mailItem.contentType + ";charset=UTF-8");
        } catch (Exception e) {
            e.getStackTrace();
            logger.error(e.getStackTrace().toString());
        }
        return email;
    }
}