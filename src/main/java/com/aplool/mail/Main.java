package com.aplool.mail;

import com.aplool.macro.MarcoBuilder;
import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import com.aplool.mail.utils.MailAgent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.mail.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by longtai on 2016/12/1.
 */
public class Main {
    static Logger log = LoggerFactory.getLogger(Main.class);

    MailHostConfig mailHostConfig;
    MailHeaderConfig mailHeaderConfig;
    String mailListFilename;
    String messageBody;
    MailAgent mailAgent;
    MarcoExecutor executor;
    EventBus mailBus = new EventBus();

    public Main(String defaultPath) {
        initMarcoExecutor(defaultPath);
        String configPath = new File(defaultPath + "/mailHost.config").getAbsolutePath();
        mailHostConfig = new MailHostConfig(configPath);
        configPath = new File(defaultPath + "/mailHeader.config").getAbsolutePath();
        mailHeaderConfig = new MailHeaderConfig(configPath);
        mailListFilename = new File(defaultPath + "/mailToList.txt").getAbsolutePath();
        mailAgent = new MailAgent(mailHostConfig);
        mailAgent.setMailHeaderConfig(mailHeaderConfig);
        mailAgent.setMacroExecutor(this.executor);
        messageBody = new File(defaultPath + "/mailBody.txt").getAbsolutePath();

        mailBus.register(new EventBusSendMail());
    }

    private void initMarcoExecutor(String defaultPath){
        this.executor = new MarcoExecutor();
        try {
            MarcoBuilder.build(this.executor,Paths.get(defaultPath+"/marco"));
        } catch (Exception e) {
            log.error("MarcoExecutor add Extend Marcos Error.", e);
        }
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
                MailItem mailItem = new MailItem();
                MailAddress toEmail = new MailAddress(email, email);
                mailItem.addTo(toEmail);
                mailItem.contentType = EmailConstants.TEXT_HTML;
                mailItem.message = mailAgent.loadMessageBodyFromFile(messageBody);
                Boolean sendResult = mailAgent.sendMail(mailItem);
                System.out.println("Mail to: " + email + ", Message Body: " + mailItem.message + " => " + sendResult.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        System.out.println("Mail Send Start :");

        Main main = new Main(args[0]);
        main.start();


    }
}
