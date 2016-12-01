package com.aplool.mail;

import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import com.aplool.mail.utils.MailAgent;
import org.apache.commons.mail.EmailConstants;

import java.net.URL;
import java.util.List;

/**
 * Created by longtai on 2016/12/1.
 */
public class Main {
    public Main(){

    }
    public static void main(String[] args){
        System.out.println("Mail Send Start :");
        try {
        URL url =  Main.class.getClassLoader().getResource("mailHost.config");
        MailHostConfig mailHostConfig = new MailHostConfig(url.getPath());
        url = Main.class.getClassLoader().getResource("mailHeader.config");
        MailHeaderConfig mailHeaderConfig = new MailHeaderConfig(url.getPath());
        List<String> mailToList = MailAgent.loadMailToFromFile(Main.class.getClassLoader().getResource("mailToList.txt").getPath().toString());
        for (String mailToEmail : mailToList) {
            MailAgent mailAgent = new MailAgent(mailHostConfig);
            mailAgent.setMailHeaderConfig(mailHeaderConfig);

            MailItem mailItem = new MailItem();
            MailAddress toEmail = new MailAddress(mailToEmail, mailToEmail);
            mailItem.addTo(toEmail);
            mailItem.contentType = EmailConstants.TEXT_HTML;
            mailItem.message = mailAgent.loadMessageBodyFromFile(Main.class.getClassLoader().getResource("mailBody.txt").getPath().toString());
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
