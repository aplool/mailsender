package com.aplool.mail;

import com.aplool.macro.MarcoBuilder;
import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import com.aplool.mail.utils.MailAgent;
import com.aplool.mail.utils.MailAgentManager;
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
    MailAgentManager mailAgentManager;
    EventBus mailBus = new EventBus();

    public Main(String defaultPath) {
        initMarcoExecutor(defaultPath);
        intiMailAgentManager(defaultPath);
        String configPath = new File(defaultPath + "mailHost.config").getAbsolutePath();

        mailListFilename = new File(defaultPath + "mailToList.txt").getAbsolutePath();

        initMessageBody(defaultPath + "mailBody.txt");

        mailBus.register(new EventBusSendMail());
    }

    private void intiMailAgentManager(String defaultPath) throws RuntimeException{

        mailHostListFile = new MailHostListFile(defaultPath+"mailHostList.txt");
        mailHeaderConfig = new MailHeaderConfig(defaultPath+"mailHeader.config");
        mailAgentManager = new MailAgentManager(this.executor);
        mailAgentManager.setMailHeaders(mailHeaderConfig);
        mailAgentManager.setMailServers(mailHostListFile);
        mailAgent = mailAgentManager.build();
        if(mailAgent == null) throw new RuntimeException("No available Mail Server.");
    }
    private void initMessageBody(String filename){
        try {
            byte[] content = Files.readAllBytes(Paths.get(filename));
            messageBody = new String(content);
        } catch (IOException e) {
            log.error("Message Body init error with : {}", filename);
        }
    }


    private void initMarcoExecutor(String defaultPath){
        this.executor = new MarcoExecutor();
        try {
            MarcoBuilder.build(this.executor,Paths.get(defaultPath+"marco"));
        } catch (Exception e) {
            log.error("MarcoExecutor add Extend Marcos Error.", e);
        }
    }

    public void start(){
        sendMailWithEmailList(mailListFilename);
    }
    private void sendMailWithEmailList(String fileName){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach((v)->{
                mailBus.post(v);
            });

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
                    mailItem.message = messageBody;

                    Boolean sendResult = mailAgent.sendMail(mailItem);
                    log.info("Mail to: {}, Message Body: {} => {}"  ,email,mailItem.message,sendResult.toString());
                    if (!sendResult) {
                        mailAgent = mailAgentManager.build();
                    }
                } else {
                    log.info("Mail Fail1 : Send to {} without Mail Host !", email);
                }
            } catch (Exception e){
                log.info("Mail Fail2 : Send to {} without Mail Host !", email);
                log.error("Mail Fail3",e.getCause());
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        log.info("Mail Send Start :");

        System.out.println(System.getProperty("user.dir"));
        String defaultPath = (args.length==0)?System.getProperty("user.dir")+System.getProperty("file.separator"):args[0];
        Main main = new Main(defaultPath);
        main.start();
    }
}
