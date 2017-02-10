package com.aplool.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;

/**
 * Created by longtai on 2017/2/9.
 */
public class BulkMimeMessage extends MimeMessage {
    static Logger log = LoggerFactory.getLogger(BulkMimeMessage.class);
    Multipart multiPart = new MimeMultipart("alternative");

    public BulkMimeMessage(Session session) {
        super(session);
    }

    public void setFrom(String email, String name) throws RuntimeException{
        try {
            this.setFrom(new InternetAddress(email,name));
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void setTo(String email, String name) throws RuntimeException{
        try {
            this.setRecipient(Message.RecipientType.TO, new InternetAddress(email,name));
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
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
    public void updateSubject(String subject) throws MessagingException{
        setSubject(subject, "utf-8");
    }
}
