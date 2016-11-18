package com.ll.utils.mail;


import com.ll.utils.mail.model.MailAddress;
import com.ll.utils.mail.model.MailHostConfig;
import com.ll.utils.mail.model.MailItem;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
//            email.setFrom("EC@coretronic.com");
//            email.setSubject("TestMail");
//            //email.setMsg("This is a multipart email ... :-)");
//            email.addTo("leo.kao@coretronic.com");
//            email.addPart("<h1>This is a multipart email ... :-)</h1>", EmailConstants.TEXT_HTML);
            email.send();
        }catch (Exception mex) {
            mex.printStackTrace();
        }
    }


    private Email createEmail(MailHostConfig mailHostConfig, MailItem mailItem) {
        MultiPartEmail email = new MultiPartEmail();
        try {
            email.setHostName(mailHostConfig.getHostAddress());
            email.setSmtpPort(mailHostConfig.getHostPort());
            if (mailHostConfig.isNeedAuth()) {
                email.setAuthenticator(new DefaultAuthenticator(mailHostConfig.getUserName(), mailHostConfig.getUserPassword()));
            }
            email.setSSLOnConnect(mailHostConfig.isNeedSSL());
            email.setFrom(mailItem.from.mailAddress, mailItem.from.mailUser);
            email.setSubject(mailItem.subject);
//            email.setMsg("This is a multipart email ... :-)");
//            email.addTo("leo.kao@coretronic.com");
            for (MailAddress mailAddr : mailItem.to) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.cc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.bcc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            email.addPart("<h1>This is a multipart email ... :-)</h1>", mailItem.contentType);
        } catch (Exception e) {
            e.getStackTrace();
            logger.error(e.getStackTrace().toString());
        }
        return email;
    }
}