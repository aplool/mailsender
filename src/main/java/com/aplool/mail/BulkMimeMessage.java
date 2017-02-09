package com.aplool.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by longtai on 2017/2/9.
 */
public class BulkMimeMessage extends MimeMessage {
    static Logger log = LoggerFactory.getLogger(BulkMimeMessage.class);
    Multipart multiPart = new MimeMultipart("alternative");

    public BulkMimeMessage(Session session) {
        super(session);
    }

    public void addHtml(String content) throws MessagingException{
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(content, "text/html; charset=utf-8");
        multiPart.addBodyPart(htmlPart);
        setContent(multiPart);
    }
    public void addText(String content) throws MessagingException{
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(content, "utf-8");
        multiPart.addBodyPart(textPart);
        setContent(multiPart);
    }
    public void updateSubject(String subject){
        try {
            setSubject(subject, "utf-8");
        } catch (MessagingException e) {
            log.error(e.getMessage(), e.getCause());
        }
    }
}
