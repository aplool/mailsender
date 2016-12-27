package com.aplool.mail.utils;

import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.customize.MyMultiPartEmail;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgent {
    private static final Logger mLogger = LoggerFactory.getLogger(MailAgent.class);
    private static final boolean debugFlag = true;
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

    public boolean sendMail(MailItem mailItem) {
        try {
            Email email = this.createEmail(mailItem);
            String messageId = email.send();
            mLogger.debug("Send Mail Message Id: {} => Result: {}", messageId, true);
            mLogger.debug("Mail Header and Content : \n{}",email.toString());
            return true;
        } catch (Exception e) {
            mLogger.debug("Send Mail Subject: {} => Result: {}", mailItem.subject, false);
            if (debugFlag) {
                e.printStackTrace();
            }
            return false;
        }
    }


    private Email createEmail(MailItem mailItem) throws Exception {
        MyMultiPartEmail email = new MyMultiPartEmail();

        Map<String, String> mailHeaders = buildHeaders();
        email.setMessageId(mailHeaders.get(MAIL_HEADER_MESSAGE_ID));
        email.setHeaders(mailHeaders);
        email.setCharset(EmailConstants.UTF_8);

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

    private Map<String, String> buildHeaders() {

        Map<String, String> mailHeaders = new HashMap<String, String>();
        try {

            Enumeration e = mMailHeaderConfig.getHeaderProperties().propertyNames();

            while (e.hasMoreElements()) {
                String headerKey = (String) e.nextElement();
                String value = this.executor.execute(mMailHeaderConfig.getHeaderProperties().getProperty(headerKey));
                mailHeaders.put(headerKey, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mailHeaders;
    }

    private void initEmailWithMarco(Email email) throws Exception{
        email.setFrom(this.executor.execute("%FROM_EMAIL"),this.executor.execute("%FROM_NAME"));
        email.setSubject(this.executor.execute("%SUBJECT"));
    }
    public void setMacroExecutor(MarcoExecutor executor){ this.executor = executor;}
    public void setMailHeaderConfig(MailHeaderConfig mailHeaderConfig) {
        mMailHeaderConfig = mailHeaderConfig;
    }

    public String loadMessageBodyFromFile(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } finally {
            bufferedReader.close();
        }
    }

    public static List<String> loadMailToFromFile(String filePath) throws IOException {
        List<String> mailToList = new LinkedList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        try {
            String line = bufferedReader.readLine();

            while (line != null) {
                mailToList.add(line);
                line = bufferedReader.readLine();
            }
            return mailToList;
        } finally {
            bufferedReader.close();
        }
    }
}
