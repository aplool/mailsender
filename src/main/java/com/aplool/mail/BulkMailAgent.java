package com.aplool.mail;

import com.aplool.macro.MarcoExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by longtai on 2017/2/7.
 */
public class BulkMailAgent {

    Logger log = LoggerFactory.getLogger(BulkMailAgent.class);

    Session mailSession;
    Transport transport;
    MarcoExecutor marcoExecutor;

    public BulkMailAgent(Session session, MarcoExecutor marcoExecutor){
        this.mailSession = session;
        this.marcoExecutor = marcoExecutor;
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
    public void send(List<String> mails, String message) throws RuntimeException{
        BulkMimeMessage mineMessage = new BulkMimeMessage(getSession());
        try {
            mineMessage.addHtml(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
        mails.stream().forEach(toEmail->{
            boolean isSend = false;
            try {
                mineMessage.updateSubject(marcoExecutor.execute("%SUBJECT"));
                mineMessage.setFrom(marcoExecutor.execute("%FROM_EMAIL"),marcoExecutor.execute("%FROM_NAME"));
                mineMessage.setTo(toEmail,toEmail);
                isSend = send(mineMessage);
            } catch (MessagingException e) {
                log.error(e.getMessage(),e.getCause());
            }
            log.info("[{}] send {}, {}", mailSession.getProperty("mail.smtp.host"), toEmail, isSend);
        });

    }
    public boolean send(Message message){
        boolean result = false;
        try {
            if (!transport.isConnected()) {
                transport.connect();
            }
            transport.sendMessage(message, message.getAllRecipients());
            log.debug("[{}] send {}, true", mailSession.getProperty("mail.smtp.host"), message.getAllRecipients()[0].toString());
            result = true;
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(),e.getCause());
            e.printStackTrace();
        } catch (MessagingException e) {
            //log.error(e.getMessage(),e.getCause());
        }
        return result;
    }


    public static Callable<BulkMailAgent> build(String ip, MarcoExecutor marcoExecutor){
        return new BulkMailAgentBuilder(ip, marcoExecutor);
    }
}
