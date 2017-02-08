package com.aplool.mail;

import com.aplool.mail.utils.MailAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by longtai on 2017/2/7.
 */
public class BulkMailAgent {

    Logger log = LoggerFactory.getLogger(BulkMailAgent.class);

    Session mailSession;
    Transport transport;

    public BulkMailAgent(Session session){
        this.mailSession = session;
        try {
            transport = mailSession.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
    public Session getSession(){
        return this.mailSession;
    }
    public void close(){
        if(transport.isConnected()) try {
            transport.close();
        } catch (MessagingException e) {
            log.error(e.getMessage(), e.getCause());
        }
    }
    public void send(List<String> mails){

    }
    public boolean send(Message message){
        boolean result = false;
        try {
            if (!transport.isConnected()) {
                transport.connect();
            }
            transport.sendMessage(message, message.getAllRecipients());
            log.debug("[{}] send {}", mailSession.getProperty("mail.smtp.host"), message.getAllRecipients()[0].toString());
            result = true;
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(),e.getCause());
            e.printStackTrace();
        } catch (MessagingException e) {
            log.error(e.getMessage(),e.getCause());
        }
        return result;
    }


    public static Callable<BulkMailAgent> build(String ip){
        return new BulkMailAgentBuilder(ip);
    }
}
