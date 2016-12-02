package com.aplool.mail.utils;

import com.aplool.macro.MarcoBuilder;
import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.customize.MyMultiPartEmail;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgent {
    private static final Logger mLogger = LoggerFactory.getLogger(MailAgent.class);
    private static final boolean debugFlag = false;
    private MailHostConfig mMailHostConfig = null;
    private MailHeaderConfig mMailHeaderConfig = null;
    private Path mMarcoPath = null;
    public static final String MAIL_HEADER_MESSAGE_ID = "Message-ID";
    public static final String MAIL_HEADER_SEND_DATE = "Date";
    public static final String MAIL_HEADER_RECEIVED = "Received";
    public static final String MAIL_HEADER_X_MAILER = "X-Mailer";
    public static final String MAIL_HEADER_X_PRIORITY = "X-Priority";
    public static final String MAIL_HEADER_X_MSMAIL_PRIORITY = "X-MSMail-Priority";
    public static final String MAIL_HEADER_ENCODE = "charset=UTF-8";


    public MailAgent(MailHostConfig mailHostConfig) {
        mMailHostConfig = mailHostConfig;
//        try {
////            this.mMarcoPath = Paths.get(this.getClass().getResource("/marco").toURI());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }

    public boolean sendMail(MailItem mailItem) {
        try {
            Email email = this.createEmail(mailItem);
            String messageId = email.send();
            mLogger.info("Send Mail Message Id: {} => Result: {}", messageId, true);
            mailItem.to.forEach(mailTo -> mLogger.info("To User : {}  , Mail : {}", mailTo.mailUser, mailTo.mailAddress));
            mailItem.cc.forEach(mailTo -> mLogger.info("CC User : {}  , Mail : {}", mailTo.mailUser, mailTo.mailAddress));
            mailItem.bcc.forEach(mailTo -> mLogger.info("BCC User : {}  , Mail : {}", mailTo.mailUser, mailTo.mailAddress));
            return true;
        } catch (Exception e) {
            mLogger.info("Send Mail Subject: {} => Result: {}", mailItem.subject, false);
            if (debugFlag) {
                e.printStackTrace();
            }
            return false;
        }
    }


    private Email createEmail(MailItem mailItem) throws Exception {
        MyMultiPartEmail email = new MyMultiPartEmail();

        Map<String, String> mailHeaders = buildHeaders();
        email.setMessageId(mailHeaders.get(this.MAIL_HEADER_MESSAGE_ID));
//        mLogger.info(this.MAIL_HEADER_MESSAGE_ID + ":" + email.getMessageId());

        email.setHeaders(mailHeaders);
        email.setCharset(EmailConstants.UTF_8);
        try {
            email.setHostName(mMailHostConfig.getHostAddress());
            email.setSmtpPort(mMailHostConfig.getHostPort());
            if (mMailHostConfig.isNeedAuth()) {
                email.setAuthenticator(new DefaultAuthenticator(mMailHostConfig.getUserName(), mMailHostConfig.getUserPassword()));
            }
            email.setSSLOnConnect(mMailHostConfig.isNeedSSL());
//            email.setFrom(mailItem.from.mailAddress, mailItem.from.mailUser);
//            email.addReplyTo(mailItem.from.mailAddress, mailItem.from.mailUser);
            for (MailAddress mailAddr : mailItem.to) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.cc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            for (MailAddress mailAddr : mailItem.bcc) {
                email.addTo(mailAddr.mailAddress, mailAddr.mailUser);
            }
            mailItem.subject = mailHeaders.get("Subject");
            email.setSubject(mailItem.subject);
            email.addPart(mailItem.message, mailItem.contentType + ";" + this.MAIL_HEADER_ENCODE);
        } catch (Exception e) {
            throw e;
        }
        return email;
    }

    private Map<String, String> buildHeaders() {
        MarcoExecutor executor = null;
        Map<String, String> mailHeaders = new HashMap<String, String>();
        try {
            executor = new MarcoExecutor();
            MarcoBuilder.build(executor, mMarcoPath);

            Enumeration e = mMailHeaderConfig.getHeaderProperties().propertyNames();

            while (e.hasMoreElements()) {
                String headerKey = (String) e.nextElement();
                mailHeaders.put(headerKey, executor.executeMarco(mMailHeaderConfig.getHeaderProperties().getProperty(headerKey)));
                mLogger.debug("{}:{}=>{}", new String[]{headerKey, mMailHeaderConfig.getHeaderProperties().getProperty(headerKey), executor.executeMarco(mMailHeaderConfig.getHeaderProperties().getProperty(headerKey))});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String sendDate = new Date().toString();
//        mailHeaders.put(this.MAIL_HEADER_RECEIVED, "from " + this.getFromIP() + " by " + this.getProxyIP() + "; " + sendDate);
//        mailHeaders.put(this.MAIL_HEADER_SEND_DATE, sendDate);
//        mailHeaders.put(this.MAIL_HEADER_MESSAGE_ID, this.genMessageId());
//        mailHeaders.put(this.MAIL_HEADER_X_MAILER, "sendMail");
//        mailHeaders.put(this.MAIL_HEADER_X_PRIORITY, "3");
//        mailHeaders.put(this.MAIL_HEADER_X_MSMAIL_PRIORITY, "3");
        return mailHeaders;
    }

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

    public Path getMarcoPath() {
        return mMarcoPath;
    }

    public void setMarcoPath(String marcoPath) {
        mMarcoPath = Paths.get(marcoPath);
    }
}
