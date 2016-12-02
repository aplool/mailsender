package com.aplool.mail;

import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import com.aplool.mail.utils.MailAgent;
import org.apache.commons.mail.EmailConstants;

import java.io.File;
import java.util.List;

/**
 * Created by longtai on 2016/12/1.
 */
public class Main {
    public Main() {

    }

    public static void main(String[] args) {
        System.out.println("Mail Send Start :");

        try {
            String configPath = new File(args[0] + "/mailHost.config").getAbsolutePath();
            System.out.println(configPath);
            MailHostConfig mailHostConfig = new MailHostConfig(configPath);
            configPath = new File(args[0] + "/mailHeader.config").getAbsolutePath();
            MailHeaderConfig mailHeaderConfig = new MailHeaderConfig(configPath);
            String messageBody = new File(args[0] + "/mailBody.txt").getAbsolutePath();
            List<String> mailToList = MailAgent.loadMailToFromFile(new File(args[0] + "/mailToList.txt").getAbsolutePath());
            for (String mailToEmail : mailToList) {
                MailAgent mailAgent = new MailAgent(mailHostConfig);
                mailAgent.setMailHeaderConfig(mailHeaderConfig);
                mailAgent.setMarcoPath(new File(args[0] + "/marco").getAbsolutePath());

                MailItem mailItem = new MailItem();
                MailAddress toEmail = new MailAddress(mailToEmail, mailToEmail);
                mailItem.addTo(toEmail);
                mailItem.contentType = EmailConstants.TEXT_HTML;
                mailItem.message = mailAgent.loadMessageBodyFromFile(messageBody);
                try {
                    Boolean sendResult = mailAgent.sendMail(mailItem);
                    System.out.println("Mail to: " + mailToEmail + ", Message Body: " + mailItem.message + " => " + sendResult.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
