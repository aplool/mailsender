package com.aplool.mail;

import com.aplool.macro.MarcoExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by longtai on 2017/2/8.
 */
public class BulkMailAgentBuilder  implements Callable<BulkMailAgent> {
    Logger log = LoggerFactory.getLogger(BulkMailAgentBuilder.class);
    private String ip;
    private MarcoExecutor marcoExecutor;
    public BulkMailAgentBuilder(String ip, MarcoExecutor marcoExecutor){
        this.ip = ip;
        this.marcoExecutor = marcoExecutor;
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


    BulkMailAgent buildBulkMailAgent(){
        BulkMailAgent result = null;
        Session session;
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", ip);
        props.setProperty("mail.debug", "false");
        session = Session.getInstance(props);
        return new BulkMailAgent(session,marcoExecutor);
    }

    boolean sendTestMail(BulkMailAgent agent){
        boolean result = false;
        BulkMimeMessage testMessage = new BulkMimeMessage(agent.getSession());
        try {
            testMessage.updateSubject(marcoExecutor.execute("["+agent.getSession().getProperty("mail.smtp.host")+"] "+"%SUBJECT"));
            testMessage.setFrom(marcoExecutor.execute("%FROM_EMAIL"),marcoExecutor.execute("%FROM_NAME"));
            testMessage.setTo(App.getConfig().getString("admin.email"),marcoExecutor.execute("%FROM_NAME"));
            testMessage.addHtml("<html><title>Test</title><body>This is 攝氏</body></html>");
            result = agent.send(testMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(),e.getCause());
        }
        return result;
    }
}
