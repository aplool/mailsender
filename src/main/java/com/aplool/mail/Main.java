package com.aplool.mail;

import com.aplool.macro.MarcoBuilder;
import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import com.aplool.mail.utils.MailAgent;
import com.aplool.mail.utils.MailHostListFile;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.mail.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by longtai on 2016/12/1.
 */
public class Main {
    static Logger log = LoggerFactory.getLogger(Main.class);

    MailHostConfig mailHostConfig = null;
    MailHeaderConfig mailHeaderConfig;
    String mailListFilename;
    String messageBody;
    MailAgent mailAgent = null;
    MarcoExecutor executor;
    MailHostListFile mailHostListFile;
    EventBus mailBus = new EventBus();

    public Main(String defaultPath) {
        initMarcoExecutor(defaultPath);
        String configPath = new File(defaultPath + "/mailHost.config").getAbsolutePath();
//        mailHostConfig = new MailHostConfig(configPath);
        initMailHostIPList(defaultPath);
        getNextMailHostConfig();
        configPath = new File(defaultPath + "/mailHeader.config").getAbsolutePath();
        mailHeaderConfig = new MailHeaderConfig(configPath);
        mailListFilename = new File(defaultPath + "/mailToList.txt").getAbsolutePath();
        initMailAgent();
        messageBody = new File(defaultPath + "/mailBody.txt").getAbsolutePath();

        mailBus.register(new EventBusSendMail());
    }


    private void initMailAgent() {
        if (mailHostConfig != null) {
            mailAgent = new MailAgent(mailHostConfig);
            mailAgent.setMailHeaderConfig(mailHeaderConfig);
            mailAgent.setMacroExecutor(this.executor);
        } else {
            mailAgent = null;
        }
    }

    private void getNextMailHostConfig() {
        String mailHostIP = mailHostListFile.getNextReachableHost();
        if (mailHostIP != "") {
            mailHostConfig = new MailHostConfig();
            mailHostConfig.setHostAddress(mailHostIP);
            log.info(mailHostIP);
        } else {
            mailHostConfig = null;
        }
    }

    private void initMarcoExecutor(String defaultPath){
        this.executor = new MarcoExecutor();
        try {
            MarcoBuilder.build(this.executor,Paths.get(defaultPath+"/marco"));
        } catch (Exception e) {
            log.error("MarcoExecutor add Extend Marcos Error.", e);
        }
    }

    private void initMailHostIPList(String defaultPath){
        mailHostListFile = new MailHostListFile(defaultPath);
    }

    public void start(){
        sendMailWithEmailList(mailListFilename);
    }
    private void sendMailWithEmailList(String fileName){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach((v)->mailBus.post(v));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class EventBusSendMail {
        @Subscribe
        public void recordCustomerChange(String email) {
            try {
                if (mailAgent != null) {
                    MailItem mailItem = new MailItem();
                    MailAddress toEmail = new MailAddress(email, email);
                    mailItem.addTo(toEmail);
                    mailItem.contentType = EmailConstants.TEXT_HTML;
                    mailItem.message = mailAgent.loadMessageBodyFromFile(messageBody);
                    Boolean sendResult = mailAgent.sendMail(mailItem);
                    log.info("Mail to: {}, Message Body: {} => {}"  ,email,mailItem.message,sendResult.toString());
                    if (!sendResult) {
                        getNextMailHostConfig();
                        initMailAgent();
                    }
                } else {
                    log.info("Mail Fail : Send to {} without Mail Host !", email);
                }
            } catch (IOException e) {
                log.error("Load Message From File Error", e.getCause());
            } catch (Exception e){
                log.error("Email Error ", e.getCause());
            }

        }
    }

    public static void main(String[] args) {
        log.info("Mail Send Start :");

        Main main = new Main(args[0]);
        main.start();
    }
}
