package com.aplool.mail;

import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import com.aplool.mail.utils.MailAgent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.mail.EmailConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by longtai on 2016/12/1.
 */
public class Main {
    MailHostConfig mailHostConfig;
    MailHeaderConfig mailHeaderConfig;
    String mailListFilename;
    String messageBody;
    MailAgent mailAgent;
    EventBus mailBus = new EventBus();

    public Main(String defaultPath) {
        String configPath = new File(defaultPath + "/mailHost.config").getAbsolutePath();
        mailHostConfig = new MailHostConfig(configPath);
        configPath = new File(defaultPath + "/mailHeader.config").getAbsolutePath();
        mailHeaderConfig = new MailHeaderConfig(configPath);
        mailListFilename = new File(defaultPath + "/mailToList.txt").getAbsolutePath();
        mailAgent = new MailAgent(mailHostConfig);
        mailAgent.setMailHeaderConfig(mailHeaderConfig);
        mailAgent.setMarcoPath(new File(defaultPath + "/marco").getAbsolutePath());
        messageBody = new File(defaultPath + "/mailBody.txt").getAbsolutePath();
        mailBus.register(new EventBusSendMail());
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
