package com.aplool.mail.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2017/2/3.
 */
public class MailAgentManagerTest {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Test
    public void isMailRelayable() throws Exception {
        boolean result = false;
        String ip = "217.193.138.182";
        Transport smtpTransport = null;
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", ip);
        props.setProperty("mail.debug", "false");
        Session smtpSession = Session.getInstance(props);
        try {
            smtpTransport = smtpSession.getTransport();
            smtpTransport.connect();
            result = smtpTransport.isConnected();
            smtpTransport.close();
        } catch (NoSuchProviderException e) {
            log.error("IP {} is not Mail Relay", ip);
        } catch (MessagingException e) {
            log.error("IP {} is not Mail Relay", ip);
        } finally {
            if (smtpTransport != null) {
                try {
                    smtpTransport.close();
                } catch (MessagingException e) {

                }
            }
        }
        log.debug("Test Result : {}",result);
    }

}
