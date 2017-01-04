package com.aplool.mail.utils;

import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.customize.MyMultiPartEmail;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgent {
    private final Logger log = LoggerFactory.getLogger(MailAgent.class);
    private MailHostConfig mMailHostConfig = null;
    private MailHeaderConfig mMailHeaderConfig = null;
    private MarcoExecutor executor=null;
    public static final String MAIL_HEADER_MESSAGE_ID = "Message-ID";
    public static final String MAIL_HEADER_FROM = "From";
    public static final String MAIL_HEADER_SEND_DATE = "Date";
    public static final String MAIL_HEADER_RECEIVED = "Received";
    public static final String MAIL_HEADER_X_MAILER = "X-Mailer";
    public static final String MAIL_HEADER_X_PRIORITY = "X-Priority";
    public static final String MAIL_HEADER_X_MSMAIL_PRIORITY = "X-MSMail-Priority";
    public static final String MAIL_HEADER_ENCODE = "charset=UTF-8";


    public MailAgent(MailHostConfig mailHostConfig) {
        mMailHostConfig = mailHostConfig;
    }

    public void sendBulk(String mailListFilename, String messageBody){
        try (Stream<String> stream = Files.lines(Paths.get(mailListFilename))) {
            stream.forEach((email)->{
                MailItem mailItem = new MailItem();
                MailAddress toEmail = new MailAddress(email, email);
                mailItem.addTo(toEmail);
                mailItem.contentType = EmailConstants.TEXT_HTML;
                mailItem.message = messageBody;
                boolean result = send(mailItem);
                log.info("[{}] [{}] Mail to {}",String.valueOf(result),this.mMailHostConfig.getHostAddress(),email);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean send(MailItem mailItem) {
        boolean result = false;
        try {
            Email email = this.createEmail(mailItem);
            String messageId = email.send();
            log.debug("Send Mail Message Id: {} => Result: {}", messageId, true);
            log.debug("Mail Header and Content : \n{}",email.toString());
            result = true;
        } catch (EmailException e) {
            log.debug("Mail Fail with  : {}", e.getMessage());
        } catch (Exception e) {
            log.debug("Mail Fail with  : {}", e.getMessage());
        }
        return result;
    }


    private Email createEmail(MailItem mailItem) throws Exception {
        MyMultiPartEmail email = new MyMultiPartEmail();
        email.setCharset(EmailConstants.UTF_8);

        //initEmailWithHeaders(email);
        initEmailWithMarco(email);
        try {
            email.setHostName(mMailHostConfig.getHostAddress());
            email.setSmtpPort(mMailHostConfig.getHostPort());
            email.setAuthenticator(mMailHostConfig.getAuthenticator());
            email.setSSLOnConnect(mMailHostConfig.isNeedSSL());

            for (MailAddress mailAddr : mailItem.to) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.cc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.bcc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            email.addPart(mailItem.message, mailItem.contentType + ";" + this.MAIL_HEADER_ENCODE);
        } catch (Exception e) {
            throw e;
        }
        return email;
    }

    private void initEmailWithHeaders(MyMultiPartEmail email) {

        Map<String, String> mailHeaders = new HashMap<String, String>();
        try {

            Enumeration e = mMailHeaderConfig.getHeaderProperties().propertyNames();

            while (e.hasMoreElements()) {
                String headerKey = (String) e.nextElement();
                String value = this.executor.execute(mMailHeaderConfig.getHeaderProperties().getProperty(headerKey));
                mailHeaders.put(headerKey, value);
            }
            email.setMessageId(mailHeaders.get(MAIL_HEADER_MESSAGE_ID));
            email.setHeaders(mailHeaders);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEmailWithMarco(Email email) throws Exception{
        email.setFrom(this.executor.execute("%FROM_EMAIL"),this.executor.execute("%FROM_NAME"));
        email.setSubject(this.executor.execute("%SUBJECT"));
    }

    public void setMacroExecutor(MarcoExecutor executor){ this.executor = executor;}
    public void setMailHeaderConfig(MailHeaderConfig mailHeaderConfig) {
        mMailHeaderConfig = mailHeaderConfig;
    }



}
