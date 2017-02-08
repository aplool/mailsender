package com.aplool.mail;

import com.aplool.mail.utils.MailAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
        return new MailCallable(ip);
    }

    private static class MailCallable implements Callable<BulkMailAgent>{
        Logger log = LoggerFactory.getLogger(BulkMailAgent.class);
        private String ip;
        public MailCallable(String ip){
            this.ip = ip;
        }
        @Override
        public BulkMailAgent call() throws Exception {
            BulkMailAgent result = null;
            log.debug("[{}] building.",ip);
            if (!isReachable(ip)) {
                log.info("[{}] is not Reachable : {}",ip,false);
                return result;
            }
            result = buildBulkMailAgent();
            if(sendTestMail(result)){
                log.info("[{}] is mail relay : {}",ip, true);
            } else {
                log.info("[{}] is mot mail relay : {}",ip, false);
                result.close();
            }
            return result;
        }

        boolean isReachable(String ip){
            boolean result = false;
            if("".equals(ip.trim())) return false;
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByName(ip);
                result = inetAddress.isReachable(3000);
            } catch (UnknownHostException e) {
                log.error("IP {} is unkownHost.", ip);
            } catch (IOException e) {
                log.error("IP {} is io Error",ip);
            }
            return result;
        }

        BulkMailAgent buildBulkMailAgent(){
            BulkMailAgent result = null;
            Session session;
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", ip);
            props.setProperty("mail.debug", "false");
            session = Session.getInstance(props);
            return new BulkMailAgent(session);
        }

        boolean sendTestMail(BulkMailAgent agent){
            boolean result = false;
            MimeMessage testMessage = new MimeMessage(agent.getSession());
            try {
                InternetAddress fromAddress = new InternetAddress("sales_account@yahoo.com.tw");
                InternetAddress toAddress = new InternetAddress(App.getConfig().getString("admin.email"));
                fromAddress.setPersonal("admin");
                toAddress.setPersonal("Test Recipient");
                testMessage.setFrom(fromAddress);
                testMessage.addRecipient(Message.RecipientType.TO,toAddress);
                testMessage.setSubject("[TestMail] 測試郵件 from ["+agent.getSession().getProperty("mail.smtp.host")+"]","UTF-8");
                testMessage.setContent("[TestMail] 測試郵件","text/plain; charset=UTF-8");
                result = agent.send(testMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
