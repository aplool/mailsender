package com.aplool.mail.utils;

import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.App;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.lang3.concurrent.CallableBackgroundInitializer;
import org.apache.commons.mail.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by longtai on 2016/12/21.
 */
public class MailAgentManager {
    private static Logger log = LoggerFactory.getLogger(MailAgentManager.class);
    private MarcoExecutor executor;
    private MailHostListFile mailServers;
    private MailHeaderConfig mailHeaders;

    public MailAgentManager(MarcoExecutor executor){
        this.executor = executor;
    }

    public void setMailServers(MailHostListFile mailServers){
        if(mailServers == null) return;
        this.mailServers = mailServers;
    }
    public void setMailHeaders(MailHeaderConfig mailHeaders){
        if(mailHeaders == null) return;
        this.mailHeaders = mailHeaders;
    }
    public boolean isNext(){
        return mailServers.isNext();
    }
    public Callable<MailAgent> get(){
        Callable<MailAgent> result = () ->{
            return build();
        };
        return result;
    }
    public MailAgent build(){
        String ip;
        MailAgent result = null;
        while((ip=mailServers.getNewHostIP()) != ""){
            result = this.build(ip);
            if(result!=null) {
                log.info("IP {} : Mail Relay Server",ip);
                break;
            }
            log.info("IP {} : not Relay", ip);
        }
        return result;
    }
    public MailAgent build(String ip){
        final MailHostConfig mailHost = new MailHostConfig();
        mailHost.setHostAddress(ip);
        return this.build(mailHost);
    }
    public MailAgent build(final MailHostConfig mailHost){

        if(!isMailRelayable(mailHost.getHostAddress())) {
            log.debug("IP {} is mail relay : {}", mailHost.getHostAddress(), "false");
            return null;
        }

        MailAgent mailAgent = new MailAgent(mailHost);
        mailAgent.setMailHeaderConfig(this.mailHeaders);
        mailAgent.setMacroExecutor(this.executor);
        boolean testResult =testSendMail(mailAgent);
        if(!testResult){
            log.debug("IP {} is mail test fail : {}", mailHost.getHostAddress(), "false");
            return null;
        }

        return mailAgent;
    }
    public boolean isMailRelayable(final String ip){
        boolean result = false;
        if(!isReachable(ip)) return false;
        Transport smtpTransport = null;
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", ip);
        props.setProperty("mail.debug", "false");
        //props.put("mail.smtp.connectiontimeout", "5000");
        //props.put("mail.smtp.timeout", "5000");
        Session smtpSession = Session.getInstance(props);
        try {
            smtpTransport = smtpSession.getTransport();
            smtpTransport.connect();
            result = smtpTransport.isConnected();
            smtpTransport.close();
            result = true;
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
        return result;
    }

    public  boolean isReachable(final String ip){
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
    public boolean testSendMail(MailAgent mailAgent) {
        boolean result = false;

        MailItem mailItem = new MailItem();
        MailAddress fromEmail = new MailAddress(App.getConfig().getString("admin.email"), "Mail Admin");
        mailItem.from = fromEmail;
        MailAddress toEmail = new MailAddress(App.getConfig().getString("admin.email"), "Mail Admin");
        mailItem.addTo(toEmail);
        mailItem.subject = "[TestMail] 測試郵件";
        mailItem.contentType = EmailConstants.TEXT_HTML;
        mailItem.message = "This is Test Mail 這是測試郵件";
        result = mailAgent.send(mailItem);

        return result;
    }
}
