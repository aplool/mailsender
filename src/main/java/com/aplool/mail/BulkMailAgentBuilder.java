package com.aplool.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by longtai on 2017/2/8.
 */
public class BulkMailAgentBuilder  implements Callable<BulkMailAgent> {
    Logger log = LoggerFactory.getLogger(BulkMailAgentBuilder.class);
    private String ip;
    public BulkMailAgentBuilder(String ip){
        this.ip = ip;
    }
    @Override
    public BulkMailAgent call() throws Exception {
        BulkMailAgent result = null;
        log.debug("[{}] building.",ip);
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
            Multipart multiPart = new MimeMultipart("alternative");
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent("<html><title>Test</title><body>This is 攝氏</body></html>", "text/html; charset=utf-8");
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Mail Body 測試", "utf-8");
            multiPart.addBodyPart(textPart);
            multiPart.addBodyPart(htmlPart);
            testMessage.setContent(multiPart);
//            testMessage.setContent("[TestMail] 測試郵件","text/plain; charset=UTF-8");
            result = agent.send(testMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(),e.getCause());

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e.getCause());

        }
        return result;
    }
}
